package Controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Główna klasa dla gry, używa metod do renderowania obiektów i zmiany ich pozycji na ekranie
 */
public class Game extends JPanel implements Runnable {

    //ogolnie static uzywamy gdy tworzymy wiele instancji (obiektow) danej klasy i nie chcemy zeby kazdy z tych obiektow tworzyl oddzielnie na nowo te zmienne
    //static zaś powoduje jednorazowe stworzenie zmiennej i współdzielenie jej miedzy kilkoma obiektami danej klasy
    //dlatego uzywanie static jako ulatwienie dostepu bez deklaracji klasy (ogolnego) nie jest najlepszym rozwiazaniem
    public static int WIDTH;
    public static int HEIGHT;
    public static int scaleX;
    public static int scaleY;

    //bez specyfikatora oznacza ze jest package-private.
    public enum STATE {
        Menu, Game, Help, Scores, Win, Lose
    }

    public static STATE gameState = STATE.Menu;
    public static Rectangle screenRect;
    private Controllers.Menu menu;
    private Handler handler;
    private HUD hud;
    private boolean running = false;
    static boolean paused = false;

    private Game() {

        WIDTH = Integer.parseInt(GameCfg.getProps().getProperty("game.width"));
        HEIGHT = Integer.parseInt(GameCfg.getProps().getProperty("game.height"));
        scaleX = WIDTH / 600;
        scaleY = HEIGHT / 800;
        screenRect = new Rectangle(0, 0, WIDTH, HEIGHT);
        handler = new Handler();
        hud = new HUD(Integer.parseInt(GameCfg.getProps().getProperty("game.livesCount")));
        menu = new Menu(handler, hud);
        //tworzymy mape
        this.addMouseMotionListener(menu);
        this.addMouseListener(menu);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (gameState == STATE.Game) {
                    if (key == KeyEvent.VK_P) {
                        paused = (!paused);

                    }
                    if (key == KeyEvent.VK_ESCAPE) {
                        handler.objects.clear();
                        gameState = STATE.Menu;
                    }
                }
            }
        });
        new Window(WIDTH, HEIGHT, "The Path Game", this);
        start();
    }

    public static void main(String[] args) {
        new GameCfg();
        new Game();

    }

    private synchronized void start() {
        Thread t1 = new Thread(this);
        t1.start();
        running = true;
    }

    @Override
    public void run() {
        requestFocus();
        double previous = System.nanoTime();
        double lag = 0.0;
        double ns = 1000000000.0 / 60.0;

        // show fps
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            double current = System.nanoTime();
            lag += (current - previous);
            previous = current;
            while (lag >= ns) {
                lag -= ns;
                tick(); //
                frames++;
            }
            repaint();
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                //System.out.println(frames + " fps");
                frames = 0;
            }
        }
    }

    /**
     * Za każdym obrotem pętli gry tylko w stanie GAME wykonujemy metodę tick z handlera
     */
    private void tick() {
        if (gameState == STATE.Game) {
            if (!paused) {
                handler.tick();
                hud.tick();
            }
        }
    }

    /**
     * Po uzyciu repaint ta metoda wywoluje sie automatycznie(przeciazona)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (gameState == STATE.Game) {
            if (!paused) {


                handler.render(g);
                hud.render(g);

            } else {

                handler.render(g);
                g.setColor(Color.white);
                Menu.drawCenteredString(g, "PAUSE", screenRect, GameCfg.getCustomFont());
            }
        } else {
            menu.render(g);
        }
        g.dispose();
    }
}
