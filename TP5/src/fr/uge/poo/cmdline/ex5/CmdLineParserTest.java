package fr.uge.poo.cmdline.ex5;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("static-method")
class CmdLineParserTest {

    @Test
    public void processShouldFailFastOnNullArgument(){
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.process(null));
    }

    @Test
    public void registerShouldFailFastOnNullArgument() {
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.addFlag(null, () -> {}));
        assertThrows(NullPointerException.class, () -> parser.addFlag("test", null));
        assertThrows(NullPointerException.class, () -> parser.addFlag(null, null));
    }

    @Test
    public void registerShouldFailFastOnDuplicateArg() {
        var parser = new CmdLineParser();
        parser.addFlag("test", () -> {});
        assertThrows(IllegalArgumentException.class, () -> parser.addFlag("test", () -> {}));
    }

    @Test
    public void registerShouldWorkWithOption() {
        var parser = new CmdLineParser();
        final boolean[] ran = new boolean[1];
        parser.addFlag("-opt", () -> ran[0] = Boolean.TRUE);
        var res = parser.process(new String[]{"-opt"});
        assertFalse(res.contains(Path.of("-opt")));
        assertTrue(ran[0]);
    }

    @Test
    public void registerShouldWorkWithFile() {
        var parser = new CmdLineParser();
        var res = parser.process(new String[]{"file1"});
        assertTrue(res.contains(Path.of("file1")));
    }


    @Test
    public void addOption1ShouldFailOnDuplicateOption() {
        var parser = new fr.uge.poo.cmdline.ex2.CmdLineParser();
        parser.addFlag("test", () -> {
        });
        assertThrows(IllegalArgumentException.class, () -> parser.addOptionWithOneParameter("test", (s) -> {
        }));
    }

    @Test
    public void processShouldWorkWithOptionParam() {
        var parser = new fr.uge.poo.cmdline.ex2.CmdLineParser();
        final String[] runned = new String[1];
        parser.addOptionWithOneParameter("-opt", (s) -> runned[0] = s);
        var res = parser.process(new String[]{"-opt", "executed"});
        assertFalse(res.contains(Path.of("-opt")));
        assertEquals("executed", runned[0]);
    }

    @Test
    public void processShouldFailOnMissingParam() {
        var parser = new fr.uge.poo.cmdline.ex2.CmdLineParser();
        final String[] runned = new String[1];
        parser.addOptionWithOneParameter("-opt", (s) -> runned[0] = s);
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt"}));
    }

    @Test
    public void processShouldFailOnMissingParam2() {
        var parser = new fr.uge.poo.cmdline.ex2.CmdLineParser();
        final String[] runned = new String[1];
        parser.addOptionWithOneParameter("-opt", (s) -> runned[0] = s);
        parser.addFlag("-test", () -> {});
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt", "-test"}));
    }

    @Test
    public void optionShouldWorkWithMultipleParameter() {
        var parser = new CmdLineParser();
        final String[] ran = new String[2];
        parser.addOptionMultipleParameters("-opt", 2, l -> { ran[0] = l.get(0); ran[1] = l.get(1);});
        var res = parser.process(new String[] {"-opt", "arg1", "arg2"});
        assertFalse(res.contains(Path.of("arg1")));
        assertFalse(res.contains(Path.of("arg2")));
        assertEquals(ran[0], "arg1");
        assertEquals(ran[1], "arg2");
    }

    @Test
    public void optionMultipleParamShouldFailWithMissingParam() {
        var parser = new CmdLineParser();
        parser.addOptionMultipleParameters("-opt", 3, l -> {});
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt"}));
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt", "arg1"}));
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt", "-opt2"}));
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt", "-opt2", "arg1"}));
    }
}