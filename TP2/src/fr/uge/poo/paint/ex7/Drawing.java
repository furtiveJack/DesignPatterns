package fr.uge.poo.paint.ex7;

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

    public WindowsSize getMinWindowsSize(WindowsSize defaultSize) {
        int width = Math.max(shapes.stream()
                        .map(a -> a.getMinSize().width)
                        .max(Integer::compareTo).orElse(defaultSize.width)
                , defaultSize.width);
        int height = Math.max(shapes.stream()
                        .map(a -> a.getMinSize().height)
                        .max(Integer::compareTo).orElse(defaultSize.height)
                , defaultSize.height);
        return new WindowsSize(width + 50, height + 50);
    }

    public void selectClosestShape(Canvas canvas, int x, int y) {
        var closest = shapes.stream()
                .min((a, b) -> (int) (a.getDistanceTo(x, y) - b.getDistanceTo(x, y)));
        if (closest.isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        drawCurrent(canvas, Canvas.Color.BLACK);
        current = closest.get();
        drawCurrent(canvas, Canvas.Color.ORANGE);
    }

    public void drawAll(Canvas canvas) {
        Canvas.Color color = Canvas.Color.BLACK;
        for (Shape s : shapes) {
            s.draw(canvas, color);
        }
    }

    private void drawCurrent(Canvas canvas, Canvas.Color c) {
        if (current == null) {
            return;
        }
        current.draw(canvas, c);
    }
}
