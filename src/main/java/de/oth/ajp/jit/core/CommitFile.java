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

    /*
     *  commit 42a748778a50f8fe141000978705676ee827e40f
     *  Date:   Thu May 17 10:27:10 2018 +0200
     *
     *      Refactored modes and added javadoc
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(ANSI_YELLOW).append("commit ").append(commitHash).append(ANSI_RESET).append(NEW_LINE);
        builder.append("Date: ").append(createdDate).append("\n");
        builder.append("\n\t").append(message).append("\n\n");
        return builder.toString();
    }
}
