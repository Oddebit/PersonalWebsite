package be.od;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {

    public Window(int width, int height, String title, App app) {
        JFrame frame = new JFrame(title);

        frame.setMinimumSize(new Dimension(width, height));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(app);
        frame.setVisible(true);
        frame.requestFocus();
    }

}
