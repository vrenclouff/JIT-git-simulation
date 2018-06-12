package de.oth.ajp.jit.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static de.oth.ajp.jit.util.StringUtils.EMPTY;
import static de.oth.ajp.jit.util.StringUtils.PATH_DELIMITER;

/**
 * Class extends functionality of {@link Paths}.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public final class PathUtils {

    private PathUtils() {}

    /**
     * Method extends path with string path components.
     * @param path basic path
     * @param paths other path components
     * @return path
     */
    public static Path get(Path path, String... paths) {
        return Paths.get(path.toString(), paths);
    }

    /**
     * Convert path to relative path.
     * @param path full path
     * @param basePath base path
     * @return relative path from basePath
     */
    public static String toRelative(Path path, String basePath) {
        basePath = basePath.endsWith(PATH_DELIMITER) ? basePath : basePath + PATH_DELIMITER;
        return path.toUri().getPath().replace(basePath, EMPTY);
    }

    /**
     * Reverse string path component and creates path
     * @param pathComponents path components
     * @return path
     */
    public static Path reverse(String... pathComponents){
        int size = pathComponents.length - 1;
        String[] reversePath = new String[size + 1];
        for (int i = size; i >= 0; i--) {
            reversePath[i] = pathComponents[size - i];
        }
        return Paths.get("", reversePath);
    }

    /**
     * Reverse list of path components and creates path
     * @param pathComponents list of path compnents
     * @return path
     */
    public static Path reverse(List<String> pathComponents) {
        return reverse(pathComponents.toArray(new String[0]));
    }
}
