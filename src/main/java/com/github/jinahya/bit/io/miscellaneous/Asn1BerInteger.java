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

import java.io.IOException;
import java.math.BigInteger;

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireGreaterThanOrEqualsTo;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * A codec for length-prefixed ASN.1 BER INTEGER content octets.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1BerInteger
        implements BitReader<BigInteger>, BitWriter<BigInteger> {

    private static final int MIN_LENGTH = 1;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The singleton instance of this codec.
     */
    public static final Asn1BerInteger INSTANCE = new Asn1BerInteger();

    // -----------------------------------------------------------------------------------------------------------------
    private Asn1BerInteger() {
        super();
        byteArrayReader = new ByteArrayReader.Signed8(BitIoConstants.SIZE_MAX_INT_UNSIGNED);
        byteArrayWriter = new ByteArrayWriter.Signed8(BitIoConstants.SIZE_MAX_INT_UNSIGNED);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public BigInteger read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        final byte[] bytes = byteArrayReader.read(input);
        requireGreaterThanOrEqualsTo(bytes.length, MIN_LENGTH, new _Supplier<IOException>() {
            @Override
            public IOException get() {
                return new IOException("length(" + bytes.length + ") < " + MIN_LENGTH);
            }
        });
        return new BigInteger(bytes);
    }

    @Override
    public void write(final BitOutput output, final BigInteger value) throws IOException {
        requireNonNullOutput(output);
        final byte[] bytes = requireNonNullValue(value).toByteArray();
        requireGreaterThanOrEqualsTo(bytes.length, MIN_LENGTH, new _Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("length(" + bytes.length + ") < " + MIN_LENGTH);
            }
        });
        byteArrayWriter.write(output, bytes);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private final ByteArrayReader byteArrayReader;

    private final ByteArrayWriter byteArrayWriter;
}
