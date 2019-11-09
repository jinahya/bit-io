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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_BYTE;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_INTEGER;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_LONG;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_SHORT;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

class ExtendedBitInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code boolean} value and determines the following object is {@code null}.
     *
     * @param input a bit input.
     * @return {@code true} when determined following object is {@code null}; {@code false} otherwise.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeBooleanIsNextNull(BitOutput, Object)
     */
    public static boolean readBooleanIsNextNull(final BitInput input) throws IOException {
        return !input.readBoolean(); // 0b0 -> null
    }

    // -----------------------------------------------------------------------------------------------------------------
    static int readUnsignedVariableInt(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        int value = 0;
        boolean next = true;
        for (int size = 1, shift = 0; size < 9 && next; size++) {
            next = input.readBoolean();
            value = (input.readInt(true, size) << shift) | value;
            shift += size;
        }
        if (next) {
            throw new IOException("no signal for the last group");
        }
        assert value >= 0;
        return value;
    }

    static long readUnsignedVariableLong(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        long value = 0L;
        boolean next = true;
        for (int size = 1, shift = 0; size < 12 && next; size++) {
            next = input.readBoolean();
            value = (input.readLong(true, size) << shift) | value;
            shift += size;
        }
        if (next) {
            throw new IOException("no signal for the last group");
        }
        assert value >= 0L;
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static byte readUnsignedVariable3(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, SIZE_EXPONENT_BYTE);
        if (!extended) {
            return (byte) size;
        }
        return input.readByte(true, size);
    }

    static short readUnsignedVariable4(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, SIZE_EXPONENT_SHORT);
        if (!extended) {
            return (short) size;
        }
        return input.readShort(true, size);
    }

    /**
     * Reads an unsigned {@code int} as a variable format.
     * <p>
     * The bit stream contains following values.
     * <ul>
     * <li>a {@code 1}-bit {@code boolean} flag indicating an {@code extended} type.</li>
     * <li>a {@code 5}-bit unsigned {@code int} for {@code size} which means,
     * <ul>
     * <li>when {@code extended} flag is {@code false}, this value is the original value.</li>
     * <li>when {@code extended} flag is {@code true}, this value is the number of bits of the following unsigned {@code int}.</li>
     * </ul>
     * <li>(optionally) a {@code size}-bit {@code int} of original value.</li>
     * </ul>
     *
     * @param input a bit input.
     * @return an unsigned {@code int} value.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeUnsignedVariable5(BitOutput, int)
     */
    static int readUnsignedVariable5(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, SIZE_EXPONENT_INTEGER);
        if (!extended) {
            return size;
        }
        return input.readInt(true, size);
    }

    /**
     * Reads an unsigned {@code long} value as a variable format.
     * <p>
     * The bit stream contains following values.
     * <ul>
     * <li>a {@code 1}-bit {@code boolean} flag indicating an {@code extended} type.</li>
     * <li>a {@code 6}-bit unsigned {@code int} for {@code size} which means,
     * <ul>
     * <li>when {@code extended} flag is {@code false}, this value is the original value.</li>
     * <li>when {@code extended} flag is {@code true}, this value is the number of bits of the following unsigned {@code long}.</li>
     * </ul>
     * <li>(optionally) a {@code size}-bit {@code long} of original value.</li>
     * </ul>
     *
     * @param input a bit input.
     * @return an unsigned {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeUnsignedVariable6(BitOutput, long)
     */
    static long readUnsignedVariable6(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, SIZE_EXPONENT_LONG);
        if (!extended) {
            return size;
        }
        return input.readLong(true, size);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an unsigned {@code int} for a length from specified bit input.
     *
     * @param input the bit input.
     * @return an unsigned {@code int} for a length.
     * @throws IOException if an I/O error occurs.
     * @see #readUnsignedVariable5(BitInput)
     * @see #readLengthLong(BitInput)
     * @see ExtendedBitOutput#writeLengthInt(BitOutput, int)
     */
    static int readLengthInt(final BitInput input) throws IOException {
        return readUnsignedVariable5(input);
    }

    static long readLengthLong(final BitInput input) throws IOException {
        return readUnsignedVariable6(input);
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
        final byte[] value = new byte[readLengthInt(input)];
        for (int i = 0; i < value.length; i++) {
            value[i] = input.readByte(unsigned, size);
        }
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static String readString(final boolean nullable, final BitInput input, final String charset)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (charset == null) {
            throw new NullPointerException("charset is null");
        }
        final byte[] bytes = readBytes(nullable, input, false, 8);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an ASCII string, which each byte is a {@code 7}-bit unsigned integer, from specified bit input.
     *
     * @param nullable a flag for nullability.
     * @param input    the bit input.
     * @return an ASCII string.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeAscii(boolean, BitOutput, String)
     */
    public static String readAscii(final boolean nullable, final BitInput input) throws IOException {
        final byte[] bytes = readBytes(nullable, input, true, 7);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, "US-ASCII");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a VLQ of an {@code int}.
     *
     * @param input a byte input to read value.
     * @param size  a value for the number of bits for a single group without the continuation-signal bit.
     * @return an {@code int} value of VLQ.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeVariableLengthQuantityInt(BitOutput, int, int)
     */
    static int readVariableLengthQuantityInt(final BitInput input, final int size) throws IOException {
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
            throw new IOException("no signal for the last group");
        }
        return value;
    }

    /**
     * Reads a VLQ of a {@code long}.
     *
     * @param input a byte input to read value.
     * @param size  a value for the number of bits for a single group without the continuation-signal bit.
     * @return a {@code long} value of VLQ.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeVariableLengthQuantityLong(BitOutput, int, long)
     */
    static long readVariableLengthQuantityLong(final BitInput input, final int size) throws IOException {
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
            throw new IOException("no signal for the last group");
        }
        return value;
    }

    // ---------------------------------------------------------------------------------------------------------- object

    /**
     * Reads an object.
     *
     * @param nullable a flag for nullability.
     * @param input    a bit input.
     * @param reader   a bit reader.
     * @param <T>      object type parameter
     * @return an object.
     * @throws IOException if an I/O error occurs.
     * @see #readObjects(boolean, BitInput, BitReader)
     * @see ExtendedBitOutput#writeObject(boolean, BitOutput, BitWriter, Object)
     */
    static <T> T readObject(final boolean nullable, final BitInput input, final BitReader<? extends T> reader)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        if (nullable && readBooleanIsNextNull(input)) {
            return null;
        }
        return reader.read(input);
    }

    /**
     * Reads multiple objects and add them to specified collection.
     *
     * @param input      a bit input.
     * @param reader     a bit reader for reading each object.
     * @param collection the collection to which objects are added.
     * @param <T>        object type parameter
     * @param <U>        collection type parameter
     * @return given collection.
     * @throws IOException if an I/O error occurs.
     * @see #readObject(boolean, BitInput, BitReader)
     * @see #readObjects(boolean, BitInput, BitReader)
     * @see ExtendedBitOutput#writeObjects(BitOutput, BitWriter, Collection)
     */
    static <T, U extends Collection<? super T>> U readObjects(final BitInput input, final BitReader<? extends T> reader,
                                                              final U collection)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        if (collection == null) {
            throw new NullPointerException("value is null");
        }
        final int size = readLengthInt(input);
        for (int i = 0; i < size; i++) {
            collection.add(readObject(true, input, reader));
        }
        return collection;
    }

    /**
     * Reads objects.
     *
     * @param nullable a flag for nullability.
     * @param input    a bit input.
     * @param reader   a bit reader.
     * @param <T>      object type parameter
     * @return a list of objects.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeObjects(boolean, BitOutput, BitWriter, List)
     */
    static <T> List<T> readObjects(final boolean nullable, final BitInput input, final BitReader<? extends T> reader)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        if (nullable && readBooleanIsNextNull(input)) {
            return null;
        }
        if (true) {
            return readObjects(input, reader, new ArrayList<T>());
        }
        final int size = readLengthInt(input);
        final List<T> value = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            value.add(readObject(true, input, reader));
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------------ utf8
    static long readUtf8(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        long value = input.readInt(true, Byte.SIZE);
        if (value <= 0x7FL) { // 0b01111111L
            return value;
        }
        final int bytes = Integer.numberOfLeadingZeros(~((byte) value)) - 24;
        value &= (0x7FL >> bytes);
        for (int i = 1; i < bytes; i++) {
            value <<= 6;
            value |= input.readInt(true, 8) & 0x3F;
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
