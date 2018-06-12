package de.oth.ajp.jit.util;

import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.oth.ajp.jit.util.StringUtils.EMPTY;

/**
 * Class for support work with hashes.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public final class Hash {

    private Hash() {}

    /**
     * Creates hash using sha256 algorithm from string
     * @param object string for hash
     * @return hashed input
     */
    public static String sha256(String object) {
        return Hashing.sha256().hashBytes(object.getBytes()).toString();
    }

    /**
     * Creates hash from file
     * @param path path to file
     * @return hash of file
     */
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
