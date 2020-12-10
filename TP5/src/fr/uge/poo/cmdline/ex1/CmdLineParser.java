package fr.uge.poo.cmdline.ex1;

import java.nio.file.Path;
import java.util.*;

public class CmdLineParser {

    private final HashMap<String, Runnable> registeredOptions = new HashMap<>();

    /**
     * Register an option to parse on the command line.
     * @param option - the name of the option.
     * @param runnable - the code to execute if this option is present.
     * @throws NullPointerException - if one of the argument is null.
     * @throws IllegalArgumentException - if the option has already been registered.
     */
    public void addFlag(String option, Runnable runnable) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(runnable);
        if (registeredOptions.containsKey(option)) {
            throw new IllegalArgumentException("Option " + option + " already registered");
        }
        registeredOptions.put(option, runnable);
    }

    /**
     * Process the received options. Execute the registered code for each registered option, and returns
     * a list of path representing the arguments that were not options (ie. files).
     * @param arguments - an array of arguments
     * @return - a list of path representing the arguments that were not options.
     * @throws NullPointerException if the arguments array is null
     */
    public List<Path> process(String[] arguments) {
        ArrayList<Path> files = new ArrayList<>();
        for (String argument : arguments) {
            var opt = registeredOptions.get(argument);
            if (opt == null) {
                files.add(Path.of(argument));
            }
            else {
                opt.run();
            }
        }
        return files;
    }
}