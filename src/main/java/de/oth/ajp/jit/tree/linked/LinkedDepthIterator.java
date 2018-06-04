package de.oth.ajp.jit.tree.linked;

import java.io.Serializable;
import java.util.*;


class LinkedDepthIterator<T extends Serializable> implements LinkedIterator {

    private enum NodeType { WHITE, GRAY }

    private class IteratorWrapper {
        Node<T> node;
        Iterator<Node<T>> nodeIterator;
        NodeType type;

        IteratorWrapper(Iterator<Node<T>> nodeIterator) {
            this.nodeIterator = nodeIterator;
            this.move();
        }

        Node<T> move() {
            Node<T> result = node;
            node = nodeIterator.next();
            type = NodeType.WHITE;
            return result;
        }
    }

    private final Stack<IteratorWrapper> stack = new Stack<>();

    LinkedDepthIterator(LinkedTree<T> tree) {
        stack.add(new IteratorWrapper(List.of(tree.getRoot()).iterator()));
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        for (;;) {
            IteratorWrapper wrapper = stack.peek();

            if (wrapper.type.equals(NodeType.GRAY)) {
                if (wrapper.nodeIterator.hasNext()) {
                    return wrapper.move().getValue();
                } else {
                    return stack.pop().node.getValue();
                }
            }
            wrapper.type = NodeType.GRAY;
            List<Node<T>> children = wrapper.node.getChildren();
            if (!children.isEmpty()) {
                stack.push(new IteratorWrapper(children.iterator()));
            }
        }
    }
}