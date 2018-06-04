package de.oth.ajp.jit.tree;

import de.oth.ajp.jit.core.FileDescriptor;
import de.oth.ajp.jit.core.FileNode;
import de.oth.ajp.jit.tree.linked.LinkedTree;
import de.oth.ajp.jit.utils.FileUtils;
import de.oth.ajp.jit.utils.SerializableUtils;
import de.oth.ajp.jit.core.Staging;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class LinkedTreeTest {

    @Test
    void breadthIterator() {

        /*
         *              0
         *              |
         *              1
         *              |
         *              2
         *            /  \
         *           4    3
         *               / \
         *              4   5
         */

        Tree<Integer> tree = new LinkedTree<>(0);

        tree.add(0,1,2,4);
        tree.add(0,1,2,3,4);
        tree.add(0,1,2,3,5);

        assertEquals(7, tree.size());

        Iterator<Integer> iterator = tree.breadthIterator();

        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());
        assertFalse(iterator.hasNext());


    }

    @Test
    void depthIterator() {

        /*
         *              0
         *              |
         *              1
         *              |
         *              2
         *            /  \
         *           4    3
         *               / \
         *              4   5
         */

        Tree<Integer> tree = new LinkedTree<>(0);

        tree.addWithRoot(0,1,2,4);
        tree.addWithRoot(0,1,2,3,4);
        tree.add(1,2,3,5);

        assertEquals(7, tree.size());

        Iterator<Integer> iterator = tree.depthIterator();

        assertEquals(4, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(0, iterator.next().intValue());

        assertFalse(iterator.hasNext());

    }

    @Test
    void createPaths() {

        FileNode root = new FileNode(FileDescriptor.createDirectory("."), null);
        Tree<FileDescriptor> tree = new LinkedTree<>(root);

        tree.add(FileUtils.convertToDescriptors("src/de/B.java", "/"));
        tree.add(FileUtils.convertToDescriptors("src/de/A.java", "/"));

        List<String> paths = new LinkedList<>();
        //root.path(new StringBuilder(), paths, "/");

        assertEquals(2, paths.size());
        assertEquals("./src/de/B.java",  paths.get(0));
        assertEquals("./src/de/A.java", paths.get(1));
    }

    @Test
    void ownNode() {

        FileNode root = new FileNode(FileDescriptor.createDirectory("."), null);
        Tree<FileDescriptor> tree = new LinkedTree<>(root);

        tree.add(FileUtils.convertToDescriptors("src/de/B.java", "/"));
        tree.add(FileUtils.convertToDescriptors("src/de/A.java", "/"));

        Map<String, String> content = new LinkedHashMap<>();
        root.hash(content);

        assertEquals(4, content.size());
    }

    @Test
    void addElement() {

        Staging staging = Staging.newInstance();

        String fileA = "src/main/de/oth/A.java";
        String fileB = "src/main/de/oth/pkg/B.java";

        staging.add(fileA);
        staging.add(fileB);

        byte[] bytes = SerializableUtils.toBytes(staging);

        Staging newStaging = SerializableUtils.toObject(Staging.class, bytes);
    }


}