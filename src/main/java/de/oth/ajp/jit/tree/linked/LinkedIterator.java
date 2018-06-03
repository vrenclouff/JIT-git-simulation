package de.oth.ajp.jit.tree.linked;


import java.io.Serializable;
import java.util.Iterator;


public abstract class LinkedIterator<T extends Serializable> implements Iterator<T> {

    private final Iterator<Node<T>> iteratorStack;

    public LinkedIterator(LinkedTree<T> tree) {
        this.iteratorStack = createStack(tree);
    }

    protected abstract Iterator<Node<T>> createStack(LinkedTree<T> tree);

    @Override
    public boolean hasNext() {
        return iteratorStack.hasNext();
    }

    @Override
    public T next() {
        return iteratorStack.next().getValue();
    }
}
