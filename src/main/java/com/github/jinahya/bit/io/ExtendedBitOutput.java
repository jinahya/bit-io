package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_BYTE;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_INTEGER;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_LONG;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_SHORT;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

class ExtendedBitOutput {

    // -----------------------------------------------------------------------------------------------------------------
    public static final int MAX_SIZE_UTF8 = 36;

    public static final long MAX_VALUE_UTF8 = -1L >>> (Long.SIZE - MAX_SIZE_UTF8);

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code 1}-bit {@code boolean} value representing the nullability of specified value to specified bit
     * output. This method writes {@code true} if specified value is {@code null} and writes {@code false} if specified
     * value is not {@code null}.
     *
     * @param output the bit output.
     * @param value  the value to be checked.
     * @return {@code true} if the {@code value} is {@code null}; {@code false} otherwise.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitInput#readBooleanIsNextNull(BitInput)
     */
    protected static boolean writeBooleanIsNextNull(final BitOutput output, final Object value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        output.writeBoolean(value != null); // null -> 0b0
        return value == null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void writeUnsignedVariableInt(final BitOutput output, int value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }
        int size = 1;
        int mask = 1;
        boolean next;
        do {
            final int v = value & mask;
            value >>= size;
            next = value > 0;
            output.writeBoolean(next);
            output.writeInt(true, size++, v);
            mask = (mask << 1) + 1;
        } while (next);
    }

    static void writeUnsignedVariableLong(final BitOutput output, long value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }
        int size = 1;
        long mask = 1L;
        boolean next;
        do {
            final long v = value & mask;
            value >>= size;
            next = value > 0L;
            output.writeBoolean(next);
            output.writeLong(true, size++, v);
            mask = (mask << 1) + 1L;
        } while (next);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void writeUnsignedVariable3(final BitOutput output, final byte value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }
        final int size = Byte.SIZE - (Integer.numberOfLeadingZeros(value) - 24);
        final boolean extended = size > SIZE_EXPONENT_BYTE;
        output.writeBoolean(extended);
        if (!extended) {
            output.writeByte(true, SIZE_EXPONENT_BYTE, value);
            return;
        }
        output.writeInt(true, SIZE_EXPONENT_BYTE, size);
        output.writeByte(true, size, value);
    }

    static void writeUnsignedVariable4(final BitOutput output, final short value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }
        final int size = Short.SIZE - (Integer.numberOfLeadingZeros(value) - 16);
        final boolean extended = size > SIZE_EXPONENT_SHORT;
        output.writeBoolean(extended);
        if (!extended) {
            output.writeShort(true, SIZE_EXPONENT_SHORT, value);
            return;
        }
        output.writeInt(true, SIZE_EXPONENT_SHORT, size);
        output.writeShort(true, size, value);
    }

    static void writeUnsignedVariable5(final BitOutput output, final int value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }
        final int size = Integer.SIZE - Integer.numberOfLeadingZeros(value);
        final boolean extended = size > SIZE_EXPONENT_INTEGER;
        output.writeBoolean(extended);
        if (!extended) {
            output.writeInt(true, SIZE_EXPONENT_INTEGER, value);
            return;
        }
        output.writeInt(true, SIZE_EXPONENT_INTEGER, size);
        output.writeInt(true, size, value);
    }

    static void writeUnsignedVariable6(final BitOutput output, final long value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }
        final int size = Long.SIZE - Long.numberOfLeadingZeros(value);
        final boolean extended = size > SIZE_EXPONENT_LONG;
        output.writeBoolean(extended);
        if (!extended) {
            output.writeLong(true, SIZE_EXPONENT_LONG, value);
            return;
        }
        output.writeInt(true, SIZE_EXPONENT_LONG, size);
        output.writeLong(true, size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an unsigned {@code int} for a length to specified bit output.
     *
     * @param output the bit output.
     * @param length the length value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeUnsignedVariable5(BitOutput, int)
     * @see #writeLengthLong(BitOutput, long)
     * @see ExtendedBitInput#readLengthInt(BitInput)
     */
    static void writeLengthInt(final BitOutput output, final int length) throws IOException {
        writeUnsignedVariable5(output, length);
    }

    static void writeLengthLong(final BitOutput output, final long length) throws IOException {
        writeUnsignedVariable6(output, length);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes specified array of bytes to specified bit output.
     *
     * @param nullable a flag for nullability.
     * @param output   the bit output to which bytes are written.
     * @param unsigned a flag for unsigned for each byte.
     * @param size     the number of valid bits in each byte.
     * @param value    the array of bytes to write.a
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitInput#readBytes(boolean, BitInput, boolean, int)
     */
    public static void writeBytes(final boolean nullable, final BitOutput output, final boolean unsigned,
                                  final int size, final byte[] value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("bitOutput is null");
        }
        requireValidSizeByte(unsigned, size);
        if (!nullable && value == null) {
            throw new NullPointerException("value is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        writeLengthInt(output, value.length);
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
        if (!nullable && value == null) {
            throw new NullPointerException("value is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        if (charset == null) {
            throw new NullPointerException("charset is null");
        }
        final byte[] bytes = value.getBytes(charset);
        writeBytes(false, output, false, 8, bytes);
    }

    public static void writeAscii(final boolean nullable, final BitOutput output, final String value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("bitOutput is null");
        }
        if (!nullable && value == null) {
            throw new NullPointerException("value is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
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
     * @see ExtendedBitInput#readVariableLengthQuantityInt(BitInput, int)
     */
    static void writeVariableLengthQuantityInt(final BitOutput output, final int size, final int value)
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

    static void writeVariableLengthQuantityLong(final BitOutput output, final int size, final long value)
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

    /**
     * Writes specified object to specified bit output.
     *
     * @param nullable a flag for nullability.
     * @param output   the bit output.
     * @param writer   a bit writer.
     * @param value    the object to write.
     * @param <T>      object type parameter
     * @throws IOException if an I/O error occurs.
     */
    static <T> void writeObject(final boolean nullable, final BitOutput output, final BitWriter<? super T> writer,
                                final T value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        if (!nullable && value == null) {
            throw new NullPointerException("value is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        writer.write(output, value);
    }

    /**
     * Writes all elements in specified collection to specified bit output using specified bit writer.
     *
     * @param output     the bit output to which values are written.
     * @param writer     the bit writer.
     * @param collection the collection whose elements are written.
     * @param <T>        object type parameter
     * @throws IOException if an I/O error occurs.
     * @see #writeObject(boolean, BitOutput, BitWriter, Object)
     * @see #writeObjects(boolean, BitOutput, BitWriter, List)
     * @see ExtendedBitInput#readObjects(BitInput, BitReader, Collection)
     */
    static <T extends BitWritable> void writeObjects(final BitOutput output, final BitWriter<? super T> writer,
                                                     final Collection<? extends T> collection)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (collection == null) {
            throw new NullPointerException("value is null");
        }
        writeLengthInt(output, collection.size());
        for (final T value : collection) {
            writeObject(true, output, writer, value);
        }
    }

    static <T extends BitWritable> void writeObjects(final boolean nullable, final BitOutput output,
                                                     final BitWriter<? super T> writer,
                                                     final List<? extends T> value)
            throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (!nullable && value == null) {
            throw new NullPointerException("value is null");
        }
        if (nullable && writeBooleanIsNextNull(output, value)) {
            return;
        }
        if (true) {
            writeObjects(output, writer, value);
            return;
        }
        assert value != null;
        writeLengthInt(output, value.size());
        for (final T v : value) {
            writeObject(true, output, writer, v);
        }
    }

    // ------------------------------------------------------------------------------------------------------------ utf8
    public static void writeUtf8(final BitOutput output, final long value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }
        if (value > MAX_VALUE_UTF8) {
            throw new IllegalArgumentException("value(" + value + ") > " + MAX_VALUE_UTF8);
        }
        if (value <= 0x7FL) {
            output.writeInt(true, Byte.SIZE, (int) value);
            return;
        }
        final int size = Long.SIZE - Long.numberOfLeadingZeros(value);
        int bytes = size / 6;
        if ((size % 6) > (6 - bytes)) {
            bytes++;
        }
        output.writeInt(true, 8, (int) ((0xFE << (6 - bytes)) | (value >> (bytes * 6))));
        for (int i = (bytes - 1) * 6; i >= 0; i -= 6) {
            output.writeInt(true, 8, (int) (0x80 | ((value >> i) & 0x3F)));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected ExtendedBitOutput() {
        super();
    }
}
