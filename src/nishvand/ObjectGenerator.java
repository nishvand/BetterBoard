package nishvand;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ObjectGenerator {
    public static Image cube(int size){
        return rectangle(size, size);
    }

    public static Image rectangle(int x, int y){
        Image img = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, x, y);
        return img;
    }

    public static Image rectangleWithRoundCorners(int x, int y, float radius){
        Image img = new BufferedImage(x, y, 1);
        Graphics g = img.getGraphics();
        int r = ((int)(y*radius));
        g.setColor(Color.WHITE);
        g.fillRect(r, 0, x - 2*r, y);
        g.fillRect(0, r, x, y - 2*r);
        g.fillOval(0, 0, 2*r, 2*r);
        g.fillOval(x - 2*r, 0, 2*r, 2*r);
        g.fillOval(0, y - 2*r, 2*r, 2*r);
        g.fillOval(x - 2*r, y - 2*r, 2*r, 2*r);
        return img;
    }
}
