package fr.uge.poo.paint.ex4;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface Shape {
    void draw(Graphics2D graphics);

    double getDistanceTo(int x, int y);

    static List<Shape> parseShapeFile(String filename) {
        Path path = Paths.get(filename);
        var list = new ArrayList<Shape>();
        try(Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                list.add(parseShape(line));
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read file " + filename);
        }
        return list;
    }

    private static Shape parseShape(String line) {
        String[] tokens = line.split(" ");
        switch (tokens[0]) {
            case "line" : return new Line(Integer.parseInt(tokens[1]),
                                            Integer.parseInt(tokens[2]),
                                            Integer.parseInt(tokens[3]),
                                            Integer.parseInt(tokens[4]));
            case "ellipse" : return new Ellipse(Integer.parseInt(tokens[1]),
                                            Integer.parseInt(tokens[2]),
                                            Integer.parseInt(tokens[3]),
                                            Integer.parseInt(tokens[4]));
            case "rectangle" : return new Rectangle(Integer.parseInt(tokens[1]),
                                            Integer.parseInt(tokens[2]),
                                            Integer.parseInt(tokens[3]),
                                            Integer.parseInt(tokens[4]));
            default: throw new UnsupportedOperationException("Unknown shape " + tokens[0]);
        }
    }
}
