package de.oth.ajp.jit.util;

import java.io.File;

/**
 * Class support work with strings.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public final class StringUtils {

    private StringUtils() {}

    public static final String SPACE_DELIMITER = "\u001F";
    public static final String NEW_LINE = "\n";
    public static final String EMPTY = "";
    public static final String PATH_DELIMITER = File.separator;

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * Upper case first character in string.
     * @param string input string
     * @return string with up first character
     */
    public static String toUpperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
