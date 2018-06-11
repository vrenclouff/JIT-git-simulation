package de.oth.ajp.jit.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static de.oth.ajp.jit.util.StringUtils.EMPTY;
import static de.oth.ajp.jit.util.StringUtils.PATH_DELIMITER;

public final class PathUtils {

    private PathUtils() {}

    public static Path get(Path path, String... paths) {
        return Paths.get(path.toString(), paths);
    }

    public static String toRelative(Path path, String basePath) {
        basePath = basePath.endsWith(PATH_DELIMITER) ? basePath : basePath + PATH_DELIMITER;
        return path.toUri().getPath().replace(basePath, EMPTY);
    }

    public static Path reverse(String... pathComponents){
        int size = pathComponents.length - 1;
        String[] reversePath = new String[size + 1];
        for (int i = size; i >= 0; i--) {
            reversePath[i] = pathComponents[size - i];
        }
        return Paths.get("", reversePath);
    }

    public static Path reverse(List<String> pathComponents) {
        return reverse(pathComponents.toArray(new String[0]));
    }
}
