package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.core.Staging;

import java.io.IOException;
import java.util.List;

import static de.oth.ajp.jit.util.JitFiles.*;
import static de.oth.ajp.jit.util.Logger.greenPrint;
import static de.oth.ajp.jit.util.Logger.print;
import static de.oth.ajp.jit.util.Logger.redPrint;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Class Status print actual state in staging file.
 * 1. At first are loaded all files which can by tracked. (files which are filtered out in .jitignore).
 * 2. loaded all files which are unchanged with commits before.
 * 3. loaded files which are in staging
 * 4. keept only files which can be tracked.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class Status implements Option {

    @Override
    public void runProcess() {

        List<String> trackedFiles = emptyList();
        List<String> allFiles = emptyList();
        List<String> unchangedFiles;

        try {
            allFiles = filesWalk().collect(toList());

            unchangedFiles = unchangedFilePaths();
            trackedFiles = readStaging().map(Staging::getTrackedFiles).orElse(emptyList());

            allFiles.removeAll(trackedFiles);
            allFiles.removeAll(unchangedFiles);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (trackedFiles.isEmpty() && allFiles.isEmpty()) {
            print("nothing to commit, working tree clean");
        } else {

            if (!trackedFiles.isEmpty()) {
                print("\nChanges to be committed:");
                print("\t(use \"jit remove <file>...\" to unstage)\n");
                trackedFiles.forEach(f -> greenPrint("\t\t" + f));
            }

            if (!allFiles.isEmpty()) {
                print("\nChanges not staged for commit:");
                print("\t(use \"jit add <file>...\" to update what will be committed)\n");
                allFiles.forEach(f -> redPrint("\t\t" + f));
            }

            if (trackedFiles.isEmpty()) {
                print("\nno changes added to commit (use \"jit add\" and/or \"jit commit\")");
            }
        }
    }
}
