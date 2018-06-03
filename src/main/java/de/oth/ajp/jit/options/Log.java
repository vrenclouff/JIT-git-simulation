package de.oth.ajp.jit.options;

import de.oth.ajp.jit.core.FileManager;
import de.oth.ajp.jit.core.Option;


public class Log implements Option {

    @Override
    public void runProcess() {
        FileManager.loadCommits().forEach(System.out::println);
    }
}
