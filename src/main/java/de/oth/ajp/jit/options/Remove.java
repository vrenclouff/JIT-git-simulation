package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.util.Logger;

import java.io.IOException;

import static de.oth.ajp.jit.util.JitFiles.modifyStaging;

/**
 * Class remove file from staging.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class Remove implements Option {

    private final String file;

    public Remove(String file) {
        this.file = file;
    }

    @Override
    public void runProcess() {
        modifyStaging(st -> st.remove(file), Logger::print);
    }
}
