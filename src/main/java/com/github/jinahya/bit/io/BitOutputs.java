package com.github.jinahya.bit.io;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.ulp;

public class BitOutputs {

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
    protected static void writeLength(final BitOutput output, final int length) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length(" + length + ") < 0");
        }
        if (length == 0) {
            output.writeBoolean(true);
            return;
        }
        final int size = (int) ceil(log(length) / log(2.0d) + ulp(1.0d));
        assert size < 32;
        output.writeUnsignedInt(5, size);
        output.writeUnsignedInt(size, length);
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
        writeLength(output, value.length);
        for (final byte v : value) {
            output.writeByte(unsigned, size, v);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static void writeString(final boolean nullable, final BitOutput output, final String value, final String charset) {
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected BitOutputs() {
        super();
    }
}
