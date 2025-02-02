package fr.uge.poo.cmdline.ex2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CmdLineParser {

    private static class Option {
        private String name;
        private int nbParam;
        private int currentIndex = 0;
        private ArrayList<String> args = new ArrayList<>();
        private Consumer<List<String>> action;

        public Option(String name, int nbParam, Consumer<List<String>> action) {
            Objects.requireNonNull(name);
            assert(nbParam >= 0);
            Objects.requireNonNull(action);
            this.name = name;
            this.nbParam = nbParam;
            this.action = action;
        }

        public void setArgs(String arg) {
            if (nbParam == 0) return;
            Objects.requireNonNull(arg);
            if (currentIndex > nbParam) {
                throw new IllegalStateException("Too much parameter for option " + name);
            }
            args.add(currentIndex, arg);
            currentIndex++;
        }

        public void exec() {
            action.accept(args);
        }
    }

    private final HashMap<String, Option> registeredOptions = new HashMap<>();

    /**
     * Register an option to parse on the command line.
     * @param option - the name of the option.
     * @param runnable - the code to execute if this option is present.
     * @throws NullPointerException - if one the argument is null.
     * @throws IllegalArgumentException - if the option has already been registered.
     */
    public void addFlag(String option, Runnable runnable) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(runnable);
        if (registeredOptions.containsKey(option)) {
            throw new IllegalArgumentException("Option " + option + " already registered");
        }
        Consumer<List<String>> consumer = strings -> runnable.run();
        Option opt = new Option(option, 0, consumer);
        registeredOptions.put(option, opt);
    }

    public void addOptionWithOneParameter(String option, Consumer<String> consumer) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(consumer);
        if (registeredOptions.containsKey(option)) {
            throw new IllegalArgumentException("Option " + option + " already registered");
        }
        Consumer<List<String>> c = strings -> consumer.accept(strings.get(0));
        Option opt = new Option(option, 1, c);
        registeredOptions.put(option, opt);
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
        boolean parsingOption = false;
        Option opt = null;
        for (String argument : arguments) {
            if (parsingOption) {
                if (opt.currentIndex < opt.nbParam) {
                    if (registeredOptions.containsKey(argument)) {
                        throw new IllegalArgumentException("Missing parameter for option " + opt.name);
                    }
                    opt.setArgs(argument);
                    continue;
                }
                else {
                    opt.exec();
                    parsingOption = false;
                }
            }
            opt = registeredOptions.get(argument);
            if (opt != null) {
                if (opt.nbParam == 0) {
                    opt.exec();
                }
                else {
                    parsingOption = true;
                }
            }
            else {
                files.add(Path.of(argument));
            }
        }
        if (parsingOption) {
            if (opt.currentIndex == opt.nbParam) {
                opt.exec();
            } else {
                throw new IllegalArgumentException("Missing parameter for option " + opt.name);
            }
        }

        return files;
    }
}