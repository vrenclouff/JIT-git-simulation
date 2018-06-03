package de.oth.ajp.jit.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {

    public static Path get(Path path, String... paths) {
        return Paths.get(path.toString(), paths);
    }
}
