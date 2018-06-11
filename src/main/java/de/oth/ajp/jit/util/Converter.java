package de.oth.ajp.jit.util;


import de.oth.ajp.jit.domain.FileDescriptor;

import static de.oth.ajp.jit.domain.FileType.DIRECTORY;
import static de.oth.ajp.jit.domain.FileType.FILE;
import static de.oth.ajp.jit.util.CollectionsUtils.forEach;
import static de.oth.ajp.jit.util.StringUtils.EMPTY;

public final class Converter {

    private Converter() {}

    public static FileDescriptor[] pathToDescriptors(String stringPath, String delimiter) {
        String[] components = stringPath.split(delimiter);
        FileDescriptor[] fileDescriptors = new FileDescriptor[components.length];
        forEach(components, (value, index) -> fileDescriptors[index] = new FileDescriptor(value, DIRECTORY, EMPTY));
        fileDescriptors[fileDescriptors.length - 1] =  new FileDescriptor(components[components.length - 1], FILE, EMPTY);
        return fileDescriptors;
    }
}
