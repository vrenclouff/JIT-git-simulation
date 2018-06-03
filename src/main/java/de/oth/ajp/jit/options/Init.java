package de.oth.ajp.jit.options;


import de.oth.ajp.jit.core.FileManager;
import de.oth.ajp.jit.core.Option;

import java.io.IOException;

public class Init implements Option {

    @Override
    public void runProcess() {
        try {
            FileManager.createMainFolder();
        } catch (IOException e) {
            printError();
        }
    }

    private void printError() {

    }
}
