package fr.uge.poo.paint.ex6;

import fr.uge.poo.simplegraphics.SimpleGraphics;

import java.awt.*;
import java.util.NoSuchElementException;

public class Paint {
    private static Drawing drawing;
    private Paint() {
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
        drawing = Drawing.create(args[0]);
        WindowsSize ws = drawing.getMinWindowsSize(new WindowsSize(500, 500));
        SimpleGraphics area = new SimpleGraphics("area", ws.width, ws.height);
        area.clear(Color.WHITE);
        Paint paint = new Paint();
        area.render(drawing::drawAll);
        area.waitForMouseEvents((x, y) -> paint.callback(area, x, y));
    }
}
