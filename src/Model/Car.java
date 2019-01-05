package Model;

import Controllers.Game;
import Controllers.GameCfg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Car extends GameObject {

    public BufferedImage image;
    private Map map;


    public Car(ID id, int x, int y, GameObject map) {
        super(id, x, y);
        // get map to check collisions
        this.map = (Map) map;
        try {
            image = ImageIO.read(new File("res/samoch.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        velY = Integer.parseInt(GameCfg.getProps().getProperty("car.speed"));
    }

    @Override
    public void tick() {
        if (wallCollision()) {
            System.out.println("kolizja");
            //TODO:system przyznawania punktow aby moc go wyswietlic po przegraniu i wygraniu
            Game.gameState = Game.STATE.Lose;
        }

        //TODO:zrobic spawn kamieni i po kolizji z nimi odjecie zycia (zrobic booleana ktory po kolizji i odjeciu zycia ustawiamy na true i kiedy jest true to juz nie odejmujemy zycia, pozniej gdy juz nie ma kolizji to go dajemu na false)
        //TODO: pamietac tez zeby sprawdzac jesli zycie <=0 to przegrana

        //jesli samochod wysokosc (y) samochodu nie nalezy(czyli jest wyzej lub nizej) do mapy (leftpoly) to albo nie wjechalismy na mape, albo juz wyjechalismy
        //ale sprawdzamy tez punkt 0,0 ktory na starcie nalezy do mapy a na koncu juz nie, dlatego wygrywamy po wyjezdzie z mapy
        if (!map.leftPoly.contains(0, y + image.getHeight()) && !map.leftPoly.contains(0, 0)) {
            System.out.println("winner");
            //TODO: dodac WINNER screenw
            Game.gameState = Game.STATE.Win;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    private boolean wallCollision() {
        return (map.leftPoly.intersects(getShape().getBounds()) || map.rightPoly.intersects(getShape().getBounds()));
    }

    //funkcja reprezentująca kształt samochodu. Bez bawienia sie z wyłuskiwaniem kształtu samochodu z obrazka.
    private Shape getShape() {
        return new RoundRectangle2D.Float(x + 15, y + 15, image.getWidth() - 30, image.getHeight() - 20, 90.0f, 90.0f);
    }
}
