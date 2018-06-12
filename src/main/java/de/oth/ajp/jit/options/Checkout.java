package de.oth.ajp.jit.options;


import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.util.Files;
import de.oth.ajp.jit.util.JitFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static de.oth.ajp.jit.util.JitFiles.readCommits;
import static de.oth.ajp.jit.util.JitFiles.walk;
import static de.oth.ajp.jit.util.Logger.print;


public class Checkout implements Option {

    private final String hash;

    public Checkout(String hash) {
        this.hash = hash;
    }

    @Override
    public void runProcess() {

        try {
            walk().map(Path::toFile).forEach(File::delete);
            readCommits().filter(c -> c.getHash().startsWith(hash))
                    .findFirst().map(Files::readCommitTree)
                    .ifPresentOrElse(list -> list.forEach(JitFiles::copyCommit), this::commitNotFound);
        } catch (IOException e) {
            print(e.getMessage());
        }
    }

    private void commitNotFound() {
        print("error: pathspec '%s' did not match any file(s) known to jit.", hash);
    }
}
