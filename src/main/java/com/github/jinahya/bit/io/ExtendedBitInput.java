package com.github.jinahya.bit.io;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;

public class ExtendedBitInput {

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends BitReadable> T readObject(final BitInput input, final Class<T> type) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        try {
            final Constructor<T> constructor = type.getDeclaredConstructor();
            if (!constructor.isSynthetic()) {
                constructor.setAccessible(true);
            }
            try {
                final T value = constructor.newInstance();
                value.read(input);
                return value;
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
    public static byte[] readBytes(final BitInput bitInput, final int lengthSize, final boolean byteUnsigned,
                                   final int byteSize)
            throws IOException {
        if (bitInput == null) {
            throw new NullPointerException("bitInput is null");
        }
        requireValidSizeInt(true, lengthSize);
        requireValidSizeByte(byteUnsigned, byteSize);
        final int length = bitInput.readInt(true, lengthSize);
        final byte[] byteArray = new byte[length];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = bitInput.readByte(byteUnsigned, byteSize);
        }
        return byteArray;
    }

    public static byte[] readBytes(final BitInput bitInput, final int lengthSize, final int byteSize)
            throws IOException {
        return readBytes(bitInput, lengthSize, false, byteSize);
    }

    // @todo: Add readShorts, readInts, and readLongs.

    // -----------------------------------------------------------------------------------------------------------------
    public static String readString(final BitInput bitInput, final int lengthSize, final int byteSize,
                                    final String charsetName)
            throws IOException {
        return new String(readBytes(bitInput, lengthSize, byteSize), charsetName);
    }

    public static String readString(final BitInput bitInput, final int lengthSize, final int byteSize,
                                    final Charset charset)
            throws IOException {
        return new String(readBytes(bitInput, lengthSize, byteSize), charset);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static String readAscii(final BitInput bitInput, final int lengthSize) throws IOException {
        return new String(readBytes(bitInput, lengthSize, true, 7), "ASCII");
    }

//    public static String readAsciiPrintable(final BitInput bitInput, final int lengthSize) throws IOException {
//        final byte[] byteArray = readBytes(bitInput, lengthSize, )
//        return readString(bitInput, lengthSize, 7, "ASCII");
//    }

    // -----------------------------------------------------------------------------------------------------------------
    protected ExtendedBitInput() {
        super();
    }
}
