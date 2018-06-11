package de.oth.ajp.jit.core;

import com.vrenclouff.linked.TreeNode;
import de.oth.ajp.jit.domain.FileDescriptor;
import de.oth.ajp.jit.util.Hash;
import de.oth.ajp.jit.util.PathUtils;

import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.oth.ajp.jit.util.StringUtils.NEW_LINE;
import static de.oth.ajp.jit.util.StringUtils.SPACE_DELIMITER;


public class FileNode extends TreeNode<FileDescriptor> {

    public FileNode(FileDescriptor value, TreeNode<FileDescriptor> parent) {
        super(value, parent);
    }

    String hash(Map<String, FileWrapper> hashes) {

        String hashContent;

        if (isLeaf()) {
            Map.Entry<String, FileWrapper> fileEntry = createCopyEntry();
            hashContent = fileEntry.getKey();
            hashes.put(fileEntry.getKey(), fileEntry.getValue());
        } else {
            Map.Entry<String, FileWrapper> fileEntry = createWriteEntry(hashes);
            hashContent = fileEntry.getKey();
            hashes.put(fileEntry.getKey(), fileEntry.getValue());
        }

        return hashContent;
    }

    private Map.Entry<String, FileWrapper> createCopyEntry() {

        TreeNode<FileDescriptor> parent = parent();
        List<String> pathComponents = new ArrayList<>();
        pathComponents.add(value().getPath().toString());

        while(!parent.isRoot()) {
            pathComponents.add(parent.value().getPath().toString());
            parent = parent.parent();
        }

        Path path = PathUtils.reverse(pathComponents);
        String hashContent = Hash.sha256File(path);

        return new AbstractMap.SimpleEntry<>(hashContent, new FileWrapper(path, FileWrapper.Type.COPY));
    }

    private Map.Entry<String, FileWrapper> createWriteEntry(Map<String, FileWrapper> hashes) {

        StringBuilder builder = new StringBuilder();
        builder.append(value().getType().getTitle()).append(NEW_LINE);
        for (TreeNode<FileDescriptor> item : children()) {
            FileNode child = (FileNode) item;
            FileDescriptor childDesc = child.value();
            String hash = child.hash(hashes);
            builder.append(childDesc.getType().getTitle())
                    .append(SPACE_DELIMITER).append(hash).append(SPACE_DELIMITER)
                    .append(childDesc.getPath()).append(NEW_LINE);
        }

        String stringContent = builder.toString();
        String hashContent = Hash.sha256(stringContent);
        return new AbstractMap.SimpleEntry<>(hashContent, new FileWrapper(stringContent, FileWrapper.Type.WRITE));

    }
}
