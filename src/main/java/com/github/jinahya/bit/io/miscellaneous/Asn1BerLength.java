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

/**
 * A deprecated alias of {@link com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength}, which this class
 * delegates to.
 *
 * @deprecated Use {@link com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength} instead;
 *             this class will be removed in the next major version.
 */
@Deprecated
public final class Asn1BerLength
        implements LongBitReader, LongBitWriter {

    /**
     * Reads and verifies a BER indefinite length.
     *
     * @param input the input.
     * @throws IOException if an I/O error occurs.
     * @see com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength#readBerIndefiniteLength(BitInput)
     */
    public static void readBerIndefiniteLength(final BitInput input) throws IOException {
        com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength.readBerIndefiniteLength(input);
    }

    /**
     * Writes a BER indefinite length.
     *
     * @param output the output.
     * @throws IOException if an I/O error occurs.
     * @see com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength#writeBerIndefiniteLength(BitOutput)
     */
    public static void writeBerIndefiniteLength(final BitOutput output) throws IOException {
        com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength.writeBerIndefiniteLength(output);
    }

    /**
     * The singleton instance of this class.
     */
    public static final Asn1BerLength INSTANCE = new Asn1BerLength();

    // -----------------------------------------------------------------------------------------------------------------
    private Asn1BerLength() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long readLong(final BitInput input) throws IOException {
        return com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength.INSTANCE.readLong(input);
    }

    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength.INSTANCE.writeLong(output, value);
    }
}
