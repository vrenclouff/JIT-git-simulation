package de.oth.ajp.jit.options;

import de.oth.ajp.jit.util.Logger;
import de.oth.ajp.jit.core.Option;

import static de.oth.ajp.jit.util.JitFiles.readCommits;


public class Log implements Option {

    @Override
    public void runProcess() {
        readCommits().forEach(Logger::print);
    }
}
