package fr.uge.poo.paint.ex5;

import java.awt.*;

public class Rectangle extends AbstractEllipseOrRectangle {
    Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                '}';
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.drawRect(getX(), getY(), getWidth(), getHeight());
    }
}
