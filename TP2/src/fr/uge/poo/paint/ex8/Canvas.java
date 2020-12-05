package fr.uge.poo.paint.ex8;

public interface Canvas {

    public enum Color {
        ORANGE(java.awt.Color.ORANGE),
        BLACK(java.awt.Color.BLACK),
        WHITE(java.awt.Color.WHITE);

        Color(java.awt.Color color) {
        }
    }

    @FunctionalInterface
    interface MouseCallback {
        void onClick(int x, int y);
    }

    void drawLine(int x1, int y1, int x2, int y2, Color color);

    void drawRectangle(int x, int y, int width, int height, Color color);

    void drawEllipse(int x, int y, int width, int height, Color color);

    void clear(Color color);

    void waitForClickEvents(MouseCallback callback);
}
