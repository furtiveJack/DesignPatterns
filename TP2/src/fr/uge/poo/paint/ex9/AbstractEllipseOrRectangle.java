package fr.uge.poo.paint.ex9;

abstract class AbstractEllipseOrRectangle implements Shape {
    final int x;
    final int y;
    final int width;
    final int height;

    AbstractEllipseOrRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public double getDistanceTo(int x, int y) {
        int centerX = this.x + (width / 2);
        int centerY = this.y + (height / 2);
        return (centerX - x)*(centerX - x) + (centerY - y)*(centerY - y);
    }

    @Override
    public WindowsSize getMinSize() {
        return new WindowsSize(x + width, y + height);
    }
}
