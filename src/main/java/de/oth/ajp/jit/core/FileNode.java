package de.oth.ajp.jit.core;

import com.vrenclouff.linked.TreeNode;
import de.oth.ajp.jit.utils.HashUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.reverse;
import static de.oth.ajp.jit.core.FileManager.readStringContent;
import static de.oth.ajp.jit.utils.StringUtils.NEW_LINE;
import static de.oth.ajp.jit.utils.StringUtils.SPACE_DELIMITER;


public class FileNode extends TreeNode<FileDescriptor> {

    public FileNode(FileDescriptor value, TreeNode<FileDescriptor> parent) {
        super(value, parent);
    }

    public String hash(Map<String, String> content) {
        FileDescriptor desc = value();
        StringBuilder builder = new StringBuilder();

        if (isLeaf()) {
            TreeNode<FileDescriptor> parent = parent();
            List<String> pathComponents = new ArrayList<>();
            pathComponents.add(value().getName());
            while(!parent.isRoot()) {
                pathComponents.add(parent.value().getName());
                parent = parent.parent();
            }
            builder.append(readStringContent(reverse(pathComponents).toArray(new String[0])));
        } else {
            builder.append(desc.getType().getTitle()).append(NEW_LINE);
            for (TreeNode<FileDescriptor> item : children()) {
                FileNode child = (FileNode) item;
                FileDescriptor childDesc = child.value();
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
}
