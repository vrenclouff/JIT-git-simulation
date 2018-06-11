package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.Option;

import java.io.IOException;

import static de.oth.ajp.jit.util.JitFiles.modifyStaging;

public class Remove implements Option {

    private final String file;

    public Remove(String file) {
        this.file = file;
    }

    @Override
    public void runProcess() {
        try {
            modifyStaging(st -> st.remove(file));
        }catch (IOException e) {
            printError(e.getMessage());
        }
    }

    private void printError(String message) {
        // TODO nepodarilo se nacist soubor
    }
}
