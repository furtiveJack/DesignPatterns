package fr.uge.poo.paint.ex2;

import fr.uge.poo.simplegraphics.SimpleGraphics;
import fr.uge.poo.simplegraphics.SimpleGraphicsExample;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Paint {
    static class Line {
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

        public void draw(Graphics2D graphics) {
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

    private final List<Line> lines;
    private Paint(List<Line> lines) {
        this.lines = lines;
    }

    public static Paint createPaint(String filename) {
        Path path = Paths.get(filename);
        var list = new ArrayList<Line>();
        try(Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                String[] tokens = line.split(" ");
                Line newLine = new Line(Integer.parseInt(tokens[1]),
                                        Integer.parseInt(tokens[2]),
                                        Integer.parseInt(tokens[3]),
                                        Integer.parseInt(tokens[4]));
                list.add(newLine);
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read file " + filename);
        }
        return new Paint(list);
    }


    private void drawAll(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        for (Line l : lines) {
            l.draw(graphics);
        }
    }

    @Override
    public String toString() {
        return "Paint{" +
                "lines=" + lines +
                '}';
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Invalid argument");
        }
        SimpleGraphics area = new SimpleGraphics("area", 800, 600);
        area.clear(Color.WHITE);
        Paint paint = createPaint(args[0]);
        System.out.println(paint);
        area.render(paint::drawAll);
    }
}
