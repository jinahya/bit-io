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
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * Utilities for ASN.1 OBJECT IDENTIFIER content octets.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1Oid {

    public static long[] readContent(final BitInput input, final int length) throws IOException {
        requireNonNullInput(input);
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }
        final byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = input.readByte8();
        }
        final int[] index = {0};
        final long firstSubidentifier = readSubidentifier(bytes, index);
        final List<Long> values = new ArrayList<Long>();
        if (firstSubidentifier < 40L) {
            values.add(Long.valueOf(0L));
            values.add(Long.valueOf(firstSubidentifier));
        } else if (firstSubidentifier < 80L) {
            values.add(Long.valueOf(1L));
            values.add(Long.valueOf(firstSubidentifier - 40L));
        } else {
            values.add(Long.valueOf(2L));
            values.add(Long.valueOf(firstSubidentifier - 80L));
        }
        while (index[0] < bytes.length) {
            values.add(Long.valueOf(readSubidentifier(bytes, index)));
        }
        final long[] arcs = new long[values.size()];
        for (int i = 0; i < arcs.length; i++) {
            arcs[i] = values.get(i).longValue();
        }
        return arcs;
    }

    public static void writeContent(final BitOutput output, final long[] value) throws IOException {
        requireNonNullOutput(output);
        requireNonNullValue(value);
        if (value.length < 2) {
            throw new IllegalArgumentException("OBJECT IDENTIFIER must have at least two arcs");
        }
        final long arc0 = value[0];
        final long arc1 = value[1];
        if (arc0 < 0L || arc0 > 2L) {
            throw new IllegalArgumentException("invalid first OBJECT IDENTIFIER arc: " + arc0);
        }
        if (arc1 < 0L || (arc0 < 2L && arc1 > 39L)) {
            throw new IllegalArgumentException("invalid second OBJECT IDENTIFIER arc: " + arc1);
        }
        Asn1Base128s.write(output, arc0 * 40L + arc1);
        for (int i = 2; i < value.length; i++) {
            Asn1Base128s.write(output, value[i]);
        }
    }

    private static long readSubidentifier(final byte[] bytes, final int[] index) throws IOException {
        long value = 0L;
        for (int i = 0; index[0] < bytes.length; i++) {
            final int octet = bytes[index[0]++] & 0xFF;
            if (i == 0 && (octet & 0x80) != 0 && (octet & 0x7F) == 0) {
                throw new IOException("non-minimal OBJECT IDENTIFIER subidentifier");
            }
            if (value > (Long.MAX_VALUE >>> 7)) {
                throw new IOException("OBJECT IDENTIFIER subidentifier exceeds signed long range");
            }
            value = (value << 7) | (octet & 0x7F);
            if ((octet & 0x80) == 0) {
                return value;
            }
        }
        throw new IOException("unterminated OBJECT IDENTIFIER subidentifier");
    }

    private Asn1Oid() {
        throw new AssertionError("instantiation is not allowed");
    }
}
