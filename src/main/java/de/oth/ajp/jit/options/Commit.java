package de.oth.ajp.jit.options;


import de.oth.ajp.jit.core.FileManager;
import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.utils.CollectionsUtils;

import java.util.Map;

public class Commit implements Option {

    private final String message;

    public Commit(String message) {
        this.message = message;
    }

    @Override
    public void runProcess() {
        FileManager.editStagingFile(staging -> {
            if (staging.isEmpty()) {
                printError();
                return;
            }

            Map<String, String> hashedTree = staging.getHashedTree();
            hashedTree.forEach((name, content) -> FileManager.createFile(name, content));
            Map.Entry<String, String> firstFile = CollectionsUtils.lastEntry(hashedTree);
            String fileName = firstFile.getKey();
            String fileContent = String.format(firstFile.getValue(), message);
            FileManager.createFile(fileName, fileContent);
            staging.removeStaging();
        }, this::printError);
    }

    private void nothingToCommit() {
        // TODO message
    }

    private void printError() {
        System.out.println("COMMIT ERROR");
    }
}
