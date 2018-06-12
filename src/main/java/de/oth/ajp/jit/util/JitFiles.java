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

/**
 * Class contains static method for work with Jit files.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
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

    /**
     * Reads and creates list of ignored files/folders.
     * @param ignoreFile path to ignore file
     * @return list of files/folders which are ignored
     */
    private static List<String> readIgnoredFiles(Path ignoreFile) {

        List<String> files;

        if (Files.exists(ignoreFile)) {
            try {
                files = Files.readAllLines(ignoreFile);
            } catch (IOException e) {
                files = new ArrayList<>(1);
            }
        } else {
            files = new ArrayList<>(1);
        }

        files.add(FOLDER);

        return files;
    }

    /**
     * Creates main directories for jit.
     * @throws IOException
     */
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

    /**
     * Loads staging file and deserializable to {@link Staging} class.
     * @return optional of staging instance
     * @throws IOException
     */
    public static Optional<Staging> readStaging() throws IOException {
        if (Files.exists(PATH_STAGING)) {
            return ofNullable(toObject(Files.readAllBytes(PATH_STAGING)));
        } else {
            return of(Staging.newInstance());
        }
    }

    /**
     * Wrapper for work with staging.
     * 1. at first is loaded staging file
     * 2. run action
     * 3. save staging to file system
     *
     * @param action action which can happen in staging
     * @throws IOException
     */
    public static void modifyStaging(Consumer<Staging> action) throws IOException {
        Optional<Staging> optionalStaging = readStaging();
        if (optionalStaging.isPresent()) {
            Staging staging = optionalStaging.get();
            action.accept(staging);
            writeStaging(staging);
        }
    }

    /**
     * Wrapper for work with staging.
     * 1. at first is loaded staging file
     * 2. run action
     * 3. save staging to file system
     *
     * @param action action which can happen in staging
     * @param error handling for exception
     */
    public static void modifyStaging(Consumer<Staging> action, Consumer<String> error) {
        try {
            modifyStaging(action);
        } catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    /**
     * Writes and serializable staging file to file system.
     * @param staging
     * @throws IOException
     */
    public static void writeStaging(Staging staging) throws IOException {
        if (staging.isRemove()) {
            Files.delete(PATH_STAGING);
        } else {
            Files.write(PATH_STAGING, toBytes(staging));
        }
    }

    /**
     * Creates commit.
     * @param name name of commit (hash)
     * @param content content of commit
     * @throws IOException
     */
    public static void writeCommit(String name, String content) throws IOException {
        Files.write(PathUtils.get(PATH_OBJECTS, name), content.getBytes());
    }

    /**
     * Creates commit.
     * @param name name of commit (hash)
     * @param content content of commit
     * @param error handling for exception
     */
    public static void writeCommit(String name, String content, Consumer<String> error) {
        try {
            writeCommit(name, content);
        }catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    /**
     * Creates commit from existing file.
     * @param source source file which will be copy as commit file
     * @param targetName hash of commit
     * @throws IOException
     */
    public static void writeCommit(Path source, String targetName) throws IOException {
        Path destPath = PathUtils.get(PATH_OBJECTS, targetName);
        Files.copy(source, destPath);
    }

    /**
     * Write commit to folders with commits.
     * @param source source file which will be copy as commit file
     * @param targetName hash of commit
     * @param error handling for exception
     */
    public static void writeCommit(Path source, String targetName, Consumer<String> error) {
        try {
            writeCommit(source, targetName);
        } catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    /**
     * Returns stream of path which (files and directories) which are in root folder (includes sub-folders).
     * @return stream of path
     * @throws IOException
     */
    public static Stream<Path> walk() throws IOException {
        String pathString = WORKING_PATH.toUri().getPath();
        return Files.walk(WORKING_PATH)
                .sorted(Comparator.reverseOrder())
                .map(e -> toRelative(e, pathString))
                .filter(JitFiles::isNotIgnored)
                .map(Paths::get);

    }

    /**
     * Returns stream of string path (only files) which are in root folder (includes sub-folders) and can be added to staging.
     * @return stream of files
     * @throws IOException
     */
    public static Stream<String> filesWalk() throws IOException {
        return walk().filter(Files::isRegularFile).map(Path::toString);
    }

    /**
     * Check if the string path is in ignore file (.jitignore)
     * @param path string path to file
     * @return true or false
     */
    public static boolean isNotIgnored(String path) {
        for (String ignore : IGNORED_FILES) {
            if (path.startsWith(ignore)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Read all commits.
     * @return stream of commit file
     */
    public static Stream<CommitFile> readCommits() {
        try {
            return Files.walk(PATH_OBJECTS).filter(de.oth.ajp.jit.util.Files::isCommitFile)
                    .map(de.oth.ajp.jit.util.Files::readCommitFile);
        }catch (IOException e) {
            return Stream.empty();
        }
    }

    /**
     * Read last commit
     * @return optional of commit file
     */
    public static Optional<CommitFile> readLastCommit() {
        return readCommits().max(Comparator.comparing(CommitFile::getCreated));
    }

    /**
     * Read commit from string path
     * @param path string path
     * @return
     */
    public static Optional<CommitFile> readCommit(String path) {
        return readCommit(get(path));
    }

    /**
     * Read commit from path
     * @param path path to commit
     * @return optional of commit file
     */
    public static Optional<CommitFile> readCommit(Path path) {
        return ofNullable(de.oth.ajp.jit.util.Files.readCommitFile(PATH_OBJECTS.resolve(path)));
    }

    /**
     * Copy commit
     * @param fileDescriptor file descriptor
     */
    public static void copyCommit(FileDescriptor fileDescriptor) {
        copyCommit(fileDescriptor.getHash(), fileDescriptor.getPath(), Logger::print);
    }

    /**
     * Copy commit.
     * @param sourceName hash of committed file
     * @param target path where will be file created
     * @param error handler for exception
     */
    public static void copyCommit(String sourceName, Path target, Consumer<String> error) {
        try {
            copyCommit(sourceName, target);
        } catch (IOException e) {
            error.accept(e.getMessage());
        }
    }

    /**
     * Copy commit.
     * @param sourceName hash of committed file
     * @param target path where will be file created
     * @throws IOException
     */
    public static void copyCommit(String sourceName, Path target) throws IOException {
        Path commitPath = PathUtils.get(PATH_OBJECTS, sourceName);

        if (Files.notExists(target.getParent())) {
            Files.createDirectories(target.getParent());
        }

        Files.copy(commitPath, target, REPLACE_EXISTING);
    }

    /**
     * Returns list of unchanged file descriptors.
     * @return list of descriptors
     */
    public static List<FileDescriptor> unchangedFileDescriptors() {
        return readCommits().map(de.oth.ajp.jit.util.Files::readCommitTree).flatMap(Collection::stream)
                .filter(cf -> cf.getHash().equals(Hash.sha256File(cf.getPath())))
                .collect(toList());
    }

    /**
     * Returns list of unchanged files.
     * @return list of files
     */
    public static List<String> unchangedFilePaths() {
        return unchangedFileDescriptors().stream().map(cf -> cf.getPath().toString()).collect(toList());
    }
}
