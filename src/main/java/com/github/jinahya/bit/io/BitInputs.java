package com.github.jinahya.bit.io;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;

public class BitInputs {

    // -----------------------------------------------------------------------------------------------------------------
    public static boolean readBooleanIsNextNull(final BitInput input) throws IOException {
        return !input.readBoolean(); // 0b0 -> null
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected static int readLength(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final int size = input.readInt(true, 5);
        return input.readInt(true, size);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static <T extends BitReadable> T readObject(final boolean nullable, final BitInput input, final T object)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        if (nullable && readBooleanIsNextNull(input)) {
            return null;
        }
        object.read(input);
        return object;
    }

    public static <T extends BitReadable> T readObject(final boolean nullable, final BitInput input,
                                                       final Class<? extends T> type)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        if (nullable && readBooleanIsNextNull(input)) {
            return null;
        }
        try {
            final Constructor<? extends T> constructor = type.getDeclaredConstructor();
            if (!constructor.isSynthetic()) {
                constructor.setAccessible(true);
            }
            try {
                return readObject(false, input, constructor.newInstance());
            } catch (final IllegalAccessException iae) {
                throw new RuntimeException(iae);
            } catch (final InstantiationException ie) {
                throw new RuntimeException(ie);
            } catch (final InvocationTargetException ite) {
                throw new RuntimeException(ite);
            }
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static byte[] readBytes(final boolean nullable, final BitInput input, final boolean unsigned, final int size)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        requireValidSizeByte(unsigned, size);
        if (nullable && readBooleanIsNextNull(input)) {
            return null;
        }
        final byte[] bytes = new byte[readLength(input)];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = input.readByte(unsigned, size);
        }
        return bytes;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static String readString(final boolean nullable, final BitInput input, final int size, final String charset)
            throws IOException {
        final byte[] bytes = readBytes(nullable, input, false, size);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static String readAsciiString(final boolean nullable, final BitInput input) throws IOException {
        final byte[] bytes = readBytes(nullable, input, true, 7);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, "ASCII");
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected BitInputs() {
        super();
    }
}
