package fr.uge.poo.paint.ex7;

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
    public void draw(Canvas canvas, Canvas.Color c) {
        canvas.drawRectangle(x, y, width, height, c);
    }
}
