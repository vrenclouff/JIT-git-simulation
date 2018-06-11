package de.oth.ajp.jit.domain;


import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.Paths.get;

public class FileDescriptor implements Serializable {

    private final String name;
    private final FileType type;
    private final String hash;

    private transient Path path;


    public FileDescriptor(String name, FileType type, String hash) {
        this.name = name;
        this.path = null;
        this.type = type;
        this.hash = hash;
    }

    public FileType getType() {
        return type;
    }

    public Path getPath() {
        return path == null ? get(name) : path;
    }

    public String getHash() {
        return hash;
    }

    public void addPath(Path path) {
        this.path = get(path.toString(), getPath().toString());
    }

    public boolean isFile() {
        return type.equals(FileType.FILE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDescriptor that = (FileDescriptor) o;
        return Objects.equals(path, that.path) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, type);
    }
}
