package de.oth.ajp.jit.utils;


public class StringUtils {

    public static final String SPACE_DELIMITER = "\u001F";
    public static final String NEW_LINE = "\n";
    public static final String EMPTY = "";
    public static final String PATH_DELIMITER = "/";

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static String toUpperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
