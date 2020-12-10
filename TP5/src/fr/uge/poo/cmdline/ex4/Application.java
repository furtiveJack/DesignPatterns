package fr.uge.poo.cmdline.ex4;

import java.nio.file.Path;
import java.util.List;

public class Application {


    public static void main(String[] args) {
        var options = new PaintSettings.Builder();

        String[] arguments = {
                "-legacy", "filename3", "-window-name", "WINDOW", "-no-borders",
                "-min-size", "600", "700", "-border-width", "15",
                "filename1", "filename2"
        };
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> options.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> options.setBordered(true));
//        cmdParser.addFlag("-no-borders", () -> options.setBordered(false));
        cmdParser.addOptionWithOneParameter("-border-width", x -> options.setBorderWidth(Integer.parseInt(x)));
        cmdParser.addOptionWithOneParameter("-window-name", options::setWindowName);
        cmdParser.addOptionMultipleParameters("-min-size", 2, (l) -> options.setMinSize(Integer.parseInt(l.get(0)), Integer.parseInt(l.get(1))));
        List<Path> files = cmdParser.process(arguments);
        // this code replaces the rest of the application
        files.forEach(System.out::println);
        System.out.println(options.build().toString());

    }
}
