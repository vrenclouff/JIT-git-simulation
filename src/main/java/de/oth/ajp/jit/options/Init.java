package de.oth.ajp.jit.options;


import de.oth.ajp.jit.core.Option;

import java.io.IOException;

import static de.oth.ajp.jit.util.JitFiles.createMainDirectories;
import static de.oth.ajp.jit.util.Logger.print;

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
