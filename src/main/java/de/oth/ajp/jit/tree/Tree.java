package de.oth.ajp.jit.tree;

import java.io.Serializable;
import java.util.Iterator;

public interface Tree<T extends Serializable> extends Serializable {

    void add(T... values);
    void remove(T... values);
    int size();
    void clean();

    Iterator<T> depthIterator();
    Iterator<T> breadthIterator();
}