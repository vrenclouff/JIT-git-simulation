package de.oth.ajp.jit.tree.linked;


import java.io.Serializable;
import java.util.List;
import java.util.Stack;

class LinkedBreadthIterator<T extends Serializable> implements LinkedIterator {

    private final Stack<Node<T>> stack = new Stack<>();

    LinkedBreadthIterator(LinkedTree<T> tree) {
        stack.add(tree.getRoot());
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        Node<T> node = stack.pop();
        List<Node<T>> children = node.getChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            stack.add(children.get(i));
        }
        return node.getValue();
    }
}