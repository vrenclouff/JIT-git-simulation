package de.oth.ajp.jit.core;

import de.oth.ajp.jit.tree.linked.LinkedTree;
import de.oth.ajp.jit.tree.Tree;
import de.oth.ajp.jit.utils.FileUtils;

import java.io.Serializable;
import java.util.*;

import static de.oth.ajp.jit.utils.StringUtils.EMPTY;
import static de.oth.ajp.jit.utils.StringUtils.PATH_DELIMITER;


public class Staging implements Serializable {

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

    public void clean() {
        this.graph.clean();
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
        root.path(new StringBuilder(), paths, pathDelimiter);
        return paths;
    }

    public boolean isEmpty() {
        return graph.size() <= 0;
    }
}
