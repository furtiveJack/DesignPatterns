package fr.uge.poo.paint.ex4;

import java.awt.*;

public class Ellipse extends AbstractEllipseOrRectangle {
    Ellipse(int x, int y,  int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Ellipse{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                '}';
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.drawOval(getX(), getY(), getWidth(), getHeight());
    }
}
