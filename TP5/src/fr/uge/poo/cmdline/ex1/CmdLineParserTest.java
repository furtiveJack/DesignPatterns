package fr.uge.poo.cmdline.ex1;

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
        final boolean[] runned = new boolean[1];
        parser.addFlag("-opt", () -> runned[0] = Boolean.TRUE);
        var res = parser.process(new String[]{"-opt"});
        assertFalse(res.contains(Path.of("-opt")));
        assertTrue(runned[0]);
    }

    @Test
    public void registerShouldWorkWithFile() {
        var parser = new CmdLineParser();
        var res = parser.process(new String[]{"file1"});
        assertTrue(res.contains(Path.of("file1")));
    }
}