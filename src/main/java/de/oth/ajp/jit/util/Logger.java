package de.oth.ajp.jit.util;


import static de.oth.ajp.jit.util.StringUtils.ANSI_GREEN;
import static de.oth.ajp.jit.util.StringUtils.ANSI_RED;
import static de.oth.ajp.jit.util.StringUtils.ANSI_RESET;

/**
 * Class is wrapper for print strings. Contains method for formatted/colored print to console.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public class Logger {

    /**
     * Simple method for print string to the console.
     * @param string input to the print
     */
    public static void print(String string) {
        System.out.println(string);
    }

    /**
     * Simple method for print object to the console.
     * @param object input to the print
     */
    public static void print(Object object) {
        print(object.toString());
    }

    /**
     * Formatted method for print string with arguments to the console.
     * @param format string pattern
     * @param args parameters
     */
    public static void print(String format, Object... args) {
        print(String.format(format, args));
    }

    /**
     * Print string with red color.
     * @param string input to the print
     */
    public static void redPrint(String string) {
        coloredPrint(string, ANSI_RED);
    }

    /**
     * Print string with green color.
     * @param string input to the print
     */
    public static void greenPrint(String string) {
        coloredPrint(string, ANSI_GREEN);
    }

    /**
     * Colored print
     * @param string input to the print
     * @param color color
     */
    private static void coloredPrint(String string, String color) {
        print(color + string + ANSI_RESET);
    }
}
