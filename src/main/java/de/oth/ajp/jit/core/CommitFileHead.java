package de.oth.ajp.jit.core;


public class CommitFileHead {

    private String path;
    private final FileType type;
    private final String hash;

    public CommitFileHead(String path, FileType type, String hash) {
        this.path = path;
        this.type = type;
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileType getType() {
        return type;
    }

    public String getHash() {
        return hash;
    }


}
