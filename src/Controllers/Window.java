package Controllers;

import javax.swing.*;
import java.awt.*;

class Window {

    Window(int width, int height, String title, Game game) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));

        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(game);//obiekt klasy game jest dodawany, aby korzystac z grafiki jframe
        frame.setVisible(true);
    }

}