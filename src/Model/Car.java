package Model;

import Controllers.Game;
import Controllers.HUD;
import Controllers.Handler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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
        if (obstacleCollision()) {
            if (!colliding) {
                hud.HEALTH--;
                if (hud.HEALTH <= 0)
                    Game.gameState = Game.STATE.Lose;
            }
            colliding = true;
        } else {
            colliding = false;
        }
    }

    private boolean obstacleCollision() {
        for (GameObject tempObject : handler.objects)
            if (tempObject.getId() == ID.Kamien && tempObject.getY() > y - 100 && tempObject.getY() < y) {
                Kamien kamien = (Kamien) tempObject;
                return kamien.getShape().intersects(getShape().getBounds2D());
            }
        return false;
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

    private boolean wallCollision() {
        return (map.leftPoly.intersects(getShape().getBounds()) || map.rightPoly.intersects(getShape().getBounds()));
    }

    //funkcja reprezentująca kształt samochodu. Bez bawienia sie z wyłuskiwaniem kształtu samochodu z obrazka.
    private Shape getShape() {
        return new RoundRectangle2D.Float(x + 15, y + 15, image.getWidth() - 30, image.getHeight() - 20, 90.0f, 90.0f);
    }
}
