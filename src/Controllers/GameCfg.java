package Controllers;

import HighScore.HighscoreManager;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameCfg {

    private static Properties props = new Properties();
    private static Font customFont;
    static HighscoreManager hm;

    public enum DIFFICULTY {
        Easy, Medium, Hard
    }


    public GameCfg() {
        hm = new HighscoreManager();

        System.out.println(hm.getHighscoreString());

        try {
            FileInputStream inFile = new FileInputStream("res/config.cfg");
            props.load(inFile);
            inFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/CenturyGothicBold.ttf")).deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //register the font
        ge.registerFont(customFont);

    }


    public static Properties getProps() {
        return props;
    }

    public static Font getCustomFont() {
        return customFont;
    }
}
