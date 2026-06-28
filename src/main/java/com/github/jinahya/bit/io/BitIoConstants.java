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

/**
 * Constants for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class BitIoConstants {

    // ------------------------------------------------------------------------------------------------------------ byte
    static final int SIZE_MAX_BYTE_UNSIGNED = 7;

    // ----------------------------------------------------------------------------------------------------------- short
    static final int SIZE_MAX_SHORT_UNSIGNED = 15;

    // ------------------------------------------------------------------------------------------------------------- int
    static final int SIZE_MAX_INT_UNSIGNED = 31;

    // ------------------------------------------------------------------------------------------------------------ long
    static final int SIZE_MAX_LONG_UNSIGNED = 63;

    // -----------------------------------------------------------------------------------------------------
    // float/double

    /**
     * The minimum size, inclusive, for an exponent of a {@code float}/{@code double} reduced encoding; the smallest
     * width at which the finite-normal code range ({@code 1 .. expMask-1}) is non-empty.
     */
    static final int MIN_EXPONENT_SIZE = 2;

    /**
     * The minimum size, inclusive, for a fraction(significand) of a {@code float}/{@code double} reduced encoding; the
     * smallest width that can distinguish all three reserved {@code expMask}-exponent states (Infinity, qNaN, sNaN).
     */
    static final int MIN_FRACTION_SIZE = 2;

    /**
     * The maximum size, inclusive, for an exponent of a {@code binary16} (half) reduced encoding; the native
     * {@code binary16} exponent width.
     */
    static final int MAX_EXPONENT_SIZE_HALF = 5;

    /**
     * The maximum size, inclusive, for a fraction of a {@code binary16} (half) reduced encoding; the native
     * {@code binary16} fraction width.
     */
    static final int MAX_FRACTION_SIZE_HALF = 10;

    /**
     * The maximum size, inclusive, for an exponent of a {@code float} reduced encoding; the native {@code float}
     * exponent width.
     */
    static final int MAX_EXPONENT_SIZE_FLOAT = 8;

    /**
     * The maximum size, inclusive, for a fraction of a {@code float} reduced encoding; the native {@code float}
     * fraction width.
     */
    static final int MAX_FRACTION_SIZE_FLOAT = 23;

    /**
     * The maximum size, inclusive, for an exponent of a {@code double} reduced encoding; the native {@code double}
     * exponent width.
     */
    static final int MAX_EXPONENT_SIZE_DOUBLE = 11;

    /**
     * The maximum size, inclusive, for a fraction of a {@code double} reduced encoding; the native {@code double}
     * fraction width.
     */
    static final int MAX_FRACTION_SIZE_DOUBLE = 52;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The size, in bits, of a single unsigned flag bit. Shared by the I/O layer ({@code boolean} values and the sign
     * bit of signed/floating-point values) and the reader/writer layer (the presence flag of
     * {@link FilterBitReader#nullable(BitReader) nullable} readers/writers).
     *
     * @see AbstractBitInput#readBoolean()
     * @see AbstractBitOutput#writeBoolean(boolean)
     * @see FilterBitReader#nullable(BitReader)
     * @see FilterBitWriter#nullable(BitWriter)
     */
    static final int FLAG_SIZE = 1;

    // -----------------------------------------------------------------------------------------------------------------
    /**
     * The name of the {@code US-ASCII} charset; the charset used for compressed
     * ({@value java.lang.Byte#SIZE}{@code  - 1}-bit element) ASCII strings.
     */
    static final String US_ASCII = "US-ASCII";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BitIoConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
