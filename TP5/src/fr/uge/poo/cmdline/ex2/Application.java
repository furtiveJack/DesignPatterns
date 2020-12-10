package fr.uge.poo.cmdline.ex2;

import java.nio.file.Path;
import java.util.List;

public class Application {

    static private class PaintSettings {
        private boolean legacy=false;
        private boolean bordered=true;
        private int borderWith = 0;
        private String windowName;


        public void setLegacy(boolean legacy) {
            this.legacy = legacy;
        }

        public boolean isLegacy(){
            return legacy;
        }

        public void setBordered(boolean bordered){
            this.bordered=bordered;
        }

        public boolean isBordered(){
            return bordered;
        }

        public void setBorderWith(String size) {
            borderWith = Integer.parseInt(size);
        }

        public void setWindowName(String name) {
            windowName = name;
        }

        @Override
        public String toString(){
            return "PaintOption [ bordered = "+bordered+", legacy = "+ legacy
                    + " borderWidth = " + borderWith + " windowName = " + windowName+" ]";
        }
    }

    public static void main(String[] args) {
        var options = new PaintSettings();
        String[] arguments={"-legacy","filename3", "-no-borders", "-window-name", "WINDOW", "-border-width", "15", "filename1","filename2"};
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> options.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> options.setBordered(true));
        cmdParser.addFlag("-no-borders", () -> options.setBordered(false));
        cmdParser.addOptionWithOneParameter("-border-width", options::setBorderWith);
        cmdParser.addOptionWithOneParameter("-window-name", options::setWindowName);
        List<Path> files = cmdParser.process(arguments);
//        List<Path> files = result.stream().map(Path::of).collect(Collectors.toList());
        // this code replaces the rest of the application
        files.forEach(System.out::println);
        System.out.println(options.toString());

    }
}
