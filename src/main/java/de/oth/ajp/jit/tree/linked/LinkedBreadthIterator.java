package de.oth.ajp.jit.tree.linked;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedBreadthIterator<T extends Serializable> extends LinkedIterator {


    public LinkedBreadthIterator(LinkedTree<T> tree) {
        super(tree);
    }

    @Override
    protected Iterator<Node<T>> createStack(LinkedTree tree) {
        List<Node<T>> stack = new ArrayList<>(tree.size());

        // TODO create stack

        return stack.iterator();
    }
}