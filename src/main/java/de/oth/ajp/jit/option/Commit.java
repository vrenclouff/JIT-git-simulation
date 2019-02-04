package de.oth.ajp.jit.option;


import de.oth.ajp.jit.core.FileWrapper;
import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.core.Staging;
import de.oth.ajp.jit.util.Logger;

import java.util.*;

import static de.oth.ajp.jit.util.CollectionsUtils.lastEntry;
import static de.oth.ajp.jit.util.JitFiles.*;
import static de.oth.ajp.jit.util.Logger.print;
import static java.lang.String.format;

/**
 * Class creates commits from staging.
 * 1. at first are all nodes in tree hashed
 * 2. last entry in map represents root of commit and it is saved to file system
 * 3. other nodes are saved to file system
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class Commit implements Option {

    private final String message;

    public Commit(String message) {
        this.message = message;
    }

    @Override
    public void runProcess() {
        modifyStaging(this::commitFiles, this::printError);
    }

    private void commitFiles(Staging staging) {

        if (staging.isEmpty()) {
            nothingToCommit();
            return;
        }

        Map<String, FileWrapper> hashedTree = staging.getHashedTree();
        Map.Entry<String, FileWrapper> firstFile = lastEntry(hashedTree);
        hashedTree.remove(firstFile.getKey());

        writeCommit(firstFile.getKey(), format(firstFile.getValue().getContent(), message), Logger::print);

        hashedTree.forEach((name, content) -> {
            if (content.isCopy()) {
                writeCommit(content.getPath(), name, Logger::print);
            } else if (content.isWrite()) {
                writeCommit(name, content.getContent(), Logger::print);
            }
        });

        staging.removeStaging();
    }

    private void nothingToCommit() {
        print("nothing to commit, working tree clean");
    }

    private void printError(String message) {
        print("error: can not read staging file");
    }
}
