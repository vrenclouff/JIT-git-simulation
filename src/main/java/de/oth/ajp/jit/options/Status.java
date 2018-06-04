package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.core.Staging;

import java.util.List;

import static de.oth.ajp.jit.core.FileManager.loadAllFiles;
import static de.oth.ajp.jit.core.FileManager.loadStagingFile;
import static de.oth.ajp.jit.core.Logger.greenPrint;
import static de.oth.ajp.jit.core.Logger.print;
import static de.oth.ajp.jit.core.Logger.redPrint;
import static java.util.List.of;


public class Status implements Option {

    @Override
    public void runProcess() {

        List<String> trackedFiles = loadStagingFile().map(Staging::getTrackedFiles).orElse(of());
        List<String> allFiles = loadAllFiles();

        allFiles.removeAll(trackedFiles);

        if (!trackedFiles.isEmpty()) {
            print("\nChanges to be committed:");
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
