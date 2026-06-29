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

import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.ADDITIONAL_1_BYTE;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.ADDITIONAL_2_BYTES;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.ADDITIONAL_4_BYTES;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.ADDITIONAL_8_BYTES;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.ADDITIONAL_DIRECT_MAX;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.ADDITIONAL_MASK;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.MAJOR_NEGATIVE_INTEGER;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.MAJOR_POSITIVE_INTEGER;
import static com.github.jinahya.bit.io.miscellaneous.CborIntConstants.MAJOR_TYPE_SHIFT;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

final class CborIntUtil {

    static long read(final BitInput input, final boolean deterministic) throws IOException {
        requireNonNullInput(input);
        final int first = input.readUnsignedInt(Byte.SIZE);
        final int major = first >>> MAJOR_TYPE_SHIFT;
        if (major != MAJOR_POSITIVE_INTEGER && major != MAJOR_NEGATIVE_INTEGER) {
            throw new IOException("not a CBOR integer major type: " + major);
        }
        final int additional = first & ADDITIONAL_MASK;
        final long argument = readArgument(input, additional);
        if (deterministic && !isShortest(additional, argument)) {
            throw new IOException("non-minimal CBOR integer");
        }
        if (major == MAJOR_POSITIVE_INTEGER) {
            return argument;
        }
        return -1L - argument;
    }

    static void write(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        if (value >= 0L) {
            writeArgument(output, MAJOR_POSITIVE_INTEGER, value);
        } else {
            writeArgument(output, MAJOR_NEGATIVE_INTEGER, -1L - value);
        }
    }

    private static long readArgument(final BitInput input, final int additional) throws IOException {
        if (additional <= ADDITIONAL_DIRECT_MAX) {
            return additional;
        }
        if (additional == ADDITIONAL_1_BYTE) {
            return input.readUnsignedInt(Byte.SIZE);
        }
        if (additional == ADDITIONAL_2_BYTES) {
            return input.readUnsignedInt(Short.SIZE);
        }
        if (additional == ADDITIONAL_4_BYTES) {
            return input.readUnsignedLong(Integer.SIZE);
        }
        if (additional == ADDITIONAL_8_BYTES) {
            final long v = input.readLong64();
            if (v < 0L) {
                throw new IOException("CBOR integer argument exceeds signed long range");
            }
            return v;
        }
        throw new IOException("invalid CBOR integer additional information: " + additional);
    }

    private static void writeArgument(final BitOutput output, final int major, final long argument) throws IOException {
        final int prefix = major << MAJOR_TYPE_SHIFT;
        if (argument <= ADDITIONAL_DIRECT_MAX) {
            output.writeUnsignedInt(Byte.SIZE, prefix | (int) argument);
        } else if (argument <= 0xFFL) {
            output.writeUnsignedInt(Byte.SIZE, prefix | ADDITIONAL_1_BYTE);
            output.writeUnsignedInt(Byte.SIZE, (int) argument);
        } else if (argument <= 0xFFFFL) {
            output.writeUnsignedInt(Byte.SIZE, prefix | ADDITIONAL_2_BYTES);
            output.writeUnsignedInt(Short.SIZE, (int) argument);
        } else if (argument <= 0xFFFFFFFFL) {
            output.writeUnsignedInt(Byte.SIZE, prefix | ADDITIONAL_4_BYTES);
            output.writeUnsignedLong(Integer.SIZE, argument);
        } else {
            output.writeUnsignedInt(Byte.SIZE, prefix | ADDITIONAL_8_BYTES);
            output.writeLong64(argument);
        }
    }

    private static boolean isShortest(final int additional, final long argument) {
        return additional <= ADDITIONAL_DIRECT_MAX
               || additional == ADDITIONAL_1_BYTE && argument > ADDITIONAL_DIRECT_MAX
               || additional == ADDITIONAL_2_BYTES && argument > 0xFFL
               || additional == ADDITIONAL_4_BYTES && argument > 0xFFFFL
               || additional == ADDITIONAL_8_BYTES && argument > 0xFFFFFFFFL;
    }

    private CborIntUtil() {
        throw new AssertionError("instantiation is not allowed");
    }
}
