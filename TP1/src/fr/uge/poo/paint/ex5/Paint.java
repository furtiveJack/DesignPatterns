package fr.uge.poo.paint.ex5;

import fr.uge.poo.simplegraphics.SimpleGraphics;

import java.awt.*;
import java.util.NoSuchElementException;

public class Paint {
    private final Drawing drawing;
    private Paint(Drawing drawing) {
        this.drawing = drawing;
    }

    private void callback(SimpleGraphics area, int x, int y) {
        area.render(graphics -> {
            try {
                drawing.selectClosestShape(graphics, x, y);
            } catch (NoSuchElementException e) {
                System.err.println("No shape loaded");
            }
        });
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage : Paint <filename>");
        }
        SimpleGraphics area = new SimpleGraphics("area", 800, 600);
        area.clear(Color.WHITE);
        Drawing drawing = Drawing.create(args[0]);
        Paint paint = new Paint(drawing);
        area.render(drawing::drawAll);
        area.waitForMouseEvents((x, y) -> paint.callback(area, x, y));
    }
}
