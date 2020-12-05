package fr.uge.poo.paint.ex9;

import com.evilcorp.coolgraphics.CoolGraphics;

import java.util.ArrayDeque;

public class CoolGraphicsAdapter implements Canvas {
    private CoolGraphics area;
    private final ArrayDeque<Runnable> drawings = new ArrayDeque<>();

    public CoolGraphicsAdapter(int width, int height) {
        this.area = new CoolGraphics("area", width, height);
    }

    static CoolGraphics.ColorPlus convertColor(Color c) {
        return switch (c) {
            case BLACK -> CoolGraphics.ColorPlus.BLACK;
            case WHITE -> CoolGraphics.ColorPlus.WHITE;
            case ORANGE -> CoolGraphics.ColorPlus.ORANGE;
        };
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        drawings.add(() -> area.drawLine(x1, y1, x2, y2, convertColor(color)));
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, Color color) {
        drawings.add(() -> {
            CoolGraphics.ColorPlus c = convertColor(color);
            area.drawLine(x, y, x + width, y, c);
            area.drawLine(x + width, y, x + width, y + height, c);
            area.drawLine(x, y, x, y + height, c);
            area.drawLine(x, y + height, x + width, y + height, c);
        });
    }

    @Override
    public void drawEllipse(int x, int y, int width, int height, Color color) {
        drawings.add(() -> {
            area.drawEllipse(x, y, width, height, convertColor(color));
        });
    }

    @Override
    public void clear(Color color) {

        area.repaint(convertColor(color));
    }

    @Override
    public void render() {
        while ( ! drawings.isEmpty()) {
            drawings.pop().run();
        }
    }

    @Override
    public void waitForClickEvents(MouseCallback callback) {
        area.waitForMouseEvents(callback::onClick);
    }
}
