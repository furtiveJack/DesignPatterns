package fr.uge.poo.cmdline.ex2;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

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
    public void addFlagShouldFailFastOnDuplicateOption() {
        var parser = new CmdLineParser();
        parser.addFlag("test", () -> {
        });
        assertThrows(IllegalArgumentException.class, () -> parser.addFlag("test", () -> {
        }));
    }

    @Test
    public void processShouldWorkWithOption() {
        var parser = new CmdLineParser();
        final boolean[] runned = new boolean[1];
        parser.addFlag("-opt", () -> runned[0] = Boolean.TRUE);
        var res = parser.process(new String[]{"-opt"});
        assertFalse(res.contains(Path.of("-opt")));
        assertTrue(runned[0]);
    }

    @Test
    public void processShouldWorkWithFile() {
        var parser = new CmdLineParser();
        var res = parser.process(new String[]{"file1"});
        assertTrue(res.contains(Path.of("file1")));
    }

    @Test
    public void addOption1ShouldFailOnDuplicateOption() {
        var parser = new CmdLineParser();
        parser.addFlag("test", () -> {
        });
        assertThrows(IllegalArgumentException.class, () -> parser.addOptionWithOneParameter("test", (s) -> {
        }));
    }

    @Test
    public void processShouldWorkWithOptionParam() {
        var parser = new CmdLineParser();
        final String[] runned = new String[1];
        parser.addOptionWithOneParameter("-opt", (s) -> runned[0] = s);
        var res = parser.process(new String[]{"-opt", "executed"});
        assertFalse(res.contains(Path.of("-opt")));
        assertEquals("executed", runned[0]);
    }

    @Test
    public void processShouldFailOnMissingParam() {
        var parser = new CmdLineParser();
        final String[] runned = new String[1];
        parser.addOptionWithOneParameter("-opt", (s) -> runned[0] = s);
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt"}));
    }

    @Test
    public void processShouldFailOnMissingParam2() {
        var parser = new CmdLineParser();
        final String[] runned = new String[1];
        parser.addOptionWithOneParameter("-opt", (s) -> runned[0] = s);
        parser.addFlag("-test", () -> {});
        assertThrows(IllegalArgumentException.class, () -> parser.process(new String[] {"-opt", "-test"}));
    }
}