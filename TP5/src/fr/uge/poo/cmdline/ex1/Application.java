package fr.uge.poo.cmdline.ex1;

import java.nio.file.Path;
import java.util.List;

public class Application {

    static private class PaintOptions{
        private boolean legacy=false;
        private boolean bordered=true;

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

        @Override
        public String toString(){
            return "PaintOption [ bordered = "+bordered+", legacy = "+ legacy +" ]";
        }
    }

    public static void main(String[] args) {
        var options = new PaintOptions();
        String[] arguments={"-legacy","-no-borders","filename1","filename2"};
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> options.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> options.setBordered(true));
        cmdParser.addFlag("-no-borders", () -> options.setBordered(false));
        List<Path> files = cmdParser.process(arguments);
//        List<Path> files = result.stream().map(Path::of).collect(Collectors.toList());
        // this code replaces the rest of the application
        files.forEach(System.out::println);
        System.out.println(options.toString());

    }
}
