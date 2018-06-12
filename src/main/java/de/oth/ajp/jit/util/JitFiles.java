package de.oth.ajp.jit.util;


import de.oth.ajp.jit.domain.CommitFile;
import de.oth.ajp.jit.core.Staging;
import de.oth.ajp.jit.domain.FileDescriptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static de.oth.ajp.jit.util.PathUtils.toRelative;
import static de.oth.ajp.jit.util.SerializableUtils.toBytes;
import static de.oth.ajp.jit.util.SerializableUtils.toObject;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public final class JitFiles {

    private JitFiles(){}

    private static final String FOLDER = ".jit";
    private static final String[] SUB_FOLDERS = {"objects", "staging"};
    private static final String STAGING_FILE = "staging.ser";
    private static final String JIT_IGNORE_FILE = ".jitignore";
    private static final String WORKING_DIR = System.getProperty("user.dir");


    private static final Path PATH_STAGING, PATH_OBJECTS, WORKING_PATH;

    private static final List<String> IGNORED_FILES;

    static {
        PATH_OBJECTS = get(FOLDER, SUB_FOLDERS[0]);
        PATH_STAGING = get(FOLDER, SUB_FOLDERS[1], STAGING_FILE);
        WORKING_PATH = get(WORKING_DIR);
        IGNORED_FILES = readIgnoredFiles(get(JIT_IGNORE_FILE));
    }

    private static List<String> readIgnoredFiles(Path ignoreFile) {

        List<String> files;

        if (Files.exists(ignoreFile)) {
            try {
                files = Files.readAllLines(ignoreFile);
            } catch (IOException e) {
                files = new ArrayList<>(3);
            }
        } else {
            files = new ArrayList<>(3);
        }

        files.add(FOLDER);
        files.add(PATH_STAGING.toString());
        files.add(JIT_IGNORE_FILE);

        return files;
    }

    public static void createMainDirectories() throws IOException {
        for (String subFolder : SUB_FOLDERS) {
            Path path = Paths.get(FOLDER, subFolder);
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            } else {
                throw new IOException("Jit was already initialized.");
            }
        }
    }

    public static Optional<Staging> readStaging() throws IOException {
        if (Files.exists(PATH_STAGING)) {
            return ofNullable(toObject(Files.readAllBytes(PATH_STAGING)));
        } else {
            return of(Staging.newInstance());
        }
    }

    public static void modifyStaging(Consumer<Staging> action) throws IOException {
        Optional<Staging> optionalStaging = readStaging();
        if (optionalStaging.isPresent()) {
            Staging staging = optionalStaging.get();
            action.accept(staging);
            writeStaging(staging);
        }
    }

    public static void modifyStaging(Consumer<Staging> action, Consumer<String> error) {
        try {
            modifyStaging(action);
        } catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    public static void writeStaging(Staging staging) throws IOException {
        if (staging.isRemove()) {
            Files.delete(PATH_STAGING);
        } else {
            Files.write(PATH_STAGING, toBytes(staging));
        }
    }

    public static void writeCommit(String name, String content) throws IOException {
        Files.write(PathUtils.get(PATH_OBJECTS, name), content.getBytes());
    }

    public static void writeCommit(String name, String content, Consumer<String> error) {
        try {
            writeCommit(name, content);
        }catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    public static void writeCommit(Path source, String targetName) throws IOException {
        Path destPath = PathUtils.get(PATH_OBJECTS, targetName);
        Files.copy(source, destPath);
    }

    public static void writeCommit(Path source, String targetName, Consumer<String> error) {
        try {
            writeCommit(source, targetName);
        } catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    public static Stream<Path> walk() throws IOException {
        String pathString = WORKING_PATH.toUri().getPath();
        return Files.walk(WORKING_PATH)
                .sorted(Comparator.reverseOrder())
                .map(e -> toRelative(e, pathString))
                .filter(JitFiles::isNotIgnored)
                .map(Paths::get);

    }

    public static Stream<String> filesWalk() throws IOException {
        return walk().filter(Files::isRegularFile).map(Path::toString);
    }

    public static boolean isNotIgnored(String path) {
        for (String ignore : IGNORED_FILES) {
            if (path.startsWith(ignore)) {
                return false;
            }
        }
        return true;
    }

    public static Stream<CommitFile> readCommits() {
        try {
            return Files.walk(PATH_OBJECTS).filter(de.oth.ajp.jit.util.Files::isCommitFile)
                    .map(de.oth.ajp.jit.util.Files::readCommitFile);
        }catch (IOException e) {
            return Stream.empty();
        }
    }

    public static Optional<CommitFile> readLastCommit() {
        return readCommits().max(Comparator.comparing(CommitFile::getCreated));
    }

    public static Optional<CommitFile> readCommit(String name) {
        return readCommit(get(name));
    }

    public static Optional<CommitFile> readCommit(Path name) {
        return ofNullable(de.oth.ajp.jit.util.Files.readCommitFile(PATH_OBJECTS.resolve(name)));
    }

    public static void copyCommit(FileDescriptor fileDescriptor) {
        copyCommit(fileDescriptor.getHash(), fileDescriptor.getPath(), null);
    }

    public static void copyCommit(String name, Path destPath, Consumer<String> error) {
        try {
            copyCommit(name, destPath);
        } catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    public static void copyCommit(String sourceName, Path target) throws IOException {
        Path commitPath = PathUtils.get(PATH_OBJECTS, sourceName);

        if (Files.notExists(target.getParent())) {
            Files.createDirectories(target.getParent());
        }

        Files.copy(commitPath, target, REPLACE_EXISTING);
    }

    public static List<FileDescriptor> unchangedFileDescriptors() {
        return readCommits().map(de.oth.ajp.jit.util.Files::readCommitTree).flatMap(Collection::stream)
                .filter(cf -> cf.getHash().equals(Hash.sha256File(cf.getPath())))
                .collect(toList());
    }

    public static List<String> unchangedFilePaths() {
        return unchangedFileDescriptors().stream().map(cf -> cf.getPath().toString()).collect(toList());
    }
}
