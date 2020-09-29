package fr.uge.poo.paint.ex4;

import java.awt.*;

public class Line implements Shape {
    private final int x1, x2, y1, y2;
    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return "Line{" +
                "x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                '}';
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public double getDistanceTo(int x, int y) {
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        return (centerX - x)*(centerX - x) + (centerY - y)*(centerY - y);
    }
}
