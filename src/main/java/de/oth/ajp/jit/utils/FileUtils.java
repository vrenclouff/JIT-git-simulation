package de.oth.ajp.jit.utils;


import de.oth.ajp.jit.core.CommitFileHead;
import de.oth.ajp.jit.core.FileDescriptor;
import de.oth.ajp.jit.core.FileType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;

import static de.oth.ajp.jit.utils.StringUtils.EMPTY;
import static de.oth.ajp.jit.utils.StringUtils.SPACE_DELIMITER;
import static java.nio.file.Files.newBufferedReader;

public class FileUtils {

    public static FileDescriptor[] convertToDescriptors(String path, String delimiter) {
        String[] components = path.split(delimiter);
        String fileName = components[components.length - 1];
        FileDescriptor[] fileDescriptors = new FileDescriptor[components.length];
        for (int i = 0; i < components.length - 1; i++) {
            fileDescriptors[i] = FileDescriptor.createDirectory(components[i]);
        }
        fileDescriptors[fileDescriptors.length - 1] = FileDescriptor.createFile(fileName);
        return fileDescriptors;
    }

    public static CommitFileHead convertToDescriptor(String commitLine) {
        String[] components = commitLine.split(SPACE_DELIMITER);
        FileType type = FileType.valueOf(components[0].toUpperCase());
        String hashFileName = components[1];
        String realName = components[2];
        return new CommitFileHead(realName, type, hashFileName);
    }

    public static FileDescriptor convertToDescriptor(CommitFileHead fileHead) {
        return new FileDescriptor(fileHead.getPath(), fileHead.getType());
    }

    public static String readFirstLine(Path path) throws IOException {
        try (BufferedReader reader = newBufferedReader(path)) {
            String line = reader.readLine();
            if (line == null)
                return EMPTY;
            return line;
        }
    }
}
