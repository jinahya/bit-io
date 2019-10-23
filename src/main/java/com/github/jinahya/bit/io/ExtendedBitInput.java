package com.github.jinahya.bit.io;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

public class ExtendedBitInput {

    // -----------------------------------------------------------------------------------------------------------------
    public static boolean readBooleanIsNextNull(final BitInput input) throws IOException {
        return !input.readBoolean(); // 0b0 -> null
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected static int readUnsignedIntVariable(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, 5);
        if (!extended) {
            return size;
        }
        return input.readInt(true, size);
    }

    protected static long readUnsignedLongVariable(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, 6);
        if (!extended) {
            return size;
        }
        return input.readLong(true, size);
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
        final byte[] bytes = new byte[readUnsignedIntVariable(input)];
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

    /**
     * Reads a VLQ of an {@code int}.
     *
     * @param input a byte input to read value.
     * @param size  number of bits for a single group.
     * @return an {@code int} value of VLQ.
     * @throws IOException if an I/O error occurs.
     */
    public static int readVariableLengthQuantityInt(final BitInput input, final int size) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        requireValidSizeInt(true, size);
        int value = 0x00;
        boolean last = false;
        int quotient = Integer.SIZE / size;
        final int remainder = Integer.SIZE % size;
        if (remainder > 0) {
            quotient++;
        }
        for (int i = 0; i < quotient; i++) {
            last = input.readInt(true, 1) == 0;
            value <<= size;
            value |= input.readInt(true, size);
            if (last) {
                break;
            }
        }
        if (!last) {
            throw new IOException("no signal for last group");
        }
        return value;
    }

    /**
     * Reads a VLQ of a {@code long}.
     *
     * @param input a byte input to read value.
     * @param size  number of bits for a single group.
     * @return a {@code long} value of VLQ.
     * @throws IOException if an I/O error occurs.
     */
    public static long readVariableLengthQuantityLong(final BitInput input, final int size) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        requireValidSizeLong(true, size);
        long value = 0x00L;
        boolean last = false;
        int quotient = Long.SIZE / size;
        final int remainder = Long.SIZE % size;
        if (remainder > 0) {
            quotient++;
        }
        for (int i = 0; i < quotient; i++) {
            last = input.readInt(true, 1) == 0;
            value <<= size;
            value |= input.readLong(true, size);
            if (last) {
                break;
            }
        }
        if (!last) {
            throw new IOException("no signal for last group");
        }
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    protected ExtendedBitInput() {
        super();
    }
}
