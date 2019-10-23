package com.github.jinahya.bit.io;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoConstants.EXPONENT_INTEGER;
import static com.github.jinahya.bit.io.BitIoConstants.EXPONENT_LONG;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

public class ExtendedBitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * .
     *
     * @param output the bit output.
     * @param value  the value to be checked.
     * @return {@code true} if the {@code value} is {@code null}; {@code false} otherwise.
     * @throws IOException if an I/O error occurs.
     */
    protected static boolean writeBooleanIsNextNull(final BitOutput output, final Object value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        output.writeBoolean(value != null); // null -> 0b0
        return value == null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static void writeUnsignedIntVariable(final BitOutput output, final int value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("length(" + value + ") < 0");
        }
        final int size = Integer.SIZE - Integer.numberOfLeadingZeros(value);
        final boolean extended = size > EXPONENT_INTEGER;
        output.writeBoolean(extended);
        if (!extended) {
            output.writeInt(true, EXPONENT_INTEGER, value);
            return;
        }
        output.writeInt(true, EXPONENT_INTEGER, size);
        output.writeInt(true, size, value);
    }

    public static void writeUnsignedLongVariable(final BitOutput output, final long value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0L) {
            throw new IllegalArgumentException("length(" + value + ") < 0L");
        }
        final int size = Long.SIZE - Long.numberOfLeadingZeros(value);
        final boolean extended = size > EXPONENT_LONG;
        output.writeBoolean(extended);
        if (!extended) {
            output.writeLong(true, EXPONENT_LONG, value);
            return;
        }
        output.writeInt(true, EXPONENT_LONG, size);
        output.writeLong(true, size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends BitWritable> void writeObject(final boolean nullable, final BitOutput output,
                                                           final T value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        value.write(output);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static void writeBytes(final boolean nullable, final BitOutput output, final boolean unsigned,
                                  final int size, final byte[] value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("bitOutput is null");
        }
        requireValidSizeByte(unsigned, size);
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        writeUnsignedIntVariable(output, value.length);
        for (final byte v : value) {
            output.writeByte(unsigned, size, v);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static void writeString(final boolean nullable, final BitOutput output, final String value,
                                   final String charset)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("bitOutput is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (charset == null) {
            throw new NullPointerException("charset is null");
        }
        final byte[] bytes = value.getBytes(charset);
        writeBytes(false, output, false, 8, bytes);
    }

    public static void writeAsciiString(final boolean nullable, final BitOutput output, final String value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("bitOutput is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final byte[] bytes = value.getBytes("US-ASCII");
        writeBytes(false, output, true, 7, bytes);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a VLQ of an {@code int}.
     *
     * @param output a bit output to write values.
     * @param size   a number of bits for a group.
     * @param value  a value to write.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeVariableLengthQuantityInt(final BitOutput output, final int size, final int value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        requireValidSizeInt(true, size);
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }
        final int mask = -1 >>> (Integer.SIZE - size);
        final int ones = Integer.SIZE - Integer.numberOfLeadingZeros(value);
        int quotient = ones / size;
        final int remainder = ones % size;
        if (remainder > 0) {
            quotient++;
        }
        for (int i = quotient - 1; i > 0; i--) {
            output.writeInt(true, 1, 1);
            output.writeInt(true, size, (value >> (i * size)) & mask);
        }
        output.writeInt(true, 1, 0);
        output.writeInt(true, size, value & mask);
    }

    public static void writeVariableLengthQuantityLong(final BitOutput output, final int size, final long value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        requireValidSizeLong(true, size);
        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }
        final long mask = -1L >>> (Long.SIZE - size);
        final int ones = Long.SIZE - Long.numberOfLeadingZeros(value);
        int quotient = ones / size;
        final int remainder = ones % size;
        if (remainder > 0) {
            quotient++;
        }
        for (int i = quotient - 1; i > 0; i--) {
            output.writeInt(true, 1, 1);
            output.writeLong(true, size, (value >> (i * size)) & mask);
        }
        output.writeInt(true, 1, 0);
        output.writeLong(true, size, value & mask);
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected ExtendedBitOutput() {
        super();
    }
}
