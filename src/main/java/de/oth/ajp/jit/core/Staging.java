package de.oth.ajp.jit.core;

import de.oth.ajp.jit.tree.linked.LinkedTree;
import de.oth.ajp.jit.tree.Tree;
import de.oth.ajp.jit.tree.linked.Node;
import de.oth.ajp.jit.utils.FileUtils;

import java.io.Serializable;
import java.util.*;

import static de.oth.ajp.jit.utils.StringUtils.EMPTY;
import static de.oth.ajp.jit.utils.StringUtils.PATH_DELIMITER;
import static java.lang.String.format;


public class Staging implements Serializable {

    private class NodeWrapper {
        Node<FileDescriptor> node;
        String path;

        public NodeWrapper(Node<FileDescriptor> node, String path) {
            this.node = node;
            this.path = path;
        }
    }

    private final FileNode root;
    private final Tree<FileDescriptor> graph;
    private final String pathDelimiter;
    private boolean remove = false;

    public static Staging newInstance() {
        return new Staging(PATH_DELIMITER);
    }

    private Staging(String pathDelimiter) {
        this.pathDelimiter = pathDelimiter;
        this.root = new FileNode(FileDescriptor.createCommit(EMPTY), null);
        this.graph = new LinkedTree<>(root);
    }

    public void add(String path) {
        this.graph.add(FileUtils.convertToDescriptors(path, pathDelimiter));
    }

    public void remove(String path) {
        this.graph.remove(FileUtils.convertToDescriptors(path, pathDelimiter));
    }

    public void removeStaging() {
        remove = true;
    }

    public boolean isRemove() {
        return remove;
    }

    public Map<String, String> getHashedTree() {
        Map<String, String> map = new LinkedHashMap<>(graph.size());
        root.hash(map);
        return map;
    }

    public List<String> getTrackedFiles() {

        List<String> paths = new LinkedList<>();
        Stack<NodeWrapper> stack = new Stack<>();

        root.getChildren().forEach(c -> stack.add(new NodeWrapper(c,
                c.getValue().getName() + (c.isLeaf() ? EMPTY : pathDelimiter))));

        while(!stack.isEmpty()) {
            NodeWrapper node = stack.pop();
            if (node.node.isLeaf()) {
                paths.add(node.path);
            } else {
                node.node.getChildren().forEach(c -> {
                    String path = format("%s%s%s", node.path, c.getValue().getName(), c.isLeaf() ? EMPTY : pathDelimiter);
                    stack.add(new NodeWrapper(c, path));
                });
            }
        }

        return paths;
    }

    public boolean isEmpty() {
        return graph.size() <= 0;
    }
}
