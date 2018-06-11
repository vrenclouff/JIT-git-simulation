package de.oth.ajp.jit.core;

import com.vrenclouff.Tree;
import com.vrenclouff.linked.LinkedTree;
import com.vrenclouff.linked.TreeNode;
import de.oth.ajp.jit.domain.FileDescriptor;

import java.io.Serializable;
import java.util.*;

import static de.oth.ajp.jit.domain.FileType.COMMIT;
import static de.oth.ajp.jit.util.Converter.pathToDescriptors;
import static de.oth.ajp.jit.util.StringUtils.EMPTY;
import static de.oth.ajp.jit.util.StringUtils.PATH_DELIMITER;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;


public class Staging implements Serializable {

    private class NodePath {
        TreeNode<FileDescriptor> node;
        String path;

        NodePath(TreeNode<FileDescriptor> node, String path) {
            this.node = node;
            this.path = path;
        }
    }


    private final FileNode root;
    private final Tree<FileDescriptor> graph;
    private final String delimiter;

    private transient boolean remove = false;

    public static Staging newInstance() {
        return new Staging(PATH_DELIMITER);
    }

    private Staging(String delimiter) {
        this.delimiter = delimiter;
        this.root = new FileNode(new FileDescriptor(EMPTY, COMMIT, EMPTY), null);
        this.graph = new LinkedTree<>(root);
    }

    public void add(String path) {
        this.graph.add(pathToDescriptors(path, delimiter));
    }

    public void remove(String path) {
        this.graph.remove(pathToDescriptors(path, delimiter));
    }

    public void removeStaging() {
        remove = true;
    }

    public boolean isRemove() {
        return remove;
    }

    public Map<String, FileWrapper> getHashedTree() {

        if (isEmpty()) {
            return emptyMap();
        }

        Map<String, FileWrapper> map = new LinkedHashMap<>(graph.size());
        root.hash(map);

        return map;
    }

    public List<String> getTrackedFiles() {

        List<String> paths = new LinkedList<>();
        Stack<NodePath> stack = new Stack<>();

        root.children().forEach(c -> stack.add(new NodePath(c,
                c.value().getPath() + (c.isLeaf() ? EMPTY : delimiter))));

        while(!stack.isEmpty()) {
            NodePath wrapper = stack.pop();
            if (wrapper.node.isLeaf()) {
                paths.add(wrapper.path);
            } else {
                wrapper.node.children().forEach(c -> {
                    String path = format("%s%s%s", wrapper.path, c.value().getPath(), c.isLeaf() ? EMPTY : delimiter);
                    stack.add(new NodePath(c, path));
                });
            }
        }

        return paths;
    }

    public boolean isEmpty() {
        return graph.size() <= 0;
    }
}
