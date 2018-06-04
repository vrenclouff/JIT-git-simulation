package de.oth.ajp.jit.options;


import de.oth.ajp.jit.core.FileManager;
import de.oth.ajp.jit.core.Option;

import java.util.Map;

import static de.oth.ajp.jit.utils.CollectionsUtils.lastEntry;

public class Commit implements Option {

    private final String message;

    public Commit(String message) {
        this.message = message;
    }

    @Override
    public void runProcess() {
        FileManager.editStagingFile(staging -> {
            if (staging.isEmpty()) {
                nothingToCommit();
                return;
            }

            Map<String, String> hashedTree = staging.getHashedTree();
            hashedTree.forEach(FileManager::createCommitFile);
            Map.Entry<String, String> firstFile = lastEntry(hashedTree);
            String fileName = firstFile.getKey();
            String fileContent = String.format(firstFile.getValue(), message);
            FileManager.createCommitFile(fileName, fileContent);
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
