package com.github.jinahya.bit.io.miscellaneous.ieee;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;

import java.io.IOException;
import java.math.BigInteger;

/**
 * A decimal interchange format with its coefficient in Densely Packed Decimal (DPD): a sign bit, a 5-bit combination
 * field, an exponent continuation, and a coefficient continuation of 10-bit declets, each of three digits.
 *
 * @see <a href="https://speleotrove.com/decimal/dbspec.html">Decimal Encoding Specification</a>
 * @see <a href="https://speleotrove.com/decimal/DPDecimal.html">A Summary of Densely Packed Decimal encoding</a>
 */
final class DecimalDpdFormat {

    private static final int COMBINATION_SIZE = 5;

    private static final int COMBINATION_INFINITY = 0x1E; // 11110

    private static final int COMBINATION_NAN = 0x1F; // 11111

    private static final int DECLET_SIZE = 10;

    private static final int DECLET_DIGITS = 3;

    /**
     * The canonical declet of each value of three digits; {@code 0} through {@code 999}.
     */
    private static final int[] DECLETS = new int[1000];

    /**
     * The value of three digits of each declet, including the non-canonical ones.
     */
    private static final int[] DIGITS = new int[1 << DECLET_SIZE];

    static {
        for (int value = 0; value < DECLETS.length; value++) {
            DECLETS[value] = compress(value / 100, value / 10 % 10, value % 10);
        }
        for (int declet = 0; declet < DIGITS.length; declet++) {
            DIGITS[declet] = expand(declet);
        }
    }

    /**
     * Compresses three digits into a declet, as the compression table of the DPD summary defines.
     *
     * @param d1 the most significant digit, {@code abcd}.
     * @param d2 the middle digit, {@code efgh}.
     * @param d3 the least significant digit, {@code ijkm}.
     * @return the declet, {@code pqr stu v wxy}.
     */
    private static int compress(final int d1, final int d2, final int d3) {
        final int a = d1 >> 3;
        final int bcd = d1 & 7;
        final int d = d1 & 1;
        final int e = d2 >> 3;
        final int fgh = d2 & 7;
        final int fg = fgh >> 1;
        final int h = d2 & 1;
        final int i = d3 >> 3;
        final int jkm = d3 & 7;
        final int jk = jkm >> 1;
        final int m = d3 & 1;
        switch ((a << 2) | (e << 1) | i) {
            case 0: // 000: bcd fgh 0 jkm
                return (bcd << 7) | (fgh << 4) | jkm;
            case 1: // 001: bcd fgh 1 00m
                return (bcd << 7) | (fgh << 4) | 0x8 | m;
            case 2: // 010: bcd jkh 1 01m
                return (bcd << 7) | (jk << 5) | (h << 4) | 0xA | m;
            case 4: // 100: jkd fgh 1 10m
                return (jk << 8) | (d << 7) | (fgh << 4) | 0xC | m;
            case 6: // 110: jkd 00h 1 11m
                return (jk << 8) | (d << 7) | (h << 4) | 0xE | m;
            case 5: // 101: fgd 01h 1 11m
                return (fg << 8) | (d << 7) | (0x1 << 5) | (h << 4) | 0xE | m;
            case 3: // 011: bcd 10h 1 11m
                return (bcd << 7) | (0x2 << 5) | (h << 4) | 0xE | m;
            default: // 111: 00d 11h 1 11m
                return (d << 7) | (0x3 << 5) | (h << 4) | 0xE | m;
        }
    }

    /**
     * Expands a declet into the value of its three digits, as the expansion table of the DPD summary defines.
     *
     * @param declet the declet, {@code pqr stu v wxy}.
     * @return the value of the three digits.
     */
    private static int expand(final int declet) {
        final int pqr = (declet >> 7) & 7;
        final int pq = pqr >> 1;
        final int r = pqr & 1;
        final int stu = (declet >> 4) & 7;
        final int st = stu >> 1;
        final int u = stu & 1;
        final int v = (declet >> 3) & 1;
        final int wxy = declet & 7;
        final int wx = wxy >> 1;
        final int y = wxy & 1;
        final int d1;
        final int d2;
        final int d3;
        if (v == 0) { // 0....: 0pqr 0stu 0wxy
            d1 = pqr;
            d2 = stu;
            d3 = wxy;
        } else if (wx == 0) { // 100..: 0pqr 0stu 100y
            d1 = pqr;
            d2 = stu;
            d3 = 8 | y;
        } else if (wx == 1) { // 101..: 0pqr 100u 0sty
            d1 = pqr;
            d2 = 8 | u;
            d3 = (st << 1) | y;
        } else if (wx == 2) { // 110..: 100r 0stu 0pqy
            d1 = 8 | r;
            d2 = stu;
            d3 = (pq << 1) | y;
        } else if (st == 0) { // 11100: 100r 100u 0pqy
            d1 = 8 | r;
            d2 = 8 | u;
            d3 = (pq << 1) | y;
        } else if (st == 1) { // 11101: 100r 0pqu 100y
            d1 = 8 | r;
            d2 = (pq << 1) | u;
            d3 = 8 | y;
        } else if (st == 2) { // 11110: 0pqr 100u 100y
            d1 = pqr;
            d2 = 8 | u;
            d3 = 8 | y;
        } else { // 11111: 100r 100u 100y
            d1 = 8 | r;
            d2 = 8 | u;
            d3 = 8 | y;
        }
        return d1 * 100 + d2 * 10 + d3;
    }

    /**
     * Returns the canonical declet of specified value of three digits.
     *
     * @param value the value; between {@code 0} and {@code 999}, both inclusive.
     * @return the canonical declet of {@code value}.
     */
    static int declet(final int value) {
        return DECLETS[value];
    }

    /**
     * Returns the value of three digits of specified declet.
     *
     * @param declet the declet; between {@code 0} and {@code 1023}, both inclusive.
     * @return the value of {@code declet}.
     */
    static int digits(final int declet) {
        return DIGITS[declet];
    }

    // -----------------------------------------------------------------------------------------------------------------
    DecimalDpdFormat(final int exponentContinuationSize, final int digits, final int bias) {
        super();
        this.exponentContinuationSize = exponentContinuationSize;
        this.digits = digits;
        this.bias = bias;
        this.declets = (digits - 1) / DECLET_DIGITS;
        this.maxEncodedExponent = 3 * (1 << exponentContinuationSize) - 1;
        this.maxCoefficient = BigInteger.TEN.pow(digits).subtract(BigInteger.ONE);
        this.maxPayload = BigInteger.TEN.pow(digits - 1).subtract(BigInteger.ONE);
        this.msdUnit = BigInteger.TEN.pow(digits - 1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    Decimal read(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final boolean negative = input.readBoolean();
        final int combination = input.readUnsignedInt(COMBINATION_SIZE);
        final int continuation = input.readUnsignedInt(exponentContinuationSize);
        final BigInteger trailing = readTrailing(input);
        if (combination == COMBINATION_INFINITY) {
            return Decimal.infinity(negative);
        }
        if (combination == COMBINATION_NAN) {
            final boolean signaling = (continuation >> (exponentContinuationSize - 1)) == 1;
            return Decimal.nan(negative, signaling, trailing);
        }
        final int exponentMsbs;
        final int msd;
        if ((combination >> 3) != 3) { // ab cde
            exponentMsbs = combination >> 3;
            msd = combination & 7;
        } else { // 11 cd e
            exponentMsbs = (combination >> 1) & 3;
            msd = 8 | (combination & 1);
        }
        final int encodedExponent = (exponentMsbs << exponentContinuationSize) | continuation;
        final BigInteger coefficient = msdUnit.multiply(BigInteger.valueOf(msd)).add(trailing);
        return Decimal.finite(negative, coefficient, encodedExponent - bias);
    }

    private BigInteger readTrailing(final BitInput input) throws IOException {
        BigInteger value = BigInteger.ZERO;
        final BigInteger thousand = BigInteger.valueOf(1000L);
        for (int i = 0; i < declets; i++) {
            value = value.multiply(thousand).add(BigInteger.valueOf(digits(input.readUnsignedInt(DECLET_SIZE))));
        }
        return value;
    }

    void write(final BitOutput output, final Decimal value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (value.isInfinite()) {
            output.writeBoolean(value.isNegative());
            output.writeUnsignedInt(COMBINATION_SIZE, COMBINATION_INFINITY);
            output.writeUnsignedInt(exponentContinuationSize, 0);
            writeTrailing(output, BigInteger.ZERO);
            return;
        }
        if (value.isNaN()) {
            if (value.getCoefficient().compareTo(maxPayload) > 0) {
                throw new IllegalArgumentException("payload(" + value.getCoefficient() + ") > " + maxPayload);
            }
            output.writeBoolean(value.isNegative());
            output.writeUnsignedInt(COMBINATION_SIZE, COMBINATION_NAN);
            output.writeUnsignedInt(exponentContinuationSize,
                                    value.isSignaling() ? 1 << (exponentContinuationSize - 1) : 0);
            writeTrailing(output, value.getCoefficient());
            return;
        }
        final BigInteger coefficient = value.getCoefficient();
        if (coefficient.compareTo(maxCoefficient) > 0) {
            throw new IllegalArgumentException(
                    "coefficient(" + coefficient + ") has more than " + digits + " digits");
        }
        final long encodedExponent = (long) value.getExponent() + bias;
        if (encodedExponent < 0L || encodedExponent > maxEncodedExponent) {
            throw new IllegalArgumentException(
                    "exponent(" + value.getExponent() + ") out of [" + -bias + ", " + (maxEncodedExponent - bias)
                    + "]");
        }
        final BigInteger[] msdAndTrailing = coefficient.divideAndRemainder(msdUnit);
        final int msd = msdAndTrailing[0].intValue();
        final int exponentMsbs = (int) (encodedExponent >> exponentContinuationSize);
        final int combination = msd < 8
                                ? (exponentMsbs << 3) | msd
                                : (3 << 3) | (exponentMsbs << 1) | (msd & 1);
        output.writeBoolean(value.isNegative());
        output.writeUnsignedInt(COMBINATION_SIZE, combination);
        output.writeUnsignedInt(exponentContinuationSize,
                                (int) (encodedExponent & ((1 << exponentContinuationSize) - 1)));
        writeTrailing(output, msdAndTrailing[1]);
    }

    private void writeTrailing(final BitOutput output, final BigInteger value) throws IOException {
        final int[] groups = new int[declets];
        BigInteger remaining = value;
        final BigInteger thousand = BigInteger.valueOf(1000L);
        for (int i = declets - 1; i >= 0; i--) {
            final BigInteger[] qr = remaining.divideAndRemainder(thousand);
            groups[i] = qr[1].intValue();
            remaining = qr[0];
        }
        for (final int group : groups) {
            output.writeUnsignedInt(DECLET_SIZE, declet(group));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final int exponentContinuationSize;

    private final int digits;

    private final int bias;

    private final int declets;

    private final int maxEncodedExponent;

    private final BigInteger maxCoefficient;

    private final BigInteger maxPayload;

    private final BigInteger msdUnit;
}
