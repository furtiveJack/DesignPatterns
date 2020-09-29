package fr.uge.poo.paint.ex4;

import fr.uge.poo.simplegraphics.SimpleGraphics;

import java.awt.*;

public class Paint {
    private final Drawing drawing;
    private Paint(Drawing drawing) {
        this.drawing = drawing;
    }

    private void callback(SimpleGraphics area, int x, int y) {
        area.render(graphics -> {
            System.out.println(drawing.selectClosestShape(x, y));
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
