package fr.uge.poo.paint.ex6;

import java.awt.*;

public class Rectangle extends AbstractEllipseOrRectangle {
    Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.drawRect(x, y, width, height);
    }
}
