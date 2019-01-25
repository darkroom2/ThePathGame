package Controllers;

import java.awt.*;

//ta klasa jako przyklad nie uzywania static xd
public class HUD {


    private int score = 0;
    private int lastScore = 0;
    public int HEALTH;
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
        this.lastScore = score;
    }

    public void render(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(10, 10, 200, 20);
        g.setColor(Color.GREEN);
        g.fillRect(10, 10, 200 / maxHealth * HEALTH, 20);

        Menu.drawCenteredString(g, "" + score, Game.screenRect, g.getFont());
    }

    public void tick() {
        score++;
    }

    public void restoreHealth() {
        HEALTH = maxHealth;
    }

    public void saveScore() {
        lastScore = score;
    }

    public void restoreScore() {
        score = lastScore;
    }

    public void addPoints(int i) {
        score += i;
    }
}
