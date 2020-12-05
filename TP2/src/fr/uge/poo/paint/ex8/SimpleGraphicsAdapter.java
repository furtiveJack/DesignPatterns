package fr.uge.poo.paint.ex8;

import fr.uge.poo.simplegraphics.SimpleGraphics;

public class SimpleGraphicsAdapter implements Canvas {
    private final SimpleGraphics area;

    public SimpleGraphicsAdapter(int width, int height) {
        area = new SimpleGraphics("area", width, height);
    }

    public Canvas createCanvas(int width, int height) {
        return new SimpleGraphicsAdapter(width, height);
    }

    static java.awt.Color convertColor(Color color) {
        return switch (color) {
            case BLACK -> java.awt.Color.BLACK;
            case WHITE -> java.awt.Color.WHITE;
            case ORANGE -> java.awt.Color.ORANGE;
        };
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        area.render(graphics -> {
            graphics.setColor(convertColor(color));
            graphics.drawLine(x1, y1, x2, y2);
        });
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, Color color) {
        area.render(graphics -> {
            graphics.setColor(convertColor(color));
            graphics.drawRect(x, y, width, height);
        });
    }

    @Override
    public void drawEllipse(int x, int y, int width, int height, Color color) {
        area.render(graphics -> {
            graphics.setColor(convertColor(color));
            graphics.drawOval(x, y, width, height);
        });

    }

    @Override
    public void clear(Color color) {
        switch (color) {
            case BLACK: area.clear(convertColor(color));
            case WHITE: area.clear(convertColor(color));
            case ORANGE: area.clear(convertColor(color));
        }
    }

    @Override
    public void waitForClickEvents(MouseCallback callback) {
        area.waitForMouseEvents(callback::onClick);
    }
}
