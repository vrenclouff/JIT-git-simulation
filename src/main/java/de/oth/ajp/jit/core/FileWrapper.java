package de.oth.ajp.jit.core;

import java.nio.file.Path;

/**
 * Class raped file/folder which is used while commit process.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class FileWrapper {

    private enum Type { WRITE, COPY }

    private Path path;
    private String content;
    private Type type;

    /**
     * Constructor creates wrapper for file which will be copied.
     * @param path path to file
     */
    FileWrapper(Path path) {
        this.path = path;
        this.type = Type.COPY;
    }

    /**
     * Constructor creates wrapper for new file which will be created.
     * @param content
     */
    FileWrapper(String content) {
        this.content = content;
        this.type = Type.WRITE;
    }

    /**
     * Test if the file will be copied.
     * @return true or false
     */
    public boolean isCopy() {
        return type.equals(Type.COPY);
    }

    /**
     * Test if the file will be created.
     * @return true or false
     */
    public boolean isWrite() {
        return type.equals(Type.WRITE);
    }

    /**
     * Gets path for file which will by copied.
     * @return path to source file
     */
    public Path getPath() {
        return path;
    }

    /**
     * Gets content of new fille.
     * @return string content of file
     */
    public String getContent() {
        return content;
    }
}
