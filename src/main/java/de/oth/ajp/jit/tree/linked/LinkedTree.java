package de.oth.ajp.jit.tree.linked;

import de.oth.ajp.jit.tree.Tree;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

public class LinkedTree<T extends Serializable> implements Tree<T> {

    protected final Node<T> root;
    private int size;

    public LinkedTree(T rootValue) {
        this.root = new Node<>(rootValue, null);
        this.size = 0;
    }

    public LinkedTree(Node<T> root) {
        this.root = root;
        this.size = 0;
    }

    @Override
    public void add(T... values) {
        Iterator<T> valuesIterator = List.of(values).iterator();
        Node node = root;
        branch:
        while (valuesIterator.hasNext()) {
            List<Node<T>> children = node.getChildren();
            T value = valuesIterator.next();
            for (Node<T> child : children) {
                if (child.getValue().equals(value)) {
                    node = child;
                    continue branch;
                }
            }
           node = newNodeInstance(value, node);
            children.add(node);
            size++;
        }
    }

    private Node<T> newNodeInstance(T value, Node node) {
        try {
            return root.getClass().getConstructor(value.getClass(), Node.class).newInstance(value, node);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            return new Node(value, node);
        }
    }

    @Override
    public void remove(T... values) {
        Iterator<T> valuesIterator = List.of(values).iterator();
        Node node = root;
        while (valuesIterator.hasNext()) {
            List<Node<T>> children = node.getChildren();
            T value = valuesIterator.next();
            for (Node<T> child : children) {
                if (child.getValue().equals(value)) {
                    node = child;
                }
            }
        }

        while(!node.isRoot() && node.isLeaf()) {
            Node parent = node.getParent();
            parent.getChildren().remove(node);
            node = parent;
            size--;
        }
    }

    public Node<T> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clean() {
        this.root.getChildren().clear();
    }

    @Override
    public Iterator<T> depthIterator() {
        return new LinkedDepthIterator<>(this);
    }

    @Override
    public Iterator<T> breadthIterator() {
        return new LinkedBreadthIterator<>(this);
    }


}
