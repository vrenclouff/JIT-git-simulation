package de.oth.ajp.jit.option;


import de.oth.ajp.jit.core.Option;

import java.io.IOException;

import static de.oth.ajp.jit.util.JitFiles.createMainDirectories;
import static de.oth.ajp.jit.util.Logger.print;

/**
 * Class makes initialization Jit process. Creates default folders for saving states.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class Init implements Option {

    @Override
    public void runProcess() {
        try {
            createMainDirectories();
            print("Initialized empty Jit repository.");
        } catch (IOException e) {
            print(e.getMessage());
        }
    }
}
