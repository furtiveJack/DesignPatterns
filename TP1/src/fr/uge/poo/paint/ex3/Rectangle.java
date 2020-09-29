package fr.uge.poo.paint.ex3;

import java.awt.*;

public class Rectangle implements Shape {
    private final int x, y, width, height;
    Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.drawRect(x, y, width, height);
    }
}
