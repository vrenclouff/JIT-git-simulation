package de.oth.ajp.jit.util;


import java.util.*;
import java.util.function.*;

/**
 * Class for support work with collections.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public final class CollectionsUtils {

    private CollectionsUtils() {}

    /**
     * Returns value from array.
     * @param array array from which we will get a value
     * @param index index to array
     * @param <T> generic value
     * @return return value from array or null if the index is out from range of array
     */
    public static <T> T get(T[] array, int index) {
        try {
            return array[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * For each for array.
     * @param array input array
     * @param action action for each value in array
     * @param <T> generic value
     */
    public static <T> void forEach(T[] array, BiConsumer<T, Integer> action) {
        for (int i = 0; i < array.length; i++) {
            action.accept(array[i], i);
        }
    }

    /**
     * Returns last entry in map.
     * @param map input map
     * @return last entry
     */
    public static Map.Entry lastEntry(Map map) {
        List<Map.Entry<String,Integer>> entryList = new ArrayList<>(map.entrySet());
        return entryList.get(entryList.size()-1);
    }
}
