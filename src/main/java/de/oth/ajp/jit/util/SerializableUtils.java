package de.oth.ajp.jit.util;

import java.io.*;


public final class SerializableUtils {

    private SerializableUtils() {}

    public static <A extends Serializable> byte[] toBytes(A object) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            new ObjectOutputStream(byteStream).writeObject(object);
            return byteStream.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static <A extends Serializable> A toObject(byte[] bytes) {
        try {
            return (A) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}