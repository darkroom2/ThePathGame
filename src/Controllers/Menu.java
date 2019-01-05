package Controllers;

import Model.Car;
import Model.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.pow;

/**
 * dziedziczy po awt mouseadapter, "nasluchuje" eventow myszki
 */
public class Menu extends MouseAdapter {

    private Handler handler;
    private HUD hud;
    private Car car;
    private Map map;
    private Font font;

    private int level = 1;
    private int maxlevels = 3;

    private GameCfg.DIFFICULTY diff = GameCfg.DIFFICULTY.Easy;

    Menu(Handler handler, HUD hud) {
        this.handler = handler;
        this.hud = hud;
        this.font = GameCfg.getCustomFont();
    }

    static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java
        // 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        for (String line : text.split("\n")) {
            g.drawString(line, x, y);
            y += metrics.getHeight();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (Game.gameState == Game.STATE.Menu) {
            // przycisk gry
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 - 56, 100, 34)) {
                handler.objects.clear();
                if (level > maxlevels) {
                    hud.setScore(0);
                    level = 1;
                }
                this.map = new Map(0, 0, handler, diff, "map" + level);
                handler.objects.add(this.map);
                //samochod musi byc stworzony po mapie, jest zalezny od niej, aby moc sprawdzac kolizje z nia
                this.car = new Car(Game.WIDTH / 2 - 50, Game.HEIGHT / 2 - 56, handler, hud);
                handler.objects.add(this.car);
                //jesli wychodzimy z gry podczas pauzy, to zostaje ona true, zatem zapobiegawczo zawsze po Play ustawiamy na false
                hud.resetScore();
                hud.resetHealth();
                Game.paused = false;
                Game.gameState = Game.STATE.Game;
            }
            // przycisk opcji
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 - 17, 100, 34)) {
                Game.gameState = Game.STATE.Help;
            }
            // high-scores
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 22, 100, 34)) {
                Game.gameState = Game.STATE.Scores;
            }
            // przycisk wyjscia
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34)) {
                System.exit(1);
            }
        }
        //stan help zalatwia Options i Help aby uniknac duplikacji kodu
        if (Game.gameState == Game.STATE.Help) {
            //latwy button
            if (mouseOver(mx, my, Game.WIDTH / 2 - 155, 300, 100, 50)) {
                diff = GameCfg.DIFFICULTY.Easy;
            }

            //sredni button
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, 300, 100, 50)) {
                diff = GameCfg.DIFFICULTY.Medium;
            }

            //hard button
            if (mouseOver(mx, my, Game.WIDTH / 2 + 55, 300, 100, 50)) {
                diff = GameCfg.DIFFICULTY.Hard;
            }

            // przycisk return
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34)) {
                Game.gameState = Game.STATE.Menu;
            }
        }

        if (Game.gameState == Game.STATE.Win) {
            // przycisk return
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34)) {
                if (level == maxlevels) {
                    String player1Name = JOptionPane.showInputDialog("Podaj imie");
                    if (player1Name != null)
                        saveScore(player1Name);
                }
                level++;
                hud.saveScore();
                Game.gameState = Game.STATE.Menu;
            }
        }

        if (Game.gameState == Game.STATE.Lose) {
            // przycisk return
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34)) {
                String player1Name = JOptionPane.showInputDialog("Podaj imie");
                if (player1Name != null)
                    saveScore(player1Name);
                Game.gameState = Game.STATE.Menu;
            }
        }
        if (Game.gameState == Game.STATE.Scores) {
            if (mouseOver(mx, my, Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34)) {
                Game.gameState = Game.STATE.Menu;
            }
        }
    }

    private void saveScore(String name) {
        Double score = hud.getScore() * pow(map.getVelY(), 2);
        GameCfg.hm.addScore(name, score.intValue());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        if (Game.gameState == Game.STATE.Game && !Game.paused) {
            int mx = e.getX();
            int my = e.getY();
            car.setX(mx - car.image.getWidth() / 2);
            car.setY(my - car.image.getHeight() / 2);
        }
    }

    void render(Graphics g) {
        Rectangle rect;
        g.setColor(Color.white);
        g.setFont(font);
        Graphics2D g2d = (Graphics2D) g;

        if (Game.gameState == Game.STATE.Scores) {
            rect = new Rectangle(0, 150, Game.WIDTH, 300);
            drawCenteredString(g, GameCfg.hm.getHighscoreString(), rect, font);
            showReturnButton(g, g2d);
        }

        if (Game.gameState == Game.STATE.Menu) {
            rect = new Rectangle(0, 150, Game.WIDTH, 34);
            drawCenteredString(g, "MENU", rect, font);

            rect = new Rectangle(Game.WIDTH / 2 - 50, Game.HEIGHT / 2 - 56, 100, 34);
            drawCenteredString(g, "Play", rect, font);
            g2d.draw(rect);

            rect = new Rectangle(Game.WIDTH / 2 - 50, Game.HEIGHT / 2 - 17, 100, 34);
            drawCenteredString(g, "Options", rect, font);
            g2d.draw(rect);

            rect = new Rectangle(Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 22, 100, 34);
            drawCenteredString(g, "Scores", rect, font);
            g2d.draw(rect);

            rect = new Rectangle(Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34);
            drawCenteredString(g, "Quit", rect, font);
            g2d.draw(rect);
        }
        if (Game.gameState == Game.STATE.Help) {
            rect = new Rectangle(0, 150, Game.WIDTH, 50);
            drawCenteredString(g, "This is game help. Put game instruction here...", rect, font.deriveFont(Font.ITALIC));

            rect = new Rectangle(0, 250, Game.WIDTH, 50);
            drawCenteredString(g, "Choose difficulty:", rect, font);

            rect = new Rectangle(Game.WIDTH / 2 - 155, 300, 100, 50);
            drawCenteredString(g, "easy", rect, font);
            if (diff == GameCfg.DIFFICULTY.Easy) {
                g2d.setColor(Color.green);
            }
            g2d.draw(rect);
            g2d.setColor(Color.white);

            rect = new Rectangle(Game.WIDTH / 2 - 50, 300, 100, 50);
            drawCenteredString(g, "medium", rect, font);
            if (diff == GameCfg.DIFFICULTY.Medium) {
                g2d.setColor(Color.green);
            }
            g2d.draw(rect);
            g2d.setColor(Color.white);

            rect = new Rectangle(Game.WIDTH / 2 + 55, 300, 100, 50);
            drawCenteredString(g, "hard", rect, font);
            if (diff == GameCfg.DIFFICULTY.Hard) {
                g2d.setColor(Color.green);
            }
            g2d.draw(rect);
            g2d.setColor(Color.white);

            rect = new Rectangle(Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34);
            drawCenteredString(g, "Return", rect, font);
            g2d.draw(rect);
        }
        if (Game.gameState == Game.STATE.Win) {
            rect = new Rectangle(0, 150, Game.WIDTH, 50);

            //getVelY() equivalent with map difficulty
            double score = hud.getScore() * pow(map.getVelY(), 2);

            drawCenteredString(g, "Wygrales z wynikiem: " + score, rect, font);

            showReturnButton(g, g2d);
        }
        if (Game.gameState == Game.STATE.Lose) {
            rect = new Rectangle(0, 150, Game.WIDTH, 50);

            double score = hud.getScore() * pow(map.getVelY(), 2);

            drawCenteredString(g, "Przegrales z wynikiem: " + score, rect, font);

            showReturnButton(g, g2d);
        }

    }

    //unnecesary function suggested by the IDE because it found duplicate code.
    private void showReturnButton(Graphics g, Graphics2D g2d) {
        Rectangle rect;
        rect = new Rectangle(Game.WIDTH / 2 - 50, Game.HEIGHT / 2 + 61, 100, 34);
        drawCenteredString(g, "Menu", rect, font);
        g2d.draw(rect);
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return mx > x && mx < x + width && my > y && my < y + height;
    }

}
