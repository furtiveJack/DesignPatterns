package fr.uge.poo.cmdline.ex3;

public class PaintOptions {
    private boolean legacy;
    private boolean bordered;
    private int borderWidth;
    private final String windowName;
    private int minSizeX;
    private int minSizeY;

    private PaintOptions(String name, boolean legacy, boolean bordered, int borderWidth, int minSizeX, int minSizeY) {
        this.windowName = name;
        this.legacy = legacy;
        this.bordered = bordered;
        this.borderWidth = borderWidth;
        this.minSizeX = minSizeX;
        this.minSizeY = minSizeY;
    }

    @Override
    public String toString(){
        return "PaintOptions [ bordered = "+bordered+", legacy = "+ legacy
                + " borderWidth = " + borderWidth + " windowName = " + windowName+" ]";
    }

    public static class PaintOptionsBuilder {
        private boolean legacy = false;
        private boolean bordered = false;
        private int borderWidth = 0;
        private String windowName;
        private int minSizeX = 500;
        private int minSizeY = 500;

        public PaintOptionsBuilder setLegacy(boolean legacy) {
            this.legacy = legacy;
            return this;
        }

        public PaintOptionsBuilder setBordered(boolean bordered) {
            this.bordered = bordered;
            return this;
        }

        public PaintOptionsBuilder setBorderWidth(int width) {
            this.borderWidth = width;
            return this;
        }

        public PaintOptionsBuilder setWindowName(String name) {
            this.windowName = name;
            return this;
        }

        public PaintOptionsBuilder setMinSize(int minX, int minY) {
            this.minSizeX = minX;
            this.minSizeY = minY;
            return this;
        }

        public PaintOptions build() {
            if (windowName == null) {
                throw new IllegalStateException("Window name can not be null");
            }
            return new PaintOptions(windowName, legacy, bordered, borderWidth, minSizeX, minSizeY);
        }

    }
}
