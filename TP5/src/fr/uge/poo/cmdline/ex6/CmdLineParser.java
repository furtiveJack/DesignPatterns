package fr.uge.poo.cmdline.ex6;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CmdLineParser {

    interface OptionsObserver {

        void onRegisteredOption(OptionsManager optionsManager, Option option);

        void onProcessedOption(OptionsManager optionsManager, Option option);

        void onFinishedProcess(OptionsManager optionsManager) throws ParseException;
    }

    static class LoggerObserver implements OptionsObserver {

        @Override
        public void onRegisteredOption(OptionsManager optionsManager, Option option) {
            System.out.println("Option " + option + " is registered");
        }

        @Override
        public void onProcessedOption(OptionsManager optionsManager, Option option) {
            System.out.println("Option " + option + " is processed");
        }

        @Override
        public void onFinishedProcess(OptionsManager optionsManager) {
            System.out.println("Process method is finished");
        }
    }

    static class MandatoryOptionObserver implements OptionsObserver {
        private final List<Option> required = new ArrayList<>();

        @Override
        public void onRegisteredOption(OptionsManager optionsManager, Option option) {
            if (option.required) {
                required.add(option);
            }
        }

        @Override
        public void onProcessedOption(OptionsManager optionsManager, Option option) {
            required.remove(option);
        }

        @Override
        public void onFinishedProcess(OptionsManager optionsManager) throws ParseException {
            if (required.size() > 0) {
                throw new ParseException("Missing required options : " + required, 0);
            }
        }
    }

    static class DocumentationOptionObserver implements OptionsObserver {
        private final HashMap<String, String> optionsDoc = new HashMap<>();

        @Override
        public void onRegisteredOption(OptionsManager optionsManager, Option option) {
            StringBuilder builder = new StringBuilder();

            if (option.aliases.isEmpty()) {
               builder.append(option.name);
            } else {
                StringJoiner joinNames = new StringJoiner(", ");
                joinNames.add(option.name);
                for (var alias : option.aliases) {
                    joinNames.add(alias);
                }
                builder.append(joinNames.toString());
            }
            builder.append("\t\t").append(option.documentation).append("\n");

            optionsDoc.put(option.name, builder.toString());
        }

        @Override
        public void onProcessedOption(OptionsManager optionsManager, Option option) {
            // pass
        }

        @Override
        public void onFinishedProcess(OptionsManager optionsManager) throws ParseException {
            // pass
        }

        public String usage() {
            List<String> orderedNames = optionsDoc
                    .keySet()
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());
            StringBuilder builder = new StringBuilder();
            for (var opt : orderedNames) {
                builder.append(optionsDoc.get(opt));
            }
            builder.append("\n");
            return builder.toString();
        }
    }

    static class ConflictsOptionObserver implements OptionsObserver {
        private final List<String> conflictsFound = new ArrayList<>();

        @Override
        public void onRegisteredOption(OptionsManager optionsManager, Option option) {
        }

        @Override
        public void onProcessedOption(OptionsManager optionsManager, Option option) {
        }

        @Override
        public void onFinishedProcess(OptionsManager optionsManager) throws ParseException {
            Collection<Option> options = optionsManager.byName.values();
            for (var opt : options) {
                for (var possibleConflict : options) {
                    if (opt.equals(possibleConflict)) continue;
                    if (opt.conflictList.contains(possibleConflict.name)) {
                        conflictsFound.add("Option " + possibleConflict.name
                                + " is in conflict with " + opt.name);
                    }
                    for (var alias : possibleConflict.aliases) {
                        if (opt.conflictList.contains(alias)) {
                            conflictsFound.add("Option " + possibleConflict.name
                                    + " is in conflict with " + opt.name);
                        }
                    }
                }
            }
            if (! conflictsFound.isEmpty()) {
                throw new ParseException(String.join("\n", conflictsFound), 0);
            }
        }
    }

    private static class OptionsManager {
        private final HashMap<String, Option> byName = new HashMap<>();
        private final List<OptionsObserver> observers = new ArrayList<>();

        OptionsManager(DocumentationOptionObserver docObs) {
            registerObserver(new LoggerObserver());
            registerObserver(new MandatoryOptionObserver());
            registerObserver(new ConflictsOptionObserver());
            registerObserver(docObs);
        }

        /**
         * Register the option with all its possible names
         *
         * @param option
         */
        void register(Option option) {
            register(option.name, option);
            for (var alias : option.aliases) {
                register(alias, option);
            }
        }

        private void register(String name, Option option) {
            if (byName.containsKey(name)) {
                throw new IllegalStateException("Option " + name + " is already registered.");
            }
            byName.put(name, option);
            notifyOptionRegistered(option);
        }

        /**
         * This method is called to signal that an option is encountered during
         * a command line process
         *
         * @param optionName
         * @return the corresponding object option if it exists
         */

        Optional<Option> processOption(String optionName) {
            var opt = Optional.ofNullable(byName.get(optionName));
            opt.ifPresent(this::notifyOptionProcessed);
            return opt;
        }

        /**
         * This method is called to signal the method process of the CmdLineParser is finished
         */
        void finishProcess() throws ParseException {
            notifyProcessFinished();
        }

        void registerObserver(OptionsObserver obs) {
            observers.add(obs);
        }

        void unregisterObserver(OptionsObserver obs) {
            if (!observers.remove(obs)) {
                throw new IllegalStateException();
            }
        }

        void notifyOptionRegistered(Option opt) {
            for (var obs : observers) {
                obs.onRegisteredOption(this, opt);
            }
        }

        void notifyOptionProcessed(Option opt) {
            for (var obs : observers) {
                obs.onProcessedOption(this, opt);
            }
        }

        void notifyProcessFinished() throws ParseException {
            for (var obs : observers) {
                obs.onFinishedProcess(this);
            }
        }
    }

    static class Option {
        final String name;
        final int nbParam;
        final Consumer<List<String>> action;
        final ArrayList<String> args = new ArrayList<>();
        ArrayList<String> conflictList = new ArrayList<>();
        List<String> aliases = new ArrayList<>();
        String documentation;
        boolean required;
        int currentIndex = 0;

        private Option(String name, int nbParam, Consumer<List<String>> action) {
            Objects.requireNonNull(name);
            assert (nbParam >= 0);
            Objects.requireNonNull(action);
            this.name = name;
            this.nbParam = nbParam;
            this.action = action;
            this.required = false;
            this.documentation = "";
        }

        private Option(Builder builder) {
            this.name = builder.name;
            this.nbParam = builder.nbParams;
            this.action = builder.action;
            this.aliases = builder.aliases;
            this.required = builder.required;
            this.documentation = builder.documentation;
            this.conflictList = builder.conflictList;
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

        @Override
        public String toString() {
            return "Option{" +
                    "name='" + name + '\'' +
                    ", nbParam=" + nbParam +
                    ", aliases=" + aliases +
                    ", currentIndex=" + currentIndex +
                    ", required=" + required +
                    ", args=" + args +
                    ", action=" + action +
                    ", doc=" + documentation +
                    ", conflictsWith=" + conflictList +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            Objects.requireNonNull(o);
            if (!(o instanceof Option opt)) {
                return false;
            }
            return nbParam == opt.nbParam && required == opt.required && name.equals(opt.name)
                    && action.equals(opt.action);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + nbParam;
            result = 31 * result + aliases.hashCode();
            result = 31 * result + currentIndex;
            result = 31 * result + args.hashCode();
            result = 31 * result + action.hashCode();
            result = 31 * result + (required ? 1 : 0);
            return result;
        }

        public void exec() {
            action.accept(args);
        }

        static class Builder {
            private final List<String> aliases = new ArrayList<>();
            private final ArrayList<String> conflictList = new ArrayList<>();
            private String name;
            private boolean required = false;
            public String documentation;
            private int nbParams = 0;
            private Consumer<List<String>> action;

            public Builder(String name, int nbParams, Consumer<List<String>> action) {
                Objects.requireNonNull(name);
                Objects.requireNonNull(action);
                assert (nbParams >= 0);
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

            public Builder required() {
                this.required = true;
                return this;
            }

            public Builder alias(String alias) {
                Objects.requireNonNull(alias);
                aliases.add(alias);
                return this;
            }

            public Builder setDoc(String documentation) {
                Objects.requireNonNull(documentation);
                this.documentation = documentation;
                return this;
            }

            public Builder conflictWith(String optName) {
                Objects.requireNonNull(optName);
                conflictList.add(optName);
                return this;
            }

            public Option build() {
                if (name == null) {
                    throw new IllegalStateException("Option name can not be null");
                }
                if (action == null) {
                    throw new IllegalStateException("Option action can not be null");
                }
                if (documentation == null) {
                    documentation = "";
                }
                return new Option(this);
            }
        }

    }

    private final DocumentationOptionObserver docObserver = new DocumentationOptionObserver();

    private final OptionsManager optionsManager = new OptionsManager(docObserver);

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

    public String usage() {
        return docObserver.usage();
    }

    /**
     * Register an option to parse on the command line.
     *
     * @param option   - the name of the option.
     * @param runnable - the code to execute if this option is present.
     * @throws NullPointerException     - if one the argument is null.
     * @throws IllegalArgumentException - if the option has already been registered.
     */
    public void addFlag(String option, Runnable runnable) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(runnable);
        Option opt = new Option(option, 0, strings -> runnable.run());
        optionsManager.register(opt);
    }

    public void addOptionWithOneParameter(String option, Consumer<String> consumer) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(consumer);
        Option opt = new Option(option, 1, strings -> consumer.accept(strings.get(0)));
        optionsManager.register(opt);
    }

    public void addOption(Option opt) {
        Objects.requireNonNull(opt);
        optionsManager.register(opt);
    }

    /**
     * Process the received options. Execute the registered code for each registered option, and returns
     * a list of path representing the arguments that were not options (ie. files).
     *
     * @param arguments - an array of arguments
     * @return - a list of path representing the arguments that were not options.
     * @throws NullPointerException     if the arguments array is null
     * @throws IllegalArgumentException if an option miss parameters
     */
    public List<Path> process(String[] arguments) throws ParseException {
        ArrayList<Path> files = new ArrayList<>();
        Option opt;
        Option current = null;
        for (String argument : arguments) {
            var _opt = optionsManager.processOption(argument);
            if (_opt.isPresent()) {
                opt = _opt.get();
                if (opt.nbParam == 0) {
                    opt.exec();
                } else {
                    current = opt;
                }
            } else {
                if (current == null) {
                    files.add(Path.of(argument));
                } else {
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
        optionsManager.finishProcess();
        return files;
    }
}