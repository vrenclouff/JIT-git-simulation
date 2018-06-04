package de.oth.ajp.jit.core;


import static de.oth.ajp.jit.utils.StringUtils.ANSI_GREEN;
import static de.oth.ajp.jit.utils.StringUtils.ANSI_RED;
import static de.oth.ajp.jit.utils.StringUtils.ANSI_RESET;

public class Logger {

    public static void print(String string) {
        System.out.println(string);
    }

    public static void redPrint(String string) {
        coloredPrint(string, ANSI_RED);
    }

    public static void greenPrint(String string) {
        coloredPrint(string, ANSI_GREEN);
    }

    private static void coloredPrint(String string, String color) {
        print(color + string + ANSI_RESET);
    }
}
