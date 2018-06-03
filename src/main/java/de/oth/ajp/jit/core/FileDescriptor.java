package de.oth.ajp.jit.core;


import java.io.Serializable;
import java.util.Objects;

public class FileDescriptor implements Serializable {

    private final String name;
    private final FileType type;

    public static FileDescriptor createFile(String name) {
        return new FileDescriptor(name, FileType.FILE);
    }

    public static FileDescriptor createDirectory(String name) {
        return new FileDescriptor(name, FileType.DIRECTORY);
    }

    public static FileDescriptor createCommit(String name) {
        return new FileDescriptor(name, FileType.COMMIT);
    }

    public FileDescriptor(String name, FileType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FileType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDescriptor that = (FileDescriptor) o;
        return Objects.equals(name, that.name) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
