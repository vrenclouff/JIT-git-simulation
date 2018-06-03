package de.oth.ajp.jit.options;


import de.oth.ajp.jit.core.*;
import de.oth.ajp.jit.utils.FileUtils;

import java.util.*;

import static de.oth.ajp.jit.core.FileManager.loadCommitFile;
import static de.oth.ajp.jit.core.FileManager.loadCommits;
import static de.oth.ajp.jit.core.FileManager.readCommitContent;
import static de.oth.ajp.jit.utils.StringUtils.PATH_DELIMITER;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Checkout implements Option {

    private final String hash;


    public Checkout(String hash) {
        this.hash = hash;
    }

    @Override
    public void runProcess() {

        List<CommitFileHead> headFile = loadCommits().stream().filter(c -> c.getCommitHash().startsWith(hash)).findFirst().
                map(CommitFile::getFiles).orElse(emptyList()).stream().map(FileUtils::convertToDescriptor).collect(toList());

        if (headFile.isEmpty()) {
            printError();
        }

        Stack<CommitFileHead> stack = new Stack<>();
        Map<String, String> files = new HashMap<>();

        stack.addAll(headFile);

        while (!stack.isEmpty()) {
            CommitFileHead actual = stack.pop();
            switch (actual.getType()) {
                case FILE: {
                    files.put(actual.getPath(), readCommitContent(actual.getHash()));
                }
                break;
                case DIRECTORY: {
                    loadCommitFile(actual.getHash()).forEach(f -> {
                        f.setPath(format("%s%s%s", actual.getPath(), PATH_DELIMITER,f.getPath()));
                        stack.add(f);
                    });
                }
                break;
            }
        }
    }

    private void printError() {
        // TODO not found commit with this hash
    }
}
