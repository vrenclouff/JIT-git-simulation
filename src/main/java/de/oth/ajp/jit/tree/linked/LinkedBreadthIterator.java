package de.oth.ajp.jit.tree.linked;


import java.io.Serializable;
import java.util.Stack;

import static de.oth.ajp.jit.utils.CollectionsUtils.forEachReverse;

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
        forEachReverse(node.getChildren(), stack::add);
        return node.getValue();
    }
}