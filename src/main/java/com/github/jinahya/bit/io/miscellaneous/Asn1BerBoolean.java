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
import com.github.jinahya.bit.io.BooleanBitReader;
import com.github.jinahya.bit.io.BooleanBitWriter;

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

/**
 * A codec for ASN.1 BER BOOLEAN content octets.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1BerBoolean
        implements BooleanBitReader, BooleanBitWriter {

    public static final Asn1BerBoolean INSTANCE = new Asn1BerBoolean();

    private Asn1BerBoolean() {
        super();
    }

    @Override
    public boolean readBoolean(final BitInput input) throws IOException {
        requireNonNullInput(input);
        return input.readUnsignedInt(Byte.SIZE) != 0;
    }

    @Override
    public void writeBoolean(final BitOutput output, final boolean value) throws IOException {
        requireNonNullOutput(output);
        output.writeUnsignedInt(Byte.SIZE, value ? 0xFF : 0x00);
    }
}
