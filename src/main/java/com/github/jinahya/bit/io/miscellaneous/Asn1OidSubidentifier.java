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

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * A codec for a single ASN.1 OBJECT IDENTIFIER subidentifier.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1OidSubidentifier
        implements BitReader<Long>, BitWriter<Long> {

    public static final Asn1OidSubidentifier INSTANCE = new Asn1OidSubidentifier();

    private Asn1OidSubidentifier() {
        super();
    }

    @Override
    public Long read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        return Long.valueOf(Asn1Base128s.read(input, true));
    }

    @Override
    public void write(final BitOutput output, final Long value) throws IOException {
        requireNonNullOutput(output);
        Asn1Base128s.write(output, requireNonNullValue(value));
    }
}
