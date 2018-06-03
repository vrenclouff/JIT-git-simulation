package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.FileManager;
import de.oth.ajp.jit.core.Option;

public class Remove implements Option {

    private final String file;

    public Remove(String file) {
        this.file = file;
    }

    @Override
    public void runProcess() {
        FileManager.editStagingFile(staging -> staging.remove(file), this::printError);
    }

    private void printError() {
        // TODO nepodarilo se nacist soubor
    }
}
