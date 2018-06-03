package de.oth.ajp.jit.core;



import de.oth.ajp.jit.utils.FileUtils;
import de.oth.ajp.jit.utils.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static de.oth.ajp.jit.utils.FileUtils.readFirstLine;
import static de.oth.ajp.jit.utils.SerializableUtils.toBytes;
import static de.oth.ajp.jit.utils.SerializableUtils.toObject;
import static de.oth.ajp.jit.utils.StringUtils.EMPTY;
import static java.lang.String.format;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;


public class FileManager {

    private static final String FOLDER = ".jit";
    private static final String[] SUB_FOLDERS = {"objects", "staging"};
    private static final String STAGING_FILE = "staging.ser";
    private static final String JIT_IGNORE_FILE = ".jitignore";

    private static final Path PATH_STAGING;
    private static final Path PATH_OBJECTS;
    private static final Path PATH_JIT_IGNORE;

    static {
        PATH_OBJECTS = get(format("%s/%s", FOLDER, SUB_FOLDERS[0]));
        PATH_STAGING = get(format("%s/%s/%s", FOLDER, SUB_FOLDERS[1], STAGING_FILE));
        PATH_JIT_IGNORE = get(JIT_IGNORE_FILE);
    }

    public static void createMainFolder() throws IOException {
        Path root = get(FOLDER);
        if (notExists(root)) {
            createDirectory(root);
            for (String subFolder : SUB_FOLDERS) {
                Path subPath = get(FOLDER+"/"+subFolder);
                if (notExists(subPath)) {
                    createDirectory(subPath);
                }
            }

        }
    }

    public static void editStagingFile(Consumer<Staging> consumer, Runnable ifError) {
        loadStagingFile().ifPresentOrElse(consumer.andThen(FileManager::saveStagingFile), ifError);
    }

    public static Optional<Staging> loadStagingFile() {
        if (exists(PATH_STAGING)) {
            try {
                return ofNullable(toObject(Staging.class, readAllBytes(PATH_STAGING)));
            } catch (IOException e) {
                return empty();
            }
        } else {
            return of(Staging.newInstance());
        }
    }

    private static void saveStagingFile(Staging staging) {
        try {
            if (staging.isRemove()) {
                delete(PATH_STAGING);
            } else {
                write(PATH_STAGING, toBytes(staging));
            }
        }catch (IOException e) {
            // TODO nepodarilo se ulozit soubor
        }
    }

    public static void createFile(String name, String content) {
        try {
            Path path = PathUtils.get(PATH_OBJECTS, name);
            write(path, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readStringContent(String[] pathComponents) {
        try {
            return new String(readAllBytes(get(EMPTY, pathComponents)));
        } catch (IOException e) {
            return EMPTY;
        }
    }

    public static String readCommitContent(String hash) {
        try {
            return new String(Files.readAllBytes(PathUtils.get(PATH_OBJECTS, hash)));
        }catch (IOException e) {
            return EMPTY;
        }
    }

    public static List<CommitFileHead> loadCommitFile(String hash) {
        try {
            List<String> lines = Files.readAllLines(PathUtils.get(PATH_OBJECTS, hash));
            return lines.subList(1, lines.size()).stream().map(FileUtils::convertToDescriptor).collect(toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<CommitFile> loadCommits() {
        try {
            return walk(PATH_OBJECTS).filter(FileManager::isCommitFile)
                    .map(FileManager::mapToCommitFile).collect(toList());
        }catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<String> loadAllFiles() {
        try {
            Path actualPath = get(EMPTY);
            List<String> jitIgnore = readAllLines(PATH_JIT_IGNORE);
            jitIgnore.add(FOLDER);
            String pathString = actualPath.toUri().getPath();
            return walk(actualPath).filter(Files::isRegularFile)
                    .map(e -> e.toUri().getPath().replace(pathString, EMPTY))
                    .filter(p -> jitIgnore(p, jitIgnore))
                    .collect(toList());
        }catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static boolean jitIgnore(String stringPath, List<String> jitIgnore) {
        for (String ignore : jitIgnore) {
            if (stringPath.startsWith(ignore)) {
                return false;
            }
        }
        return true;
    }


    private static boolean isCommitFile(Path path) {
        try {
            String firstLine = readFirstLine(path);
            String stringFileType = firstLine.split("\t")[0].toUpperCase();
            String stringCommitType = FileType.COMMIT.name().toUpperCase();
            return stringFileType.equals(stringCommitType);
        }catch (IOException e) {
            return false;
        }
    }

    private static CommitFile mapToCommitFile(Path path) {
        try {
            BasicFileAttributes attr = readAttributes(path, BasicFileAttributes.class);
            String commitHash = path.getFileName().toString();
            String stringDate = attr.creationTime().toString();
            List<String> allLines = readAllLines(path);
            String message = allLines.get(0).split("\t")[1];
            List<String> files = allLines.subList(1, allLines.size());
            return new CommitFile(commitHash, stringDate, message, files);
        } catch (IOException e) {
            return null;
        }
    }

}
