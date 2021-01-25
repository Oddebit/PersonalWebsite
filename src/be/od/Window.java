package be.od;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                ThemePixer.getPixTheme(app);
            }
        });
    }
}
