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
import java.math.BigInteger;

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * Utilities for ASN.1 BER INTEGER content octets.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-X.690">ITU-T X.690: ASN.1 encoding rules</a>
 */
public final class Asn1BerInteger {

    public static BigInteger readContent(final BitInput input, final int length) throws IOException {
        requireNonNullInput(input);
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }
        final byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = input.readByte8();
        }
        return new BigInteger(bytes);
    }

    public static void writeContent(final BitOutput output, final BigInteger value) throws IOException {
        requireNonNullOutput(output);
        final byte[] bytes = requireNonNullValue(value).toByteArray();
        for (int i = 0; i < bytes.length; i++) {
            output.writeByte8(bytes[i]);
        }
    }

    private Asn1BerInteger() {
        throw new AssertionError("instantiation is not allowed");
    }
}
