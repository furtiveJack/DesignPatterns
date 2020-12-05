package fr.uge.poo.cmdline.ex0;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


@SuppressWarnings("static-method")
class CmdLineParserTest {

    @Test
    public void processShouldFailFastOnNullArgument(){
        var parser = new CmdLineParser();
        assertThrows(NullPointerException.class, () -> parser.process(null));
    }

}