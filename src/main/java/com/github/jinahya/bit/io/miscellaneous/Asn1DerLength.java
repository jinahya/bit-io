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
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;

import java.io.IOException;

/**
 * A codec for ASN.1 DER definite length octets.
 *
 * <p>DER uses definite, shortest-form length octets only. Indefinite and non-minimal BER forms are rejected on read,
 * and writes always use the shortest definite form.</p>
 */
public final class Asn1DerLength
        implements BitReader<Long>, BitWriter<Long> {

    /**
     * The singleton instance of this codec.
     */
    public static final Asn1DerLength INSTANCE = new Asn1DerLength();

    private Asn1DerLength() {
        super();
    }

    @Override
    public Long read(final BitInput input) throws IOException {
        return Asn1Lengths.readDefinite(input, true);
    }

    @Override
    public void write(final BitOutput output, final Long value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        Asn1Lengths.writeDefinite(output, value);
    }
}
