package fr.uge.poo.cmdline.ex3;

import java.nio.file.Path;
import java.util.List;

public class Application {


    public static void main(String[] args) {
        var options = new PaintSettings.PaintSettingsBuilder();

        String[] arguments = {"-legacy", "filename3", "-no-borders", "-window-name", "WINDOW", "-border-width", "15",
                "filename1", "filename2", "-min-size", "600", "700"};
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> options.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> options.setBordered(true));
        cmdParser.addFlag("-no-borders", () -> options.setBordered(false));
        cmdParser.registerOptionWithOneParameter("-border-width", x -> options.setBorderWidth(Integer.parseInt(x)));
        cmdParser.registerOptionWithOneParameter("-window-name", options::setWindowName);
//        cmdParser.registerOptionWithParameters("-min-size", 2, (l) -> options.setMinSize(Integer.parseInt(l.get(0)), Integer.parseInt(l.get(1))));

        List<Path> files = cmdParser.process(arguments);
        // this code replaces the rest of the application
        files.forEach(System.out::println);
        System.out.println(options.build().toString());

    }
}
