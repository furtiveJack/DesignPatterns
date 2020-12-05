package fr.uge.poo.paint.ex9;

public interface Canvas {

    public enum Color {
        ORANGE(java.awt.Color.ORANGE),
        BLACK(java.awt.Color.BLACK),
        WHITE(java.awt.Color.WHITE);

        Color(java.awt.Color color) {
        }
    }

    @FunctionalInterface
    public interface MouseCallback {
        void onClick(int x, int y);
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color);

    public void drawRectangle(int x, int y, int width, int height, Color color);

    public void drawEllipse(int x, int y, int width, int height, Color color);

    public void clear(Color color);

    public void render();

    public void waitForClickEvents(MouseCallback callback);
}
