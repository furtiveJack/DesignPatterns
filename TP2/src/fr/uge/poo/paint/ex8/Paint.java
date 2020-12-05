package fr.uge.poo.paint.ex8;

import com.evilcorp.coolgraphics.CoolGraphics;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

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
            throw new IllegalArgumentException("Usage : Paint <filename>");
        }
        drawing = Drawing.create(args[0]);
        WindowsSize minSize = drawing.getMinWindowsSize(new WindowsSize(500, 500));
        ServiceLoader<Canvas> loader = ServiceLoader.load(fr.uge.poo.paint.ex8.Canvas.class);
        System.out.println("Coucou :"  + loader);
        canvas = new SimpleGraphicsAdapter(minSize.width, minSize.height);

//        canvas = new CoolGraphicsAdapter(minSize.width, minSize.height);

        canvas.clear(Canvas.Color.WHITE);
        Paint paint = new Paint();
        drawing.drawAll(canvas);
        canvas.waitForClickEvents(paint::callback);
    }
}
