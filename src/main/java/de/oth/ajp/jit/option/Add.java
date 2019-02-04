package de.oth.ajp.jit.option;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.util.JitFiles;
import de.oth.ajp.jit.util.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static de.oth.ajp.jit.util.JitFiles.modifyStaging;
import static de.oth.ajp.jit.util.JitFiles.unchangedFilePaths;
import static de.oth.ajp.jit.util.JitFiles.filesWalk;
import static de.oth.ajp.jit.util.Logger.print;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

/**
 * Class added new file to staging.
 * 1. at first are loaded all unchanged files
 * 2. created list of paths which could be added to staging
 * 3. removed files which are unchanged
 * 4. added new or changed files to staging
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class Add implements Option {

    private final String file;

    public Add(String file) {
        this.file = file;
    }

    @Override
    public void runProcess() {

        Path filePath = Paths.get(file);
        if (Files.notExists(filePath) || !Files.isRegularFile(filePath)) {
            print("fatal: pathspec '%s' did not match any files", file);
            return;
        }

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
            print(e.getMessage());
        }
    }

    private void printIgnoredFiles(List<String> messages) {

        if (!messages.isEmpty()) {
            print("The following paths are ignored by one of your .jitignore files:");
        }
        messages.forEach(Logger::print);
    }
}
