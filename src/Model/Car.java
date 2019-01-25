package Model;

import Controllers.Game;
import Controllers.HUD;
import Controllers.Handler;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Car extends GameObject {

    public BufferedImage image;
    private Handler handler;
    private HUD hud;
    private Map map;

    private boolean colliding = false;

    public Car(int x, int y, Handler handler, HUD hud) {
        super(ID.Car, x, y);
        // get map to check collisions
        this.handler = handler;
        this.hud = hud;
        try {
            image = ImageIO.read(new File("res/samoch.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map = (Map) handler.getObject(ID.Map);
    }

    @Override
    public void tick() {
        if (wallCollision()) {
            System.out.println("kolizja");
            Game.gameState = Game.STATE.Lose;
        }

        //TODO: zrobic spawn kamieni i po kolizji z nimi odjecie zycia (zrobic booleana ktory po kolizji i odjeciu zycia ustawiamy na true i kiedy jest true to juz nie odejmujemy zycia, pozniej gdy juz nie ma kolizji to go dajemu na false)
        //TODO: pamietac tez zeby sprawdzac jesli zycie <=0 to przegrana

        //jesli samochod wysokosc (y) samochodu nie nalezy(czyli jest wyzej lub nizej) do mapy (leftpoly) to albo nie wjechalismy na mape, albo juz wyjechalismy
        //ale sprawdzamy tez punkt 0,0 ktory na starcie nalezy do mapy a na koncu juz nie, dlatego wygrywamy po wyjezdzie z mapy
        if (winCondition()) {
            System.out.println("winner");
            Game.gameState = Game.STATE.Win;
        }
        Pair<Boolean, GameObject> kolizja = obstacleCollision();
        if (kolizja.getKey()) { // Key is the first value of Pair (boolean in our case)
            if (!colliding) {
                if (kolizja.getValue().getId() == ID.Kamien) {
                    hud.HEALTH--;
                    hud.addPoints(-50); // za strate zycia obcinamy 5k pkt
                    if (hud.HEALTH <= 0)
                        Game.gameState = Game.STATE.Lose;
                } else if (kolizja.getValue().getId() == ID.Serce) { // tu serce
                    hud.HEALTH++;
                } else { // tu bonus
                    Bonus bonus = (Bonus) kolizja.getValue(); // castujemy GameObject na Bonus bo bonus ma wlasna metode ktorej nie mozemu uzyc za pomoca bazowej klasy GameObject
                    int akcja = bonus.getAction();
                    System.out.println(akcja);
                    if (akcja == 0) {
                        //System.out.println(0);
                        map.makeMapFaster(map.getVelY() + 3, 3);
                    } else {
                        //System.out.println(1);
                        hud.addPoints(50);
                        // TODO: LAST ThiNG TO FIX IS SCORE SYSTEM AND WE ARE DONE SIEMA GITHUB
                    }
                }
            }
            colliding = true;
        } else {
            colliding = false;
        }
    }

    private Pair<Boolean, GameObject> obstacleCollision() {
        for (GameObject tempObject : handler.objects) {
            if (tempObject.getId() == ID.Kamien && tempObject.getY() > y - 100 && tempObject.getY() < y) {
                Kamien kamien = (Kamien) tempObject;
                boolean collision = kamien.getShape().intersects(getShape().getBounds2D());
                return new Pair<>(collision, tempObject);
            } else if (tempObject.getId() == ID.Serce && tempObject.getY() > y - 100 && tempObject.getY() < y) {
                Serce serce = (Serce) tempObject;
                boolean collision = serce.getShape().intersects(getShape().getBounds2D());
                return new Pair<>(collision, tempObject);
            } else if (tempObject.getId() == ID.Bonus && tempObject.getY() > y - 100 && tempObject.getY() < y) {
                Bonus bonus = (Bonus) tempObject;
                boolean collision = bonus.getShape().intersects(getShape().getBounds2D());
                return new Pair<>(collision, tempObject);
            }
        }
        return new Pair<>(false, null);
    }

    private boolean winCondition() {
        return !map.leftPoly.contains(0, y + image.getHeight()) && !map.leftPoly.contains(0, 0);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.draw(getShape().getBounds());
    }

    @Override
    public void setVelY(int velY) {

    }

    private boolean wallCollision() {
        return (map.leftPoly.intersects(getShape().getBounds()) || map.rightPoly.intersects(getShape().getBounds()));
    }

    //funkcja reprezentująca kształt samochodu. Bez bawienia sie z wyłuskiwaniem kształtu samochodu z obrazka.
    @Override
    public Rectangle2D getShape() {
        Rectangle2D rect = new Rectangle();
        rect.setRect(x + 10, y + 10, image.getWidth() - 20, image.getHeight() - 20);
        return rect;
    }
}
