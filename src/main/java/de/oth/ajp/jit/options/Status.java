package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.core.Staging;

import java.util.List;

import static de.oth.ajp.jit.core.FileManager.loadAllFiles;
import static de.oth.ajp.jit.core.FileManager.loadStagingFile;
import static de.oth.ajp.jit.utils.StringUtils.ANSI_GREEN;
import static de.oth.ajp.jit.utils.StringUtils.ANSI_RED;
import static de.oth.ajp.jit.utils.StringUtils.ANSI_RESET;
import static java.util.List.of;


public class Status implements Option {

    enum  JitFileStatus {
        UNTRACKED   (ANSI_RED),
        TRACKED     (ANSI_GREEN),

        ;
        private final String color;

        JitFileStatus(String color) {
            this.color = color;
        }
        public String format(String string) {
            return color + string + ANSI_RESET;
        }
    }


    @Override
    public void runProcess() {

        List<String> trackedFiles = loadStagingFile().map(Staging::getTrackedFiles).orElse(of());
        List<String> allFiles = loadAllFiles();

        allFiles.removeAll(trackedFiles);

        if (!trackedFiles.isEmpty()) {
            System.out.println("\nChanges to be committed:");
            System.out.println("\t(use \"jit remove <file>...\" to unstage)\n");
            trackedFiles.forEach(f -> System.out.println("\t\t" + JitFileStatus.TRACKED.format(f)));
        }

        if (!allFiles.isEmpty()) {
            System.out.println("\nChanges not staged for commit:");
            System.out.println("\t(use \"jit add <file>...\" to update what will be committed)\n");
            allFiles.forEach(f -> System.out.println("\t\t" + JitFileStatus.UNTRACKED.format(f)));
        }

        if (trackedFiles.isEmpty()) {
            System.out.println("\nno changes added to commit (use \"jit add\" and/or \"jit commit\")");
        }
    }
}
