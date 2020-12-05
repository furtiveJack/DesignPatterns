package fr.uge.poo.cmdline.ex7;

import fr.uge.poo.cmdline.ex7.CmdLineParser.Option.Builder;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("static-method")
class CmdLineParserTest {

    @Test
    public void processShouldFailFastOnNullArgument() {
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.process(null));
    }

    @Test
    public void registerShouldFailFastOnNullArgument() {
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.addFlag(null, () -> {
        }));
        assertThrows(NullPointerException.class, () -> parser.addFlag("test", null));
        assertThrows(NullPointerException.class, () -> parser.addFlag(null, null));
    }

    @Test
    public void registerShouldFailFastOnDuplicateArg() {
        var parser = new CmdLineParser();
        parser.addFlag("test", () -> {
        });
        assertThrows(IllegalStateException.class, () -> parser.addFlag("test", () -> {
        }));
    }

    @Test
    public void registerShouldWorkWithOption() throws ParseException {
        var parser = new CmdLineParser();
        final boolean[] ran = new boolean[1];
        parser.addFlag("-opt", () -> ran[0] = Boolean.TRUE);
        var res = parser.process(new String[]{"-opt"});
        assertFalse(res.contains(Path.of("-opt")));
        assertTrue(ran[0]);
    }

    @Test
    public void registerShouldWorkWithFile() throws ParseException {
        var parser = new CmdLineParser();
        var res = parser.process(new String[]{"file1"});
        assertTrue(res.contains(Path.of("file1")));
    }

    @Test
    public void optionShouldWorkWithOneParameters() throws ParseException {
        var parser = new CmdLineParser();
        final String[] ran = new String[1];
        parser.addOptionWithOneParameter("-opt", arg -> ran[0] = arg);
        String arg = "optionArgument";
        var res = parser.process(new String[]{"-opt", arg});
        assertFalse(res.contains(Path.of("-opt")));
        assertFalse(res.contains(Path.of(arg)));
        assertEquals(ran[0], arg);
    }

    @Test
    public void optionOneParamShouldFailWithMissingParameter() {
        var parser = new CmdLineParser();
        final String[] ran = new String[1];
        parser.addOptionWithOneParameter("-opt", arg -> ran[0] = arg);
        String arg = "optionArgument";
        assertThrows(ParseException.class, () -> parser.process(new String[]{"-opt"}));
        assertNotEquals(ran[0], arg);
        assertThrows(ParseException.class, () -> parser.process(new String[]{"-opt", "-opt2"}));
        assertNotEquals(ran[0], arg);
    }

    @Test
    public void optionShouldWorkWithMultipleParameter() throws ParseException {
        var parser = new CmdLineParser();
        final String[] ran = new String[2];
        parser.addOption(new Builder("-opt", 2, l -> {
            ran[0] = l.get(0);
            ran[1] = l.get(1);
        }).build());
        var res = parser.process(new String[]{"-opt", "arg1", "arg2"});
        assertFalse(res.contains(Path.of("arg1")));
        assertFalse(res.contains(Path.of("arg2")));
        assertEquals(ran[0], "arg1");
        assertEquals(ran[1], "arg2");
    }

    @Test
    public void optionMultipleParamShouldFailWithMissingParam() {
        var parser = new CmdLineParser();
        parser.addOption(new Builder("-opt", 3, l -> {
        }).build());
        assertThrows(ParseException.class, () -> parser.process(new String[]{"-opt"}));
        assertThrows(ParseException.class, () -> parser.process(new String[]{"-opt", "arg1"}));
        assertThrows(ParseException.class, () -> parser.process(new String[]{"-opt", "-opt2"}));
        assertThrows(ParseException.class, () -> parser.process(new String[]{"-opt", "-opt2", "arg1"}));
    }

    @Test
    public void processRequiredOption() {
        var cmdParser = new CmdLineParser();
        var option = new Builder("-test", 0, l -> {
        }).alias("-t").required().build();
        cmdParser.addOption(option);
        cmdParser.addFlag("-test1", () -> {
        });
        String[] arguments = {"-test1", "a", "b"};
        assertThrows(ParseException.class, () -> {
            cmdParser.process(arguments);
        });
        String[] arguments2 = {"-t", "a", "b"};
        assertThrows(ParseException.class, () -> {
            cmdParser.process(arguments2);
        });
    }

    @Test
    public void processConflicts() {
        var cmdParser = new CmdLineParser();
        var option= new Builder("-test",0, l->{}).conflictWith("-test1").build();
        cmdParser.addOption(option);
        var option2= new Builder("-test1",0, l->{}).build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test","-test1"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processConflicts2() {
        var cmdParser = new CmdLineParser();
        var option= new Builder("-test",0, l->{}).conflictWith("-test1").build();
        cmdParser.addOption(option);
        var option2= new Builder("-test1",0, l->{}).build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test1","-test"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processConflictsAndAliases() {
        var cmdParser = new CmdLineParser();
        var option= new Builder("-test",0, l->{}).conflictWith("-test2").build();
        cmdParser.addOption(option);
        var option2= new Builder("-test1",0, l->{}).alias("-test2").build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test1","-test"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processConflictsAndAliases2() {
        var cmdParser = new CmdLineParser();
        var option= new Builder("-test",0, l->{}).conflictWith("-test2").build();
        cmdParser.addOption(option);
        var option2= new Builder("-test1",0, l->{}).alias("-test2").build();
        cmdParser.addOption(option2);
        String[] arguments = {"-test","-test1"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void wrongArgument() {
        var hosts = new ArrayList();
        var cmdParser = new CmdLineParser();
        var optionHosts = new Builder("-hosts",2, hosts::addAll).build();
        cmdParser.addOption(optionHosts);
        cmdParser.addFlag("-legacy",()->{});

        String[] arguments = {"-hosts","localhost","-legacy","file"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments);});
    }

    @Test
    public void processPolicyStandard() {
        var hosts = new ArrayList<String>();
        var cmdParser = new CmdLineParser();
        var optionHosts= new Builder("-hosts",2, hosts::addAll).build();
        cmdParser.addOption(optionHosts);
        cmdParser.addFlag("-legacy",()->{});
        String[] arguments = {"-hosts","localhost","-legacy","file"};
        assertThrows(ParseException.class,()->{cmdParser.process(arguments,CmdLineParser.STANDARD);});
    }

    @Test
    public void processPolicyRelaxed() {
        var hosts = new ArrayList<String>();
        var cmdParser = new CmdLineParser();
        var optionHosts= new Builder("-hosts",2, hosts::addAll).build();
        cmdParser.addOption(optionHosts);
        cmdParser.addFlag("-legacy",()->{});
        String[] arguments = {"-hosts","localhost","-legacy","file"};
        assertDoesNotThrow(() -> cmdParser.process(arguments,CmdLineParser.RELAXED));
        assertEquals(1,hosts.size());
        assertEquals("localhost",hosts.get(0));
    }



    @Test
    public void processPolicyOldSchool() {
        var hosts = new ArrayList<String>();
        var cmdParser = new CmdLineParser();
        var optionHosts= new Builder("-hosts",2, hosts::addAll).build();
        cmdParser.addOption(optionHosts);
        cmdParser.addFlag("-legacy",()->{});
        String[] arguments = {"-hosts","localhost","-legacy","file"};
        assertDoesNotThrow(() -> cmdParser.process(arguments,CmdLineParser.OLDSCHOOL));
        assertEquals(2,hosts.size());
        assertEquals("localhost",hosts.get(0));
        assertEquals("-legacy",hosts.get(1));
    }
}