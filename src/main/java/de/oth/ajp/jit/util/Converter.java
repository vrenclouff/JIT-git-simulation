package de.oth.ajp.jit.util;


import de.oth.ajp.jit.domain.FileDescriptor;

import static de.oth.ajp.jit.domain.FileType.DIRECTORY;
import static de.oth.ajp.jit.domain.FileType.FILE;
import static de.oth.ajp.jit.util.CollectionsUtils.forEach;
import static de.oth.ajp.jit.util.StringUtils.EMPTY;

/**
 * Class for support work with converting.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public final class Converter {

    private Converter() {}

    /**
     * Convert path to descriptor of file.
     * @param stringPath file's string path
     * @param delimiter delimiter in path
     * @return array of descriptor
     */
    public static FileDescriptor[] pathToDescriptors(String stringPath, String delimiter) {
        String[] components = stringPath.split(delimiter);
        FileDescriptor[] fileDescriptors = new FileDescriptor[components.length];
        forEach(components, (value, index) -> fileDescriptors[index] = new FileDescriptor(value, DIRECTORY, EMPTY));
        fileDescriptors[fileDescriptors.length - 1] =  new FileDescriptor(components[components.length - 1], FILE, EMPTY);
        return fileDescriptors;
    }
}
