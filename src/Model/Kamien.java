package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Kamien extends GameObject {

    private BufferedImage image;

    Kamien(int x, int y) {
        super(ID.Kamien, x, y);
        //velY = -1;
        try {
            image = ImageIO.read(new File("res/kamien.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //velY = Integer.parseInt(GameCfg.getProps().getProperty("kamien.speed"));

    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.YELLOW);
//        g2d.draw(getShape());
    }

    public Shape getShape() {
        Rectangle2D rect = new Rectangle();
        rect.setRect(x + 10, y + 10, image.getWidth() - 20, image.getHeight() - 20);
        return rect;
    }
}
