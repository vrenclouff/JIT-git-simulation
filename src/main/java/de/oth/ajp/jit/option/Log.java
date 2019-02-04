package de.oth.ajp.jit.option;

import de.oth.ajp.jit.util.Logger;
import de.oth.ajp.jit.core.Option;

import static de.oth.ajp.jit.util.JitFiles.readCommits;

/**
 * Class prints all commits.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class Log implements Option {

    @Override
    public void runProcess() {
        readCommits().forEach(Logger::print);
    }
}
