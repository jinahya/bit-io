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
 * A codec for ASN.1 high-tag-number base-128 content octets.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1Base128TagNumber
        implements LongBitReader, LongBitWriter {

    public static final Asn1Base128TagNumber INSTANCE = new Asn1Base128TagNumber();

    private Asn1Base128TagNumber() {
        super();
    }

    @Override
    public long readLong(final BitInput input) throws IOException {
        requireNonNullInput(input);
        return Asn1Base128Util.read(input, true);
    }

    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        Asn1Base128Util.write(output, value);
    }
}
