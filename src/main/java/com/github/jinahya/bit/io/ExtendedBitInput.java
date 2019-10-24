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
import java.nio.charset.Charset;

import static com.github.jinahya.bit.io.BitIoConstants.MAX_EXPONENT_INTEGER;
import static com.github.jinahya.bit.io.BitIoConstants.MAX_EXPONENT_LONG;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

public class ExtendedBitInput {

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
     * @see ExtendedBitOutput#writeUnsignedIntVariable(BitOutput, int)
     */
    protected static int readUnsignedIntVariable(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, MAX_EXPONENT_INTEGER);
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
     * @see ExtendedBitOutput#writeUnsignedLongVariable(BitOutput, long)
     */
    protected static long readUnsignedLongVariable(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean extended = input.readBoolean();
        final int size = input.readInt(true, MAX_EXPONENT_LONG);
        if (!extended) {
            return size;
        }
        return input.readLong(true, size);
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
    public static String readString(final boolean nullable, final BitInput input, final Charset charset)
            throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final byte[] bytes = readBytes(nullable, input, false, 8);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an ascii string from specified bit input.
     *
     * @param nullable a flag for nullability.
     * @param input    the bit input.
     * @return an ascii string.
     * @throws IOException if an I/O error occurs.
     * @see ExtendedBitOutput#writeAscii(boolean, BitOutput, String)
     */
    public static String readAscii(final boolean nullable, final BitInput input) throws IOException {
        final byte[] bytes = readBytes(nullable, input, true, 7);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, Charset.forName("US-ASCII"));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a VLQ of an {@code int}.
     *
     * @param input a byte input to read value.
     * @param size  a value for the number of bits for a single group without the continuation-signal bit.
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
     * @param size  a value for the number of bits for a single group without the continuation-signal bit.
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
