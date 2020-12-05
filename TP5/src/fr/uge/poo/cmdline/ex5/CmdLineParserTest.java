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
    public void optionShouldWorkWithOneParameters() {
        var parser = new CmdLineParser();
        final String[] ran = new String[1];
        parser.addOptionOneParameter("-opt", arg -> ran[0] = arg);
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
        parser.addOptionOneParameter("-opt", arg -> ran[0] = arg);
        String arg = "optionArgument";
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[]{"-opt"}));
        assertNotEquals(ran[0], arg);
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt", "-opt2"}));
        assertNotEquals(ran[0], arg);
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