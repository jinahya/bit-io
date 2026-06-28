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

import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthConstants.MASK_LENGTH_OCTETS;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthConstants.MASK_LONG_FORM;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthConstants.MAX_LENGTH_OCTETS_FOR_LONG;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthConstants.VALUE_INDEFINITE;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthConstants.VALUE_RESERVED;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

final class Asn1Lengths {

    static long readDefinite(final BitInput input, final boolean der) throws IOException {
        requireNonNullInput(input);
        final int first = input.readInt(true, Byte.SIZE);
        if ((first & MASK_LONG_FORM) == 0) {
            return first;
        }
        if (first == VALUE_INDEFINITE) {
            throw new IOException("indefinite length is not a definite length");
        }
        if (first == VALUE_RESERVED) {
            throw new IOException("reserved ASN.1 length octet: 0xFF");
        }
        final int lengthOctets = first & MASK_LENGTH_OCTETS;
        return der ? readDerLongForm(input, lengthOctets) : readBerLongForm(input, lengthOctets);
    }

    static void writeDefinite(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        if (value < 0L) {
            throw new IllegalArgumentException("negative length: " + value);
        }
        if (value <= MASK_LENGTH_OCTETS) {
            output.writeInt(true, Byte.SIZE, (int) value);
            return;
        }
        int count = 0;
        for (long v = value; v != 0L; v >>>= Byte.SIZE) {
            count++;
        }
        output.writeInt(true, Byte.SIZE, MASK_LONG_FORM | count);
        for (int shift = (count - 1) * Byte.SIZE; shift >= 0; shift -= Byte.SIZE) {
            output.writeInt(true, Byte.SIZE, (int) (value >>> shift) & 0xFF);
        }
    }

    static void readIndefinite(final BitInput input) throws IOException {
        requireNonNullInput(input);
        final int value = input.readInt(true, Byte.SIZE);
        if (value != VALUE_INDEFINITE) {
            throw new IOException("not an indefinite length marker: " + value);
        }
    }

    static void writeIndefinite(final BitOutput output) throws IOException {
        requireNonNullOutput(output);
        output.writeInt(true, Byte.SIZE, VALUE_INDEFINITE);
    }

    private static long readBerLongForm(final BitInput input, final int lengthOctets) throws IOException {
        long value = 0L;
        boolean overflow = false;
        for (int i = 0; i < lengthOctets; i++) {
            final int octet = input.readInt(true, Byte.SIZE);
            if (!overflow) {
                if (value > (Long.MAX_VALUE >>> Byte.SIZE)) {
                    overflow = true;
                } else {
                    value = (value << Byte.SIZE) | octet;
                    if (value < 0L) {
                        overflow = true;
                    }
                }
            }
        }
        if (overflow) {
            throw new IOException("ASN.1 length exceeds signed long range");
        }
        return value;
    }

    private static long readDerLongForm(final BitInput input, final int lengthOctets) throws IOException {
        if (lengthOctets > MAX_LENGTH_OCTETS_FOR_LONG) {
            throw new IOException("ASN.1 length exceeds signed long range");
        }
        long value = 0L;
        for (int i = 0; i < lengthOctets; i++) {
            final int octet = input.readInt(true, Byte.SIZE);
            if (i == 0 && octet == 0) {
                throw new IOException("non-minimal DER length");
            }
            value = (value << Byte.SIZE) | octet;
            if (value < 0L) {
                throw new IOException("ASN.1 length exceeds signed long range");
            }
        }
        if (value <= MASK_LENGTH_OCTETS) {
            throw new IOException("non-minimal DER length");
        }
        return value;
    }

    private Asn1Lengths() {
        throw new AssertionError("instantiation is not allowed");
    }
}
