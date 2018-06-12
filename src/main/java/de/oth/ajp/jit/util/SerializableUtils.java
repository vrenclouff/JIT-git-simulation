package de.oth.ajp.jit.util;

import java.io.*;

/**
 * Class for support serialization and deserialization.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
public final class SerializableUtils {

    private SerializableUtils() {}

    /**
     * Serializable object to array of bytes.
     * @param object input object
     * @param <A> generic input
     * @return array of bytes
     */
    public static <A extends Serializable> byte[] toBytes(A object) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            new ObjectOutputStream(byteStream).writeObject(object);
            return byteStream.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    /**
     * Deserialize array of bytes to object
     * @param bytes array of bytes
     * @param <A> generic input
     * @return instance of object
     */
    public static <A extends Serializable> A toObject(byte[] bytes) {
        try {
            return (A) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}