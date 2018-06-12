package de.oth.ajp.jit.util;


import de.oth.ajp.jit.domain.CommitFile;
import de.oth.ajp.jit.domain.FileDescriptor;
import de.oth.ajp.jit.domain.FileType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static de.oth.ajp.jit.util.JitFiles.readCommit;
import static de.oth.ajp.jit.util.StringUtils.EMPTY;
import static de.oth.ajp.jit.util.StringUtils.SPACE_DELIMITER;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.readAttributes;
import static java.util.stream.Collectors.toList;

/**
 * Class extends functionality of {@link java.nio.file.Files}.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public final class Files {

    private Files() {}

    /**
     * Returns first line in file.
     * @param path file
     * @return first line in file
     * @throws IOException
     */
    public static String readFirstLine(Path path) throws IOException {
        try (BufferedReader reader = newBufferedReader(path)) {
            String line = reader.readLine();
            if (line == null)
                return EMPTY;
            return line;
        }
    }

    /**
     * Checks if the file is commit file
     * @param path path to commit file
     * @return true or false
     */
    public static boolean isCommitFile(Path path) {
        try {
            String firstLine = readFirstLine(path);
            String stringFileType = firstLine.split("\t")[0].toUpperCase();
            String stringCommitType = FileType.COMMIT.name().toUpperCase();
            return stringFileType.equals(stringCommitType);
        }catch (IOException e) {
            return false;
        }
    }

    /**
     * Converts path (commit file) to class {@link CommitFile} represents committed file
     * @param path path to commit file
     * @return instance of committed file
     */
    public static CommitFile readCommitFile(Path path) {
        try {

            String hash = path.getFileName().toString();
            BasicFileAttributes attr = readAttributes(path, BasicFileAttributes.class);
            List<String> allLines = readAllLines(path);


            Date created = new Date(attr.creationTime().toMillis());
            String[] headComponents = allLines.get(0).split("\t");
            FileType commitFileType = FileType.valueOf(headComponents[0].toUpperCase());
            String message = CollectionsUtils.get(headComponents, 1);

            List<FileDescriptor> files = allLines.subList(1, allLines.size()).stream().map(commitLine -> {
                String[] components = commitLine.split(SPACE_DELIMITER);
                FileType type = FileType.valueOf(components[0].toUpperCase());
                String hashFileName = components[1];
                String realName = components[2];
                return new FileDescriptor(realName, type, hashFileName);
            }).collect(toList());

            return new CommitFile(hash, created, message, commitFileType, files);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Loads root commit and creates list of committed files.
     * @param root root commit
     * @return list of files which was committed
     */
    public static List<FileDescriptor> readCommitTree(CommitFile root) {
        List<FileDescriptor> files = new LinkedList<>();
        Stack<CommitFile> stack = new Stack<>();
        stack.add(root);

        while (!stack.isEmpty()) {
            CommitFile actual = stack.pop();

            actual.getFiles().forEach(desc -> {
                if (desc.isFile()) {
                    desc.addPath(actual.getFileDescriptor().getPath());
                    files.add(desc);
                } else {
                    readCommit(desc.getHash()).ifPresent(commit -> {
                        desc.addPath(actual.getFileDescriptor().getPath());
                        commit.setFileDescriptor(desc);
                        stack.add(commit);
                    });
                }
            });
        }
        return files;
    }
}
