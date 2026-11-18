package com.github.jinahya.bit.io.miscellaneous.mongodb;

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
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;
import com.github.jinahya.bit.io.miscellaneous.ieee.Decimal;

import java.io.IOException;
import java.math.BigInteger;

/**
 * A codec for the BSON {@code Decimal128} type: the IEEE 754 {@code decimal128} interchange format with its coefficient
 * as an unsigned binary integer (BID), in little-endian byte order; 16 bytes.
 *
 * <p>A finite value holds up to 34 coefficient digits and an exponent between {@code -6176} and {@code 6111}, both
 * inclusive. Writing never rounds or clamps; a value out of these ranges, or a NaN whose payload has more than 33
 * digits, is rejected with an {@link IllegalArgumentException}. Reading accepts every encoding; as IEEE 754 specifies,
 * a non-canonical coefficient (greater than {@code 10^34 - 1}) reads as zero, and so does a non-canonical NaN payload.
 * </p>
 *
 * <p>Note that the BSON string form of every NaN is {@code "NaN"}, whereas a {@link Decimal} read by this codec keeps
 * the sign, the kind, and the payload of a NaN.</p>
 *
 * @see <a href="https://github.com/mongodb/specifications/blob/master/source/bson-decimal128/decimal128.md">BSON
 * Decimal128</a>
 * @see <a href="https://bsonspec.org/spec.html">BSON Specification</a>
 */
public final class BsonDecimal128
        implements BitReader<Decimal>, BitWriter<Decimal> {

    /**
     * The number of bytes of the type. The value is {@value}.
     */
    public static final int BYTES = 16;

    /**
     * The singleton instance of this codec.
     */
    public static final BsonDecimal128 INSTANCE = new BsonDecimal128();

    private static final int BIAS = 6176;

    private static final int MAX_ENCODED_EXPONENT = 12287; // 3 * 2^12 - 1

    private static final int EXPONENT_SIZE = 14;

    private static final int COEFFICIENT_SIZE = 113; // in the 00..10 combination forms

    private static final int TRAILING_SIZE = 110; // the trailing significand field

    private static final BigInteger MAX_COEFFICIENT = BigInteger.TEN.pow(34).subtract(BigInteger.ONE);

    private static final BigInteger MAX_PAYLOAD = BigInteger.TEN.pow(33).subtract(BigInteger.ONE);

    // -----------------------------------------------------------------------------------------------------------------
    private BsonDecimal128() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Decimal read(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final byte[] bytes = new byte[BYTES];
        for (int i = bytes.length - 1; i >= 0; i--) { // little-endian; the most significant byte comes last
            bytes[i] = (byte) input.readUnsignedInt(Byte.SIZE);
        }
        final BigInteger bits = new BigInteger(1, bytes); // the 128-bit string, sign bit first
        final boolean negative = bits.testBit(127);
        final int combination = bits.shiftRight(122).intValue() & 0x1F; // G0..G4
        if (combination == 0x1E) { // 11110
            return Decimal.infinity(negative);
        }
        if (combination == 0x1F) { // 11111
            final boolean signaling = bits.testBit(121);
            final BigInteger payload = bits.and(mask(TRAILING_SIZE));
            return Decimal.nan(negative, signaling, payload.compareTo(MAX_PAYLOAD) > 0 ? BigInteger.ZERO : payload);
        }
        final int encodedExponent;
        BigInteger coefficient;
        if ((combination >> 3) != 3) { // G0G1 != 11; the exponent is G0..G13
            encodedExponent = bits.shiftRight(COEFFICIENT_SIZE).intValue() & ((1 << EXPONENT_SIZE) - 1);
            coefficient = bits.and(mask(COEFFICIENT_SIZE));
        } else { // G0G1 == 11; the exponent is G2..G15, and the coefficient is 100 followed by 111 bits
            encodedExponent = bits.shiftRight(COEFFICIENT_SIZE - 2).intValue() & ((1 << EXPONENT_SIZE) - 1);
            coefficient = BigInteger.valueOf(4L).shiftLeft(COEFFICIENT_SIZE - 2)
                    .or(bits.and(mask(COEFFICIENT_SIZE - 2)));
        }
        if (coefficient.compareTo(MAX_COEFFICIENT) > 0) {
            coefficient = BigInteger.ZERO; // non-canonical
        }
        return Decimal.finite(negative, coefficient, encodedExponent - BIAS);
    }

    @Override
    public void write(final BitOutput output, final Decimal value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        BigInteger bits;
        if (value.isInfinite()) {
            bits = BigInteger.valueOf(0x1EL).shiftLeft(122);
        } else if (value.isNaN()) {
            if (value.getCoefficient().compareTo(MAX_PAYLOAD) > 0) {
                throw new IllegalArgumentException("payload(" + value.getCoefficient() + ") > " + MAX_PAYLOAD);
            }
            bits = BigInteger.valueOf(0x1FL).shiftLeft(122).or(value.getCoefficient());
            if (value.isSignaling()) {
                bits = bits.setBit(121);
            }
        } else {
            final BigInteger coefficient = value.getCoefficient();
            if (coefficient.compareTo(MAX_COEFFICIENT) > 0) {
                throw new IllegalArgumentException("coefficient(" + coefficient + ") has more than 34 digits");
            }
            final long encodedExponent = (long) value.getExponent() + BIAS;
            if (encodedExponent < 0L || encodedExponent > MAX_ENCODED_EXPONENT) {
                throw new IllegalArgumentException(
                        "exponent(" + value.getExponent() + ") out of [" + -BIAS + ", " + (MAX_ENCODED_EXPONENT - BIAS)
                        + "]");
            }
            bits = BigInteger.valueOf(encodedExponent).shiftLeft(COEFFICIENT_SIZE).or(coefficient);
        }
        if (value.isNegative()) {
            bits = bits.setBit(127);
        }
        for (int i = 0; i < BYTES; i++) { // little-endian; the least significant byte comes first
            output.writeUnsignedInt(Byte.SIZE, bits.shiftRight(i * Byte.SIZE).intValue() & 0xFF);
        }
    }

    private static BigInteger mask(final int size) {
        return BigInteger.ONE.shiftLeft(size).subtract(BigInteger.ONE);
    }
}
