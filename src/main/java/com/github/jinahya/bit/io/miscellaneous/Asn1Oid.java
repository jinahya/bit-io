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
import com.github.jinahya.bit.io.BitIoConstants;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;
import com.github.jinahya.bit.io.ByteArrayReader;
import com.github.jinahya.bit.io.ByteArrayWriter;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireGreaterThanOrEqualsTo;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * A codec for length-prefixed ASN.1 OBJECT IDENTIFIER content octets.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1Oid
        implements BitReader<long[]>, BitWriter<long[]> {

    /**
     * The singleton instance of this codec.
     */
    public static final Asn1Oid INSTANCE = new Asn1Oid();

    @Override
    public long[] read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        final byte[] bytes = byteArrayReader.read(input);
        requireGreaterThanOrEqualsTo(bytes.length, MIN_LENGTH, new _Supplier<IOException>() {
            @Override
            public IOException get() {
                return new IOException("length(" + bytes.length + ") < " + MIN_LENGTH);
            }
        });
        return decode(bytes);
    }

    @Override
    public void write(final BitOutput output, final long[] value) throws IOException {
        requireNonNullOutput(output);
        final byte[] bytes = encode(value);
        requireGreaterThanOrEqualsTo(bytes.length, MIN_LENGTH, new _Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("length(" + bytes.length + ") < " + MIN_LENGTH);
            }
        });
        byteArrayWriter.write(output, bytes);
    }

    private static long[] decode(final byte[] bytes) throws IOException {
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

    private static byte[] encode(final long[] value) throws IOException {
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
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(bytes));
        Asn1Base128Util.write(output, arc0 * 40L + arc1);
        for (int i = 2; i < value.length; i++) {
            Asn1Base128Util.write(output, value[i]);
        }
        output.align(1);
        return bytes.toByteArray();
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
        super();
        byteArrayReader = new ByteArrayReader.Signed8(BitIoConstants.SIZE_MAX_INT_UNSIGNED);
        byteArrayWriter = new ByteArrayWriter.Signed8(BitIoConstants.SIZE_MAX_INT_UNSIGNED);
    }

    private static final int MIN_LENGTH = 1;

    private final ByteArrayReader byteArrayReader;

    private final ByteArrayWriter byteArrayWriter;
}
