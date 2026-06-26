package com.github.jinahya.bit.io.miscellaneous;

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

final class RiceGolombCodes {

    static final int MIN_PARAMETER = 0;

    static final int MAX_PARAMETER = Long.SIZE - 2;

    static final long MIN_SIGNED_VALUE = -(1L << (Long.SIZE - 2));

    static final long MAX_SIGNED_VALUE = Long.MAX_VALUE >> 1;

    static int requireParameter(final int parameter) {
        if (parameter < MIN_PARAMETER || parameter > MAX_PARAMETER) {
            throw new IllegalArgumentException("parameter out of range: " + parameter);
        }
        return parameter;
    }

    static long foldSigned(final long value) {
        if (value < MIN_SIGNED_VALUE || value > MAX_SIGNED_VALUE) {
            throw new IllegalArgumentException("value out of range: " + value);
        }
        return value < 0L ? ((-value) << 1) - 1L : value << 1;
    }

    static long unfoldSigned(final long value) {
        return (value & 0x01L) == 0L ? value >>> 1 : -((value >>> 1) + 1L);
    }

    static long readUnsigned(final BitInput input, final int parameter, final int quotientBit) throws IOException {
        requireInput(input);
        requireParameter(parameter);
        if (quotientBit != 0 && quotientBit != 1) {
            throw new IllegalArgumentException("invalid quotientBit: " + quotientBit);
        }
        long quotient = 0L;
        while (input.readInt(true, 1) == quotientBit) {
            if (quotient == Long.MAX_VALUE) {
                throw new IOException("quotient exceeds signed long range");
            }
            quotient++;
        }
        if (quotient > (Long.MAX_VALUE >> parameter)) {
            throw new IOException("value exceeds signed long range");
        }
        final long remainder = parameter == 0 ? 0L : input.readLong(true, parameter);
        return (quotient << parameter) | remainder;
    }

    static void writeUnsigned(final BitOutput output, final int parameter, final long value, final int quotientBit)
            throws IOException {
        requireOutput(output);
        requireParameter(parameter);
        if (value < 0L) {
            throw new IllegalArgumentException("negative value: " + value);
        }
        if (quotientBit != 0 && quotientBit != 1) {
            throw new IllegalArgumentException("invalid quotientBit: " + quotientBit);
        }
        final long quotient = value >>> parameter;
        writeRepeated(output, quotient, quotientBit);
        output.writeInt(true, 1, quotientBit ^ 0x01);
        if (parameter > 0) {
            output.writeLong(true, parameter, value & ((1L << parameter) - 1L));
        }
    }

    private static void requireInput(final BitInput input) {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
    }

    private static void requireOutput(final BitOutput output) {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
    }

    private static void writeRepeated(final BitOutput output, long count, final int bit) throws IOException {
        final long chunk = bit == 0 ? 0L : Long.MAX_VALUE;
        while (count >= Long.SIZE - 1) {
            output.writeLong(true, Long.SIZE - 1, chunk);
            count -= Long.SIZE - 1;
        }
        if (count > 0L) {
            output.writeLong(true, (int) count, chunk);
        }
    }

    private RiceGolombCodes() {
        throw new AssertionError("instantiation is not allowed");
    }
}
