package fr.uge.poo.paint.ex7;

import java.util.NoSuchElementException;

public class Paint {
    private static Drawing drawing;
    private static Canvas canvas;

    private Paint() {
    }

    private void callback(int x, int y) {
        try {
            drawing.selectClosestShape(canvas, x, y);
        } catch (NoSuchElementException e) {
            System.err.println("No shape loaded");
        }
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new IllegalArgumentException("Usage : Paint <filename> [-legacy]");
        }
        drawing = Drawing.create(args[0]);
        WindowsSize minSize = drawing.getMinWindowsSize(new WindowsSize(500, 500));
        if (args.length == 2 && args[1].equals("-legacy")) {
            canvas = new SimpleGraphicsAdapter(minSize.width, minSize.height);
        }
        else {
            canvas = new CoolGraphicsAdapter(minSize.width, minSize.height);
        }

        canvas.clear(Canvas.Color.WHITE);
        Paint paint = new Paint();
        drawing.drawAll(canvas);
        canvas.waitForClickEvents(paint::callback);
    }
}
