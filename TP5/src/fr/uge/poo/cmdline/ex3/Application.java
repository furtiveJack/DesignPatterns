package fr.uge.poo.cmdline.ex3;

import java.nio.file.Path;
import java.util.List;

public class Application {


    public static void main(String[] args) {
        var options = new PaintOptions.PaintOptionsBuilder();

        String[] arguments = {"-legacy", "filename3", "-no-borders", "-window-name", "WINDOW", "-border-width", "15",
                "filename1", "filename2", "-min-size", "600", "700"};
        var cmdParser = new CmdLineParser();
        cmdParser.registerOption("-legacy", () -> options.setLegacy(true));
        cmdParser.registerOption("-with-borders", () -> options.setBordered(true));
        cmdParser.registerOption("-no-borders", () -> options.setBordered(false));
        cmdParser.registerOptionWithParameter("-border-width", x -> options.setBorderWidth(Integer.parseInt(x)));
        cmdParser.registerOptionWithParameter("-window-name", options::setWindowName);
//        cmdParser.registerOptionWithParameters("-min-size", 2, (l) -> options.setMinSize(Integer.parseInt(l.get(0)), Integer.parseInt(l.get(1))));

        List<Path> files = cmdParser.process(arguments);
        // this code replaces the rest of the application
        files.forEach(System.out::println);
        System.out.println(options.build().toString());

    }
}
