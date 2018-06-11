package de.oth.ajp.jit.options;


import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.util.Files;
import de.oth.ajp.jit.util.JitFiles;

import java.io.IOException;

import static de.oth.ajp.jit.util.JitFiles.walk;
import static de.oth.ajp.jit.util.Logger.print;
import static de.oth.ajp.jit.util.JitFiles.readCommits;
import static java.nio.file.Paths.get;


public class Checkout implements Option {

    private final String hash;

    public Checkout(String hash) {
        this.hash = hash;
    }

    @Override
    public void runProcess() {
        try {
            walk().forEach(e -> {
                try {
                    java.nio.file.Files.deleteIfExists(get(e).getName(0));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            readCommits().filter(c -> c.getHash().startsWith(hash))
                    .findFirst().map(Files::readCommitTree)
                    .ifPresentOrElse(list -> list.forEach(JitFiles::copyCommit), this::commitNotFound);
        } catch (IOException e) {

        }
    }

    private void commitNotFound() {
        print("error: pathspec '%s' did not match any file(s) known to jit.", hash);
    }
}
