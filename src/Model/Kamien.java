package Model;

import Controllers.GameCfg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Kamien extends GameObject {

    private BufferedImage image;

    Kamien(ID id, int x, int y) {
        super(id, x, y);
        velY = -1;
        try {
            image = ImageIO.read(new File("res/kamien.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        velY = Integer.parseInt(GameCfg.getProps().getProperty("kamien.speed"));

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
}
