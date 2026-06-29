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

import static com.github.jinahya.bit.io.miscellaneous.Asn1Base128Constants.GROUP_SIZE;
import static com.github.jinahya.bit.io.miscellaneous.Asn1Base128Constants.MASK_CONTINUATION;
import static com.github.jinahya.bit.io.miscellaneous.Asn1Base128Constants.MASK_VALUE;
import static com.github.jinahya.bit.io.miscellaneous.Asn1Base128Constants.MAX_OCTETS_FOR_LONG;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

final class Asn1Base128Util {

    static long read(final BitInput input, final boolean minimal) throws IOException {
        requireNonNullInput(input);
        long value = 0L;
        for (int i = 0; i < MAX_OCTETS_FOR_LONG; i++) {
            final int octet = input.readUnsignedInt(Byte.SIZE);
            if (minimal && i == 0 && (octet & MASK_CONTINUATION) != 0 && (octet & MASK_VALUE) == 0) {
                throw new IOException("non-minimal ASN.1 base-128 value");
            }
            if (value > (Long.MAX_VALUE >>> GROUP_SIZE)) {
                throw new IOException("ASN.1 base-128 value exceeds signed long range");
            }
            value = (value << GROUP_SIZE) | (octet & MASK_VALUE);
            if ((octet & MASK_CONTINUATION) == 0) {
                return value;
            }
        }
        throw new IOException("unterminated ASN.1 base-128 value");
    }

    static void write(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        if (value < 0L) {
            throw new IllegalArgumentException("negative ASN.1 base-128 value: " + value);
        }
        int shift = 63;
        while (shift > 0 && ((value >>> shift) & MASK_VALUE) == 0L) {
            shift -= GROUP_SIZE;
        }
        for (; shift > 0; shift -= GROUP_SIZE) {
            output.writeUnsignedInt(Byte.SIZE, (int) ((value >>> shift) & MASK_VALUE) | MASK_CONTINUATION);
        }
        output.writeUnsignedInt(Byte.SIZE, (int) value & MASK_VALUE);
    }

    private Asn1Base128Util() {
        throw new AssertionError("instantiation is not allowed");
    }
}
