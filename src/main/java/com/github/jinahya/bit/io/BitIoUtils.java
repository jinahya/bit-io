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

import static com.github.jinahya.bit.io.BitIoConstants.MAX_EXPONENT_SIZE_DOUBLE;
import static com.github.jinahya.bit.io.BitIoConstants.MAX_EXPONENT_SIZE_FLOAT;
import static com.github.jinahya.bit.io.BitIoConstants.MAX_EXPONENT_SIZE_HALF;
import static com.github.jinahya.bit.io.BitIoConstants.MAX_FRACTION_SIZE_DOUBLE;
import static com.github.jinahya.bit.io.BitIoConstants.MAX_FRACTION_SIZE_FLOAT;
import static com.github.jinahya.bit.io.BitIoConstants.MAX_FRACTION_SIZE_HALF;
import static com.github.jinahya.bit.io.BitIoConstants.MIN_EXPONENT_SIZE;

/**
 * Internal utilities shared by the reader/writer layer.
 */
public final class BitIoUtils {

    /**
     * Reads a single-bit presence flag from specified input, as a {@link BitInput#readBoolean() boolean}.
     *
     * @param input the input from which the flag is read; must not be {@code null}.
     * @return {@code true} if a value is present and follows (flag set); {@code false} if the value is {@code null}
     *         (flag clear).
     * @throws NullPointerException if {@code input} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    static boolean readPresenceFlag(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        return input.readBoolean();
    }

    /**
     * Writes a single-bit presence flag for specified value to specified output, as a
     * {@link BitOutput#writeBoolean(boolean) boolean}.
     *
     * @param output the output to which the flag is written; must not be {@code null}.
     * @param value  the value whose presence the flag represents; may be {@code null}.
     * @return {@code true} if {@code value} is present (non-{@code null}) and should be written; {@code false}
     *         otherwise.
     * @throws NullPointerException if {@code output} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    static boolean writePresenceFlag(final BitOutput output, final Object value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        final boolean present = value != null;
        output.writeBoolean(present);
        return present;
    }

    /**
     * Returns the zero-based index of the highest set bit in specified value. A {@code 1.4}-safe replacement for
     * {@code 31 - }{@link Integer#numberOfLeadingZeros(int)} ({@code Integer.numberOfLeadingZeros} is a Java {@code 5}
     * method that the retro-translated {@code 1.3}/{@code 1.4} artifacts cannot rely on).
     *
     * @param value the value.
     * @return the index of the highest set bit, between {@code 0} and {@code 31}, both inclusive; {@code -1} when
     *         {@code value} is {@code 0}.
     */
    public static int highestOneBitIndex(final int value) {
        int index = -1;
        for (int v = value; v != 0; v >>>= 1) {
            index++;
        }
        return index;
    }

    /**
     * Returns the zero-based index of the highest set bit in specified value. A {@code 1.4}-safe replacement for
     * {@code 63 - }{@link Long#numberOfLeadingZeros(long)} ({@code Long.numberOfLeadingZeros} is a Java {@code 5}
     * method that the retro-translated {@code 1.3}/{@code 1.4} artifacts cannot rely on).
     *
     * @param value the value.
     * @return the index of the highest set bit, between {@code 0} and {@code 63}, both inclusive; {@code -1} when
     *         {@code value} is {@code 0}.
     */
    public static int highestOneBitIndex(final long value) {
        int index = -1;
        for (long v = value; v != 0L; v >>>= 1) {
            index++;
        }
        return index;
    }

    private BitIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }

    /**
     * Checks that the specified bit size is valid for a {@code byte} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value Byte#SIZE} - (unsigned ? {@code 1} :
     *                 {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeByte(final boolean unsigned, final int size) {
        return unsigned ? requireValidSizeForUnsignedByte(size) : requireValidSizeForSignedByte(size);
    }

    /**
     * Checks that the specified bit size is valid for an <em>unsigned</em> {@code byte} value.
     *
     * @param size the bit size to check; between {@code 1} and ({@value Byte#SIZE} - {@code 1}), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeForUnsignedByte(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size >= Byte.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned byte >= " + Byte.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a <em>signed</em> {@code byte} value.
     *
     * @param size the bit size to check; between {@code 1} and {@value Byte#SIZE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    public static int requireValidSizeForSignedByte(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Byte.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for signed byte > " + Byte.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a {@code short} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value Short#SIZE} - (unsigned ? {@code 1} :
     *                 {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeShort(final boolean unsigned, final int size) {
        return unsigned ? requireValidSizeForUnsignedShort(size) : requireValidSizeForSignedShort(size);
    }

    /**
     * Checks that the specified bit size is valid for an <em>unsigned</em> {@code short} value.
     *
     * @param size the bit size to check; between {@code 1} and ({@value Short#SIZE} - {@code 1}), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeForUnsignedShort(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size >= Short.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned short >= " + Short.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a <em>signed</em> {@code short} value.
     *
     * @param size the bit size to check; between {@code 1} and {@value Short#SIZE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeForSignedShort(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Short.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for signed short > " + Short.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for an <em>unsigned</em> {@code int} value.
     *
     * @param size the bit size to check; between {@code 1} and ({@value Integer#SIZE} - {@code 1}), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    public static int requireValidSizeForUnsignedInt(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size >= Integer.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned int >= " + Integer.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a <em>signed</em> {@code int} value.
     *
     * @param size the bit size to check; between {@code 1} and {@value Integer#SIZE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeForSignedInt(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Integer.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for signed int > " + Integer.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for an {@code int} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value Integer#SIZE} - (unsigned ? {@code 1} :
     *                 {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeInt(final boolean unsigned, final int size) {
        return unsigned ? requireValidSizeForUnsignedInt(size) : requireValidSizeForSignedInt(size);
    }

    /**
     * Checks that the specified bit size is valid for a {@code long} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value Long#SIZE} - (unsigned ? {@code 1} :
     *                 {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeLong(final boolean unsigned, final int size) {
        return unsigned ? requireValidSizeForUnsignedLong(size) : requireValidSizeForSignedLong(size);
    }

    /**
     * Checks that the specified bit size is valid for an <em>unsigned</em> {@code long} value.
     *
     * @param size the bit size to check; between {@code 1} and ({@value Long#SIZE} - {@code 1}), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeForUnsignedLong(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size >= Long.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned long >= " + Long.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a <em>signed</em> {@code long} value.
     *
     * @param size the bit size to check; between {@code 1} and {@value Long#SIZE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeForSignedLong(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Long.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for signed long > " + Long.SIZE);
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a {@code char} value.
     *
     * @param size the bit size to check; between {@code 1} and {@value Character#SIZE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeChar(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Character.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") > " + Character.SIZE);
        }
        return size;
    }

    private static int requireValidSizeInRange(final int size, final int max, final String name) {
        if (size < MIN_EXPONENT_SIZE) { // MIN_EXPONENT_SIZE == MIN_FRACTION_SIZE == 2
            throw new IllegalArgumentException("invalid " + name + "(" + size + ") < " + MIN_EXPONENT_SIZE);
        }
        if (size > max) {
            throw new IllegalArgumentException("invalid " + name + "(" + size + ") > " + max);
        }
        return size;
    }

    /**
     * Checks that the specified exponent size is valid for a {@code binary16} (half) reduced encoding.
     *
     * @param size the exponent size to check; between {@value BitIoConstants#MIN_EXPONENT_SIZE} and
     *             {@value BitIoConstants#MAX_EXPONENT_SIZE_HALF}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidExponentSizeHalf(final int size) {
        return requireValidSizeInRange(size, MAX_EXPONENT_SIZE_HALF, "exponentSize");
    }

    /**
     * Checks that the specified fraction size is valid for a {@code binary16} (half) reduced encoding.
     *
     * @param size the fraction size to check; between {@value BitIoConstants#MIN_FRACTION_SIZE} and
     *             {@value BitIoConstants#MAX_FRACTION_SIZE_HALF}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidFractionSizeHalf(final int size) {
        return requireValidSizeInRange(size, MAX_FRACTION_SIZE_HALF, "fractionSize");
    }

    /**
     * Checks that the specified exponent size is valid for a {@code float} reduced encoding.
     *
     * @param size the exponent size to check; between {@value BitIoConstants#MIN_EXPONENT_SIZE} and
     *             {@value BitIoConstants#MAX_EXPONENT_SIZE_FLOAT}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidExponentSizeFloat(final int size) {
        return requireValidSizeInRange(size, MAX_EXPONENT_SIZE_FLOAT, "exponentSize");
    }

    /**
     * Checks that the specified fraction size is valid for a {@code float} reduced encoding.
     *
     * @param size the fraction size to check; between {@value BitIoConstants#MIN_FRACTION_SIZE} and
     *             {@value BitIoConstants#MAX_FRACTION_SIZE_FLOAT}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidFractionSizeFloat(final int size) {
        return requireValidSizeInRange(size, MAX_FRACTION_SIZE_FLOAT, "fractionSize");
    }

    /**
     * Checks that the specified exponent size is valid for a {@code double} reduced encoding.
     *
     * @param size the exponent size to check; between {@value BitIoConstants#MIN_EXPONENT_SIZE} and
     *             {@value BitIoConstants#MAX_EXPONENT_SIZE_DOUBLE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidExponentSizeDouble(final int size) {
        return requireValidSizeInRange(size, MAX_EXPONENT_SIZE_DOUBLE, "exponentSize");
    }

    /**
     * Checks that the specified fraction size is valid for a {@code double} reduced encoding.
     *
     * @param size the fraction size to check; between {@value BitIoConstants#MIN_FRACTION_SIZE} and
     *             {@value BitIoConstants#MAX_FRACTION_SIZE_DOUBLE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidFractionSizeDouble(final int size) {
        return requireValidSizeInRange(size, MAX_FRACTION_SIZE_DOUBLE, "fractionSize");
    }
}
