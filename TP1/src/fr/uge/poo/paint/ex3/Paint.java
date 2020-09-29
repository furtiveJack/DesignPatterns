package fr.uge.poo.paint.ex3;

import fr.uge.poo.simplegraphics.SimpleGraphics;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Paint {
    private final List<Shape> shapes;
    private Paint(List<Shape> shapes) {
        Objects.requireNonNull(shapes);
        this.shapes = shapes;
    }

    public static Paint createPaint(String filename) {
        var shapes = Shape.parseShapeFile(filename);
        return new Paint(shapes);
    }


    private void drawAll(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        for (Shape s : shapes) {
            s.draw(graphics);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Invalid argument");
        }
        SimpleGraphics area = new SimpleGraphics("area", 800, 600);
        area.clear(Color.WHITE);
        Paint paint = createPaint(args[0]);
        area.render(paint::drawAll);
    }
}
