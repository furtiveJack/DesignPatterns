package fr.uge.poo.paint.ex5;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Drawing {
    private final List<Shape> shapes;
    private Shape current = null;

    Drawing(List<Shape> shapes) {
        Objects.requireNonNull(shapes);
        this.shapes = shapes;
    }

    public static Drawing create(String filename) {
        var shapes = Shape.parseShapeFile(filename);
        if (shapes.isEmpty()) {
            throw new IllegalArgumentException("File " + filename + " doesn't contain shapes");
        }
        return new Drawing(shapes);
    }

    public void selectClosestShape(Graphics2D graphics, int x, int y) {
        var closest = shapes.stream()
                .min((a, b) -> (int) (a.getDistanceTo(x, y) - b.getDistanceTo(x, y)));
        if (closest.isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        drawCurrent(graphics, Color.BLACK);
        current = closest.get();
        drawCurrent(graphics, Color.ORANGE);

    }

    void drawAll(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        for (Shape s : shapes) {
            s.draw(graphics);
        }
    }

    void drawCurrent(Graphics2D graphics, Color c) {
        if (current == null) {
            return;
        }
        graphics.setColor(c);
        current.draw(graphics);
    }
}
