package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Bonus extends GameObject {

    private BufferedImage image;
    private Random rnd = new Random();

    Bonus(int x, int y) {
        super(ID.Bonus, x, y);
        try {
            image = ImageIO.read(new File("res/questionMark.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAction() { // getAction is a randomly 1 or 0
        return rnd.nextInt(2); // 1 or 0
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
