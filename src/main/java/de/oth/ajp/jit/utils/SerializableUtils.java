package de.oth.ajp.jit.utils;

import java.io.*;


public class SerializableUtils {

    public static <A> byte[] toBytes(A object) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            new ObjectOutputStream(byteStream).writeObject(object);
            return byteStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static <A> A toObject(Class<A> aClass, byte[] bytes) {
        try {
            return (A)new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}