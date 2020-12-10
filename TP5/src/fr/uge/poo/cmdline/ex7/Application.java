package fr.uge.poo.cmdline.ex7;

import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Application {


    public static void main(String[] args) throws ParseException {
        var options = new PaintSettings.Builder();

        String[] arguments = {
                "-legacy", "filename3", "-window-name", "WINDOW", "-no-borders",
                "-min-size", "600", "700", "-bw", "15", "-server", "localhost", "8080",
                "filename1", "filename2"
        };
        var cmdParser = new CmdLineParser();
        cmdParser.addFlag("-legacy", () -> options.setLegacy(true));
        cmdParser.addFlag("-with-borders", () -> options.setBordered(true));
//        cmdParser.addFlag("-no-borders", () -> options.setBordered(false));
//        cmdParser.addOptionOneParameter("-border-width", x -> options.setBorderWidth(Integer.parseInt(x)));
        cmdParser.addOptionWithOneParameter("-window-name", options::setWindowName);
        cmdParser.addOption(CmdLineParser.twoIntOptionBuilder("-min-size", options::setMinSize).build());
        cmdParser.addOption(CmdLineParser.intOptionBuilder("-border-width", options::setBorderWidth)
                .alias("-bw")
                .setDoc("Specifies the border width")
                .build());

        cmdParser.addOption(CmdLineParser.inetSocketOptionBuilder("-remote-server", options::setRemoteServer)
                .alias("-rs")
                .alias("-server")
                .build());
        List<Path> files = cmdParser.process(arguments);
        // this code replaces the rest of the application
        files.forEach(System.out::println);
        System.out.println(options.build().toString());
        System.out.println(cmdParser.usage());

    }
}
