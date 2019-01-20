package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Serce extends GameObject {

    private BufferedImage image;

    Serce(int x, int y) {
        super(ID.Serce, x, y);
        try {
            image = ImageIO.read(new File("res/serce.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, null);

    }

    @Override
    public void setVelY(int velY) {
        this.velY = velY;

    }


    @Override
    public Rectangle2D getShape() {
        Rectangle2D rect = new Rectangle();
        rect.setRect(x + 10, y + 10, image.getWidth() - 20, image.getHeight() - 20);
        return rect;
    }
}
