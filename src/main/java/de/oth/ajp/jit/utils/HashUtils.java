package de.oth.ajp.jit.utils;

import com.google.common.hash.Hashing;

public class HashUtils {

    public static String sha256(String object) {
        return Hashing.sha256().hashBytes(object.getBytes()).toString();
    }
}
