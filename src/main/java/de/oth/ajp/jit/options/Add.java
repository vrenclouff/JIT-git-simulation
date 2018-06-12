package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.util.JitFiles;
import de.oth.ajp.jit.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.oth.ajp.jit.util.JitFiles.modifyStaging;
import static de.oth.ajp.jit.util.JitFiles.unchangedFilePaths;
import static de.oth.ajp.jit.util.JitFiles.filesWalk;
import static de.oth.ajp.jit.util.Logger.print;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;


public class Add implements Option {

    private final String file;

    public Add(String file) {
        this.file = file;
    }

    @Override
    public void runProcess() {
        try {
            List<String> unchangedPaths = unchangedFilePaths();
            List<String> ignoredPaths = new ArrayList<>();
            List<String> files = file.equals(".") ? filesWalk().collect(toList()) : new ArrayList<>(of(file));
            files.removeAll(unchangedPaths);
            modifyStaging(st -> files.forEach(file -> {
                if (JitFiles.isNotIgnored(file)) {
                    st.add(file);
                } else {
                    ignoredPaths.add(file);
                }
            }));
            printIgnoredFiles(ignoredPaths);
        }catch (IOException e) {
            printError(e.getMessage());
        }
    }

    private void printIgnoredFiles(List<String> messages) {

        if (!messages.isEmpty()) {
            print("The following paths are ignored by one of your .jitignore files:");
        }
        messages.forEach(Logger::print);
    }

    private void printError(String message) {
        // TODO nepodarilo se nacist soubor
        print("Neco se stalo");
    }
}
