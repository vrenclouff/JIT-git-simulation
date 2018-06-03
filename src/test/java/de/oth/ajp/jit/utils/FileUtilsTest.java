package de.oth.ajp.jit.utils;

import de.oth.ajp.jit.core.FileDescriptor;
import de.oth.ajp.jit.core.FileType;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void convertToDescriptors() {

        String path = "src/de/FileA.java";

        FileDescriptor[] fileDescriptors = FileUtils.convertToDescriptors(path, "/");

        assertEquals("src", fileDescriptors[0].getName());
        assertEquals(FileType.DIRECTORY, fileDescriptors[0].getType());

        assertEquals("de", fileDescriptors[1].getName());
        assertEquals(FileType.DIRECTORY, fileDescriptors[1].getType());

        assertEquals("FileA.java", fileDescriptors[2].getName());
        assertEquals(FileType.FILE, fileDescriptors[2].getType());
    }
}