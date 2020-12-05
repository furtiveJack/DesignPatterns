package fr.uge.poo.paint.ex9;

public class Square implements Shape {
    private Rectangle square;

    public Square(int x, int y, int width) {
        square = new Rectangle(x, y, width, width);
    }

    @Override
    public void draw(Canvas canvas, Canvas.Color c) {
        square.draw(canvas, c);
    }

    public String toString() {
        return "Sqaure{" +
                "x=" + square.x +
                ", y=" + square.y +
                ", width" + square.width +
                ", height=" + square.height +
                '}';
    }
    @Override
    public double getDistanceTo(int x, int y) {
        return square.getDistanceTo(x, y);
    }

    @Override
    public WindowsSize getMinSize() {
        return square.getMinSize();
    }
}
