package de.oth.ajp.jit.domain;


import java.util.Date;
import java.util.List;

import static de.oth.ajp.jit.util.StringUtils.*;

public class CommitFile {

    private final Date created;
    private final String message;
    private final List<FileDescriptor> files;

    private FileDescriptor fileDescriptor;

    public CommitFile(String hash, Date created, String message, FileType type, List<FileDescriptor> files) {
        this.created = created;
        this.message = message;
        this.files = files;
        this.fileDescriptor = new FileDescriptor(EMPTY, type, hash);
    }

    public String getHash() {
        return fileDescriptor.getHash();
    }

    public List<FileDescriptor> getFiles() {
        return files;
    }

    public Date getCreated() {
        return created;
    }

    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public void setFileDescriptor(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    @Override
    public String toString() {
        return ANSI_YELLOW + "commit " + fileDescriptor.getHash() + ANSI_RESET + NEW_LINE +
                "Date: " + created + "\n" +
                "\n\t" + message + "\n\n";
    }
}
