package de.oth.ajp.jit.util;

import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.oth.ajp.jit.util.StringUtils.EMPTY;

public final class Hash {

    private Hash() {}

    public static String sha256(String object) {
        return Hashing.sha256().hashBytes(object.getBytes()).toString();
    }

    public static String sha256File(Path path) {
        if (Files.exists(path) && Files.isRegularFile(path)) {
            try {
                return sha256(new String(Files.readAllBytes(path)));
            } catch (IOException e) {
                return EMPTY;
            }
        } else {
            return EMPTY;
        }
    }
}
