package fr.uge.poo.paint.ex8;

public class Ellipse extends AbstractEllipseOrRectangle {
    Ellipse(int x, int y,  int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Ellipse{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public void draw(Canvas canvas, Canvas.Color c) {
        canvas.drawEllipse(x, y, width, height, c);
    }
}
