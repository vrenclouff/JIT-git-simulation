package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.FileManager;
import de.oth.ajp.jit.core.Option;

import java.util.List;


public class Add implements Option {

    private final String file;

    public Add(String file) {
        this.file = file;
    }

    @Override
    public void runProcess() {
        List<String> files = file.equals(".") ? FileManager.loadAllFiles() : List.of(file);
        FileManager.editStagingFile(staging -> files.forEach(staging::add), this::printError);
    }

    private void printError() {
        // TODO nepodarilo se nacist soubor
    }
}
