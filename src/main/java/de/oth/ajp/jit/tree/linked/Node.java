package de.oth.ajp.jit.tree.linked;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class Node<T extends Serializable> implements Serializable {

    private final T value;
    private final Node<T> parent;
    private final List<Node<T>> children;

    protected Node(T value, Node<T> parent) {
        this.value = value;
        this.parent = parent;
        this.children = new LinkedList<>();
    }

    public T getValue() {
        return value;
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    public boolean isRoot() {
        return parent == null;
    }
}