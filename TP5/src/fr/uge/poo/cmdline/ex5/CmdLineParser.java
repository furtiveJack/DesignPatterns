package fr.uge.poo.cmdline.ex5;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CmdLineParser {

    private static class Option {
        private final String name;
        private final int nbParam;
        private List<String> aliases;
        private int currentIndex = 0;
        private final ArrayList<String> args = new ArrayList<>();
        private final Consumer<List<String>> action;

        private Option(String name, int nbParam, Consumer<List<String>> action) {
            Objects.requireNonNull(name);
            assert(nbParam >= 0);
            Objects.requireNonNull(action);
            this.name = name;
            this.nbParam = nbParam;
            this.action = action;
        }

        private Option(Builder builder) {
            this.name = builder.name;
            this.nbParam = builder.nbParams;
            this.action = builder.action;
            this.aliases = builder.aliases;
        }

        public void setArg(String arg) {
            if (nbParam == 0) return;
            Objects.requireNonNull(arg);
            if (arg.startsWith("-")) {
                throw new IllegalArgumentException("Missing parameter for option " + name);
            }
            if (currentIndex > nbParam) {
                throw new IllegalStateException("Too much parameter for option " + name);
            }
            args.add(currentIndex, arg);
            currentIndex++;
        }

        public void exec() {
            action.accept(args);
        }

        static class Builder {
            private String name;
            private final List<String> aliases = new ArrayList<>();
            private int nbParams = 0;
            private Consumer<List<String>> action;

            public Builder(String name, int nbParams, Consumer<List<String>> action) {
                Objects.requireNonNull(name);
                Objects.requireNonNull(action);
                assert(nbParams >= 0);
                this.name = name;
                this.nbParams = nbParams;
                this.action = action;
            }

            public Builder setName(String name) {
                Objects.requireNonNull(name);
                this.name = name;
                return this;
            }

            public Builder setNbParameters(int nbParams) {
                assert (nbParams >= 0);
                this.nbParams = nbParams;
                return this;
            }
            public Builder setAction(Consumer<List<String>> action) {
                Objects.requireNonNull(action);
                this.action = action;
                return this;
            }

            public Builder setAlias(String alias) {
                Objects.requireNonNull(alias);
                aliases.add(alias);
                return this;
            }

            public Option build() {
                if (name == null) {
                    throw new IllegalStateException("Option name can not be null");
                }
                if (action == null) {
                    throw new IllegalStateException("Option action can not be null");
                }
                return new Option(this);
            }
        }
    }

    static Option.Builder intOptionBuilder(String name, Consumer<Integer> action) {
        return new Option.Builder(name, 1, l -> {
            if (l == null || l.size() != 1) {
                throw new IllegalArgumentException("Illegal number of arguments for option " + name);
            }
            int arg = Integer.parseInt(l.get(0));
            action.accept(arg);
        });
    }

    static Option.Builder twoIntOptionBuilder(String name, BiConsumer<Integer, Integer> action) {
        return new Option.Builder(name, 2, l -> {
            if (l == null || l.size() != 2) {
                throw new IllegalArgumentException("Illegal number of arguments for option " + name);
            }
            int arg1 = Integer.parseInt(l.get(0));
            int arg2 = Integer.parseInt(l.get(1));
            action.accept(arg1, arg2);
        });
    }

    static Option.Builder inetSocketOptionBuilder(String name, Consumer<InetSocketAddress> action) {
        return new Option.Builder(name, 2, l -> {
           if (l == null || l.size() != 2) {
               throw new IllegalArgumentException("Illegal number of arguments for option " + name);
           }
           var inet = new InetSocketAddress(l.get(0), Integer.parseInt(l.get(1)));
           action.accept(inet);
        });
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
        Option opt = new Option(option, 0, strings -> runnable.run());
        registeredOptions.put(option, opt);
    }

    public void addOptionOneParameter(String option, Consumer<String> consumer) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(consumer);
        if (registeredOptions.containsKey(option)) {
            throw new IllegalArgumentException("Option " + option + " already registered");
        }
        Option opt = new Option(option, 1, strings -> consumer.accept(strings.get(0)));
        registeredOptions.put(option, opt);
    }

    public void addOptionMultipleParameters(String option, int argCount, Consumer<List<String>> consumer) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(consumer);
        if (registeredOptions.containsKey(option)) {
            throw new IllegalArgumentException("Option " + option + " already registered");
        }
        Option opt = new Option(option, argCount, consumer);
        registeredOptions.put(option, opt);
    }

    public void addOption(Option opt) {
        Objects.requireNonNull(opt);
        if (registeredOptions.containsKey(opt.name)) {
            throw new IllegalArgumentException("Option " + opt.name + " already registered");
        }
        registeredOptions.put(opt.name, opt);
        opt.aliases.forEach(alias -> registeredOptions.put(alias, opt));
    }

    /**
     * Process the received options. Execute the registered code for each registered option, and returns
     * a list of path representing the arguments that were not options (ie. files).
     * @param arguments - an array of arguments
     * @return - a list of path representing the arguments that were not options.
     * @throws NullPointerException if the arguments array is null
     * @throws IllegalArgumentException if an option miss parameters
     */
    public List<Path> process(String[] arguments) {
        ArrayList<Path> files = new ArrayList<>();
        Option opt;
        Option current = null;
        for (String argument : arguments) {
            opt = registeredOptions.get(argument);
            if (opt != null) {
                if (opt.nbParam == 0) {
                    opt.exec();
                }
                else {
                    current = opt;
                }
            }
            else {
                if (current == null) {
                    files.add(Path.of(argument));
                }
                else {
                    current.setArg(argument);
                    if (current.currentIndex == current.nbParam) {
                        current.exec();
                        current = null;
                    }
                }
            }
        }
        if (current != null) {
            throw new IllegalArgumentException("Missing parameter for option " + current.name);
        }
        return files;
    }
}