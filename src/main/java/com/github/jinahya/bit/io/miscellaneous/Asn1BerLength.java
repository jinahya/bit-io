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
import com.github.jinahya.bit.io.LongBitReader;
import com.github.jinahya.bit.io.LongBitWriter;

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

/**
 * A codec for ASN.1 BER length octets.
 *
 * <p>The {@link #readLong(BitInput)} and {@link #writeLong(BitOutput, long)} methods handle definite lengths only. A
 * BER indefinite length is a marker, not a non-negative length value, and is handled explicitly by
 * {@link #readBerIndefiniteLength(BitInput)} and {@link #writeBerIndefiniteLength(BitOutput)}.</p>
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1BerLength
        implements LongBitReader, LongBitWriter {

    /**
     * Reads and verifies a BER indefinite length marker.
     *
     * @param input the input to read from; must not be {@code null}.
     * @throws IOException if an I/O error occurs, or if the next octet is not {@code 0x80}.
     */
    public static void readBerIndefiniteLength(final BitInput input) throws IOException {
        requireNonNullInput(input);
        Asn1LengthUtil.readIndefinite(input);
    }

    /**
     * Writes a BER indefinite length marker.
     *
     * @param output the output to write to; must not be {@code null}.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeBerIndefiniteLength(final BitOutput output) throws IOException {
        requireNonNullOutput(output);
        Asn1LengthUtil.writeIndefinite(output);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The singleton instance of this codec.
     */
    public static final Asn1BerLength INSTANCE = new Asn1BerLength();

    // -----------------------------------------------------------------------------------------------------------------
    private Asn1BerLength() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long readLong(final BitInput input) throws IOException {
        requireNonNullInput(input);
        return Asn1LengthUtil.readDefinite(input, false);
    }

    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        Asn1LengthUtil.writeDefinite(output, value);
    }
}
