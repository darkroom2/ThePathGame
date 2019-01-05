package Controllers;

import java.awt.*;

//ta klasa jako przyklad nie uzywania static xd
public class HUD {


    private int score = 0;
    private int HEALTH = 3;
    private int maxHealth;

    public HUD(int maxHealth) {
        this.maxHealth = maxHealth;
        this.HEALTH = maxHealth;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void render(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(10, 10, 200, 20);
        g.setColor(Color.GREEN);
        g.fillRect(10, 10, 200 / maxHealth * HEALTH, 20);
    }

    public void tick() {
        score++;
    }
}
