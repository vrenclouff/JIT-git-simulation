package de.oth.ajp.jit.domain;


import java.util.Date;
import java.util.List;

import static de.oth.ajp.jit.util.StringUtils.*;

/**
 * Class keeps information about committed file.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class CommitFile {

    private final Date created;
    private final String message;
    private final List<FileDescriptor> files;

    private FileDescriptor fileDescriptor;

    /**
     * Constructor for sets all information about commit
     * @param hash hash of committed file
     * @param created dat of created commit
     * @param message message of commit
     * @param type type of commit {@link FileType}
     * @param files children which commit contains
     */
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
