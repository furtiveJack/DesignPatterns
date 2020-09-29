package fr.uge.poo.paint.ex4;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Drawing {
    private final List<Shape> shapes;

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

    public Shape selectClosestShape(int x, int y) {
        var closest = shapes.stream()
                .min((a, b) -> (int) (a.getDistanceTo(x, y) - b.getDistanceTo(x, y)));
        if (closest.isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return closest.get();
    }

    void drawAll(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        for (Shape s : shapes) {
            s.draw(graphics);
        }
    }
}
