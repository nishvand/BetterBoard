package nishvand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main {
    public static int width = 1280, height = 720;
    public static Engine e = new Engine();

    public static void main(String[] args) {
        e.setPreferredSize(new Dimension(width, height));
        JFrame frame = new JFrame("board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(e, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                e.onResize(evt.getComponent().getWidth(), evt.getComponent().getHeight());
                width = evt.getComponent().getWidth();
                height = evt.getComponent().getHeight();
            }
        });

        e.launch();
    }
}