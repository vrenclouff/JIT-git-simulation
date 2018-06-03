package de.oth.ajp.jit;



public class Main {

    public static void main(String[] args) {

       Commands.parseArgs(args).runProcess();


    }

    private static void measure(Runnable method) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++ ) {
            method.run();
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
    }
}
