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
 * A codec for ASN.1 DER BOOLEAN content octets.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1DerBoolean
        implements BitReader<Boolean>, BitWriter<Boolean> {

    public static final Asn1DerBoolean INSTANCE = new Asn1DerBoolean();

    private Asn1DerBoolean() {
        super();
    }

    @Override
    public Boolean read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        final int value = input.readUnsignedInt(Byte.SIZE);
        if (value != 0x00 && value != 0xFF) {
            throw new IOException("invalid DER BOOLEAN content octet: " + value);
        }
        return Boolean.valueOf(value != 0);
    }

    @Override
    public void write(final BitOutput output, final Boolean value) throws IOException {
        requireNonNullOutput(output);
        output.writeUnsignedInt(Byte.SIZE, requireNonNullValue(value).booleanValue() ? 0xFF : 0x00);
    }
}
