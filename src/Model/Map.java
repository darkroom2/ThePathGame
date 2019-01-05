package Model;

import Controllers.GameCfg;

import java.awt.*;
import java.util.stream.Stream;

public class Map extends GameObject {

    //int obstacleCount;
    //int extraLivesCount;
    //int circleCount;
    int[] leftCoords;
    int[] rightCoords;
    Polygon leftPoly;
    Polygon rightPoly;

    public Map(ID id, int x, int y, GameCfg.DIFFICULTY diff) {
        super(id, x, y);
        leftPoly = new Polygon();
        rightPoly = new Polygon();
        velY = loadDifficulty(diff);

        // fancy-complex way of converting string to int array. first we get property normally, and split it, and use it in Stream.of and then use mapToInt using Integer::parseInt and lastly we make array from the output
        leftCoords = Stream.of(GameCfg.getProps().getProperty("map.leftPoly").split(",")).mapToInt(Integer::parseInt).toArray();
        rightCoords = Stream.of(GameCfg.getProps().getProperty("map.rightPoly").split(",")).mapToInt(Integer::parseInt).toArray();
        //dodajemy w odwrotnej kolejnosci (od tylu) (od poczatku cos nie dizalalo wiec od tylu), przeskakujemy co 2 wpisy, zakladamy ze leftCoords, i right coords maja taka sama dlugosc (warunek petli)
        for (int i = leftCoords.length - 1; i > 0; i -= 2) {
            leftPoly.addPoint(leftCoords[i - 1], leftCoords[i]);
            rightPoly.addPoint(rightCoords[i - 1], rightCoords[i]);
        }
        //przesuwamy jednorazowo mape o 50 jednostek w dol, aby na starcie bylo widac kawalek
        leftPoly.translate(0, 30 * velY);
        rightPoly.translate(0, 30 * velY);
    }

    private int loadDifficulty(GameCfg.DIFFICULTY diff) {
        switch (diff) {
            case Easy:
                return Integer.parseInt(GameCfg.getProps().getProperty("map.EasySpeed"));
            case Medium:
                return Integer.parseInt(GameCfg.getProps().getProperty("map.MediumSpeed"));
            case Hard:
                return Integer.parseInt(GameCfg.getProps().getProperty("map.HardSpeed"));
            default:
                return 0;
        }
    }

    @Override
    public void tick() {
        // x += velX;
        // y += velY;
        leftPoly.translate(0, velY);
        rightPoly.translate(0, velY);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillPolygon(leftPoly);
        g.fillPolygon(rightPoly);
    }
}