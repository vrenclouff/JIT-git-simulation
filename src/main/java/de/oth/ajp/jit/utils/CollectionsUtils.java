package de.oth.ajp.jit.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionsUtils {

    public static <T> T get(T[] array, int index) {
        try {
            return array[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static Map.Entry lastEntry(Map map) {
        List<Map.Entry<String,Integer>> entryList = new ArrayList<>(map.entrySet());
        return entryList.get(entryList.size()-1);
    }
}
