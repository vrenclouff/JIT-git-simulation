package de.oth.ajp.jit.core;

import com.google.common.collect.Lists;
import de.oth.ajp.jit.tree.linked.Node;
import de.oth.ajp.jit.utils.HashUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.oth.ajp.jit.utils.StringUtils.NEW_LINE;
import static de.oth.ajp.jit.utils.StringUtils.SPACE_DELIMITER;


public class FileNode extends Node<FileDescriptor> {

    public FileNode(FileDescriptor value, Node<FileDescriptor> parent) {
        super(value, parent);
    }

    public String hash(Map<String, String> content) {
        FileDescriptor desc = getValue();
        StringBuilder builder = new StringBuilder();

        if (isLeaf()) {
            builder.append(FileManager.readStringContent(buildFilePathComponents()));
        } else {
            builder.append(desc.getType().getTitle()).append(NEW_LINE);
            for (Node<FileDescriptor> item : getChildren()) {
                FileNode child = (FileNode) item;
                FileDescriptor childDesc = child.getValue();
                String hash = child.hash(content);
                builder.append(childDesc.getType().getTitle())
                        .append(SPACE_DELIMITER).append(hash)
                        .append(SPACE_DELIMITER)
                        .append(childDesc.getName()).append(NEW_LINE);
            }
        }

        String stringContent = builder.toString();
        String hashContent = HashUtils.sha256(stringContent);
        content.put(hashContent, stringContent);
        return hashContent;
    }

    public void path(StringBuilder builder, List<String> paths, String delimiter) {

        switch (getValue().getType()) {
            case FILE:      paths.add(builder.toString() + getValue().getName());   break;
            case DIRECTORY: builder.append(getValue().getName()).append(delimiter); break;
        }

        for (Node<FileDescriptor> item : getChildren()) {
            ((FileNode) item).path(builder, paths, delimiter);
        }
    }

    private String[] buildFilePathComponents() {
        Node<FileDescriptor> parent = getParent();
        List<String> pathComponents = new ArrayList<>();
        pathComponents.add(getValue().getName());
        while(!parent.isRoot()) {
            pathComponents.add(parent.getValue().getName());
            parent = parent.getParent();
        }
        return Lists.reverse(pathComponents).toArray(new String[0]);
    }
}
