package de.oth.ajp.jit.core;


import java.nio.file.Path;

public class FileWrapper {

    enum Type { WRITE, COPY }

    private Path path;
    private String content;
    private Type type;


    FileWrapper(Path path, Type type) {
        this.path = path;
        this.type = type;
    }

    FileWrapper(String content, Type type) {
        this.content = content;
        this.type = type;
    }

    public boolean isCopy() {
        return type.equals(Type.COPY);
    }

    public boolean isWrite() {
        return type.equals(Type.WRITE);
    }

    public Path getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}
