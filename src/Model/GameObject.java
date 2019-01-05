package Model;

import java.awt.*;

/**
 * Klasa abstrakcyjna, dziedziczą po niej wszystkie gameobjecty
 */
public abstract class GameObject {
    int velX, velY;
    int x, y;
    public ID id;

    GameObject(ID id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
