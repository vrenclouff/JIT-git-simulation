package de.oth.ajp.jit.core;


import java.util.List;

import static de.oth.ajp.jit.utils.StringUtils.ANSI_RESET;
import static de.oth.ajp.jit.utils.StringUtils.ANSI_YELLOW;
import static de.oth.ajp.jit.utils.StringUtils.NEW_LINE;

public class CommitFile {

    private final String commitHash;
    private final String createdDate;
    private final String message;
    private final List<String> files;

    public CommitFile(String commitHash, String createdDate, String message, List<String> files) {
        this.commitHash = commitHash;
        this.createdDate = createdDate;
        this.message = message;
        this.files = files;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public List<String> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return ANSI_YELLOW + "commit " + commitHash + ANSI_RESET + NEW_LINE +
                "Date: " + createdDate + "\n" +
                "\n\t" + message + "\n\n";
    }
}
