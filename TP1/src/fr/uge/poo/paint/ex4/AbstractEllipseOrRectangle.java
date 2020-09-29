package fr.uge.poo.paint.ex4;

public abstract  class AbstractEllipseOrRectangle implements Shape {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    AbstractEllipseOrRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    @Override
    public double getDistanceTo(int x, int y) {
        int centerX = this.x + (width / 2);
        int centerY = this.y + (height / 2);
        return (centerX - x)*(centerX - x) + (centerY - y)*(centerY - y);
    }
}
