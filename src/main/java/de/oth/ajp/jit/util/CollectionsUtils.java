package de.oth.ajp.jit.util;


import java.util.*;
import java.util.function.*;

public final class CollectionsUtils {

    private CollectionsUtils() {}

    public static <T> T get(T[] array, int index) {
        try {
            return array[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static <T> void forEach(T[] array, BiConsumer<T, Integer> action) {
        for (int i = 0; i < array.length; i++) {
            action.accept(array[i], i);
        }
    }

    public static Map.Entry lastEntry(Map map) {
        List<Map.Entry<String,Integer>> entryList = new ArrayList<>(map.entrySet());
        return entryList.get(entryList.size()-1);
    }

    public static <T> void forEachReverse(List<T> list, Consumer<? super T> action) {
        for (int i = list.size() - 1; i >= 0; i--) {
            action.accept(list.get(i));
        }
    }
}
