package Model;

import Controllers.GameCfg;
import Controllers.Handler;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Map extends GameObject {

    private Handler handler;

    Polygon leftPoly;
    Polygon rightPoly;

    public Map(int x, int y, Handler handler, GameCfg.DIFFICULTY diff, String level) {
        super(ID.Map, x, y);
        this.handler = handler;
        leftPoly = new Polygon();
        rightPoly = new Polygon();

        velY = loadDifficulty(diff);

        // fancy-complex way of converting string to int array. first we get property normally, and split it, and use it in Stream.of and then use mapToInt using Integer::parseInt and lastly we make array from the output
        int[] leftCoords = Stream.of(GameCfg.getProps().getProperty(level + ".leftPoly").split(",")).mapToInt(Integer::parseInt).toArray();
        int[] rightCoords = Stream.of(GameCfg.getProps().getProperty(level + ".rightPoly").split(",")).mapToInt(Integer::parseInt).toArray();
        int[] obstacleCoords = Stream.of(GameCfg.getProps().getProperty(level + ".obstacleCoords").split(",")).mapToInt(Integer::parseInt).toArray();
        int[] lifeCoords = Stream.of(GameCfg.getProps().getProperty(level + ".lifeCoords").split(",")).mapToInt(Integer::parseInt).toArray();
        int[] bonusCoords = Stream.of(GameCfg.getProps().getProperty(level + ".bonusCoords").split(",")).mapToInt(Integer::parseInt).toArray();

        //dodajemy w odwrotnej kolejnosci (od tylu) (od poczatku cos nie dizalalo wiec od tylu), przeskakujemy co 2 wpisy, zakladamy ze leftCoords, i right coords maja taka sama dlugosc (warunek petli)
        for (int i = leftCoords.length - 1; i > 0; i -= 2) {
            leftPoly.addPoint(leftCoords[i - 1], leftCoords[i]);
            rightPoly.addPoint(rightCoords[i - 1], rightCoords[i]);
        }

        for (int i = 0; i < obstacleCoords.length / 2; ++i) {
            Kamien kamyk = new Kamien(obstacleCoords[i * 2] - 50, obstacleCoords[i * 2 + 1] + 50);
            kamyk.setVelY(velY);
            handler.addObject(kamyk);
        }
        for (int i = 0; i < lifeCoords.length / 2; ++i) {
            Serce serce = new Serce(lifeCoords[i * 2] - 50, lifeCoords[i * 2 + 1] + 50);
            serce.setVelY(velY);
            handler.addObject(serce);
        }
        for (int i = 0; i < bonusCoords.length / 2; ++i) {
            Bonus bonus = new Bonus(bonusCoords[i * 2] - 50, bonusCoords[i * 2 + 1] + 50);
            bonus.setVelY(velY);
            handler.addObject(bonus);
        }
        //przesuwamy jednorazowo mape o 50 jednostek w dol, aby na starcie bylo widac kawalek
        leftPoly.translate(0, 30 * velY);
        rightPoly.translate(0, 30 * velY);
    }


    //private int random(Random rnd, int min, int max) {
    //    return rnd.nextInt(max - min + 1) + min;
    //}

//    private void generateMap() {
//        Random rnd = new Random(5);
//        int coordX;
//        int coordX2;
//        int i;
//        leftCoords = new int[34];
//        rightCoords = new int[34];
//        leftPoly.addPoint(0, 0);
//        leftCoords[0] = 0;
//        leftCoords[1] = 0;
//        rightPoly.addPoint(600, 0);
//        rightCoords[0] = 600;
//        rightCoords[1] = 0;
//        for (i = 0; i < 15; ++i) {
//            coordX = random(rnd, 0, 300);
//            coordX2 = random(rnd, coordX + 100, coordX + 300);
//
//            leftPoly.addPoint(coordX, -i * 300);
//            leftCoords[(i + 1) * 2] = coordX;
//            leftCoords[(i + 1) * 2 + 1] = -i * 300;
//
//            rightPoly.addPoint(coordX2, -i * 300);
//            rightCoords[(i + 1) * 2] = coordX2;
//            rightCoords[(i + 1) * 2 + 1] = -i * 300;
//        }
//
//        leftPoly.addPoint(0, (-i + 1) * 300);
//        rightPoly.addPoint(600, (-i + 1) * 300);
//        leftCoords[(i + 1) * 2] = 0;
//        leftCoords[(i + 1) * 2 + 1] = (-i + 1) * 300;
//        rightCoords[(i + 1) * 2] = 600;
//        rightCoords[(i + 1) * 2 + 1] = (-i + 1) * 300;
//        for (int coords : leftCoords) {
//            System.out.print(coords + ",");
//        }
//        System.out.println(" ");
//        for (int coords : rightCoords) {
//            System.out.print(coords + ",");
//        }
//    }

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

    @Override
    public void setVelY(int velY) {
        this.velY = velY;
    }

    @Override
    public Rectangle2D getShape() {
        return null;
    }

    public void makeMapFaster(int vel, int duration) {
        int restoreVelY = this.velY;
        handler.updateVelY(vel);
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                handler.updateVelY(restoreVelY);
            }
        };
        ses.schedule(task, duration, TimeUnit.SECONDS);
        ses.shutdown();
    }
}