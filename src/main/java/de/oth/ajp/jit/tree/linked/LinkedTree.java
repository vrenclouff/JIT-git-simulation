package de.oth.ajp.jit.tree.linked;

import de.oth.ajp.jit.tree.Tree;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

public class LinkedTree<T extends Serializable> implements Tree<T> {

    private final Node<T> root;
    private int size;

    public LinkedTree(T rootValue) {
        this.root = new Node<>(rootValue, null);
        this.size = 1;
    }

    public LinkedTree(Node<T> root) {
        this.root = root;
        this.size = 0;
    }

    @SafeVarargs @Override
    public final void add(T... values) {
        add(List.of(values).iterator(), root);
    }

    @SafeVarargs @Override
    public final void addWithRoot(T... values) {
        Iterator<T> valuesIterator = List.of(values).iterator();
        Node<T> node = root;

        if (!node.getValue().equals(valuesIterator.next())) {
            throw new IllegalArgumentException("Uzel nesedi");
        }

        add(valuesIterator, node);
    }

    private void add(Iterator<T> valuesIterator, Node<T> root) {
        Node<T> node = root;
        branch:
        while (valuesIterator.hasNext()) {
            T value = valuesIterator.next();

            List<Node<T>> children = node.getChildren();
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

    private Node<T> newNodeInstance(T value, Node<T> node) {
        try {
            return root.getClass().getConstructor(value.getClass(), Node.class).newInstance(value, node);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            return new Node<>(value, node);
        }
    }

    @SafeVarargs @Override
    public final void remove(T... values) {
        Iterator<T> valuesIterator = List.of(values).iterator();
        Node<T> node = root;
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
            Node<T> parent = node.getParent();
            parent.getChildren().remove(node);
            node = parent;
            size--;
        }
    }

    Node<T> getRoot() {
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
