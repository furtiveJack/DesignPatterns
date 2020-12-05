package fr.uge.poo.cmdline.ex7;

import java.net.InetSocketAddress;

public class PaintSettings {
    private boolean legacy;
    private boolean bordered;
    private int borderWidth;
    private final String windowName;
    private int minSizeX;
    private int minSizeY;
    private InetSocketAddress addr;


    private PaintSettings(Builder builder) {
        this.windowName = builder.windowName;
        this.legacy = builder.legacy;
        this.bordered = builder.bordered;
        this.borderWidth = builder.borderWidth;
        this.minSizeX = builder.minSizeX;
        this.minSizeY = builder.minSizeY;
        this.addr = builder.addr;
    }

    @Override
    public String toString(){
        return "PaintOptions [ " +
                "bordered = "+bordered+", " +
                "legacy = "+ legacy+", " +
                "borderWidth = " + borderWidth +", " +
                "minSize = (" + minSizeX + ", " + minSizeY + "), " +
                "remoteServer = " + addr.toString() + ", " +
                "windowName = " + windowName+" ]";
    }

    public static class Builder {
        private boolean legacy = false;
        private boolean bordered = false;
        private int borderWidth = 0;
        private String windowName;
        private int minSizeX = 500;
        private int minSizeY = 500;
        private InetSocketAddress addr;

        public Builder setLegacy(boolean legacy) {
            this.legacy = legacy;
            return this;
        }

        public Builder setBordered(boolean bordered) {
            this.bordered = bordered;
            return this;
        }

        public Builder setBorderWidth(int width) {
            this.borderWidth = width;
            return this;
        }

        public Builder setWindowName(String name) {
            this.windowName = name;
            return this;
        }

        public Builder setMinSize(int minX, int minY) {
            this.minSizeX = minX;
            this.minSizeY = minY;
            return this;
        }

        public Builder setRemoteServer(InetSocketAddress addr) {
            this.addr = addr;
            return this;
        }

        public PaintSettings build() {
            if (windowName == null) {
                throw new IllegalStateException("Window name can not be null");
            }
            return new PaintSettings(this);
        }

    }
}
