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

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

final class CborInts {

    static long read(final BitInput input, final boolean deterministic) throws IOException {
        requireNonNullInput(input);
        final int first = input.readUnsignedInt(Byte.SIZE);
        final int major = first >>> 5;
        if (major != 0 && major != 1) {
            throw new IOException("not a CBOR integer major type: " + major);
        }
        final int additional = first & 0x1F;
        final long argument = readArgument(input, additional);
        if (deterministic && !isShortest(additional, argument)) {
            throw new IOException("non-minimal CBOR integer");
        }
        if (major == 0) {
            return argument;
        }
        return -1L - argument;
    }

    static void write(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        if (value >= 0L) {
            writeArgument(output, 0, value);
        } else {
            writeArgument(output, 1, -1L - value);
        }
    }

    private static long readArgument(final BitInput input, final int additional) throws IOException {
        if (additional < 24) {
            return additional;
        }
        if (additional == 24) {
            return input.readUnsignedInt(Byte.SIZE);
        }
        if (additional == 25) {
            return input.readUnsignedInt(Short.SIZE);
        }
        if (additional == 26) {
            return input.readUnsignedLong(Integer.SIZE);
        }
        if (additional == 27) {
            final long v = input.readLong64();
            if (v < 0L) {
                throw new IOException("CBOR integer argument exceeds signed long range");
            }
            return v;
        }
        throw new IOException("invalid CBOR integer additional information: " + additional);
    }

    private static void writeArgument(final BitOutput output, final int major, final long argument) throws IOException {
        final int prefix = major << 5;
        if (argument < 24L) {
            output.writeUnsignedInt(Byte.SIZE, prefix | (int) argument);
        } else if (argument <= 0xFFL) {
            output.writeUnsignedInt(Byte.SIZE, prefix | 24);
            output.writeUnsignedInt(Byte.SIZE, (int) argument);
        } else if (argument <= 0xFFFFL) {
            output.writeUnsignedInt(Byte.SIZE, prefix | 25);
            output.writeUnsignedInt(Short.SIZE, (int) argument);
        } else if (argument <= 0xFFFFFFFFL) {
            output.writeUnsignedInt(Byte.SIZE, prefix | 26);
            output.writeUnsignedLong(Integer.SIZE, argument);
        } else {
            output.writeUnsignedInt(Byte.SIZE, prefix | 27);
            output.writeLong64(argument);
        }
    }

    private static boolean isShortest(final int additional, final long argument) {
        return additional < 24
               || additional == 24 && argument >= 24L
               || additional == 25 && argument > 0xFFL
               || additional == 26 && argument > 0xFFFFL
               || additional == 27 && argument > 0xFFFFFFFFL;
    }

    private CborInts() {
        throw new AssertionError("instantiation is not allowed");
    }
}
