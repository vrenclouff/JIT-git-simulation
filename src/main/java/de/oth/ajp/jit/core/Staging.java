package de.oth.ajp.jit.core;

import com.vrenclouff.Tree;
import com.vrenclouff.linked.LinkedTree;
import com.vrenclouff.linked.TreeNode;
import de.oth.ajp.jit.utils.FileUtils;

import java.io.Serializable;
import java.util.*;

import static de.oth.ajp.jit.utils.StringUtils.EMPTY;
import static de.oth.ajp.jit.utils.StringUtils.PATH_DELIMITER;
import static java.lang.String.format;


public class Staging implements Serializable {

    private class NodeWrapper {
        TreeNode<FileDescriptor> node;
        String path;

        NodeWrapper(TreeNode<FileDescriptor> node, String path) {
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

        root.children().forEach(c -> stack.add(new NodeWrapper(c,
                c.value().getName() + (c.isLeaf() ? EMPTY : pathDelimiter))));

        while(!stack.isEmpty()) {
            NodeWrapper node = stack.pop();
            if (node.node.isLeaf()) {
                paths.add(node.path);
            } else {
                node.node.children().forEach(c -> {
                    String path = format("%s%s%s", node.path, c.value().getName(), c.isLeaf() ? EMPTY : pathDelimiter);
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
