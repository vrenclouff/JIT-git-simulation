package de.oth.ajp.jit.tree.linked;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public  class LinkedDepthIterator<T extends Serializable> extends LinkedIterator {


    public LinkedDepthIterator(LinkedTree<T> tree) {
        super(tree);
    }

    @Override
    protected Iterator createStack(LinkedTree tree) {
        List<Node<T>> stack = new ArrayList<>(tree.size());

        // TODO create stack

        return stack.iterator();
    }
}