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
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Asn1OidTest {

    @Test
    void instanceIsNotNull() {
        assertNotNull(Asn1Oid.INSTANCE);
    }

    @Test
    void roundTripsValues() throws IOException {
        roundTrip(new long[]{0L, 0L});
        roundTrip(new long[]{1L, 2L, 840L, 113549L});
        roundTrip(new long[]{2L, 999L, 3L});
    }

    @Test
    void readRejectsZeroLengthContent() {
        assertThrows(IOException.class,
                     () -> _TestUtils.read(Asn1Oid.INSTANCE, new byte[]{0x00, 0x00, 0x00, 0x00}));
    }

    @Test
    void readRejectsNullInput() {
        assertThrows(NullPointerException.class, () -> Asn1Oid.INSTANCE.read((BitInput) null));
    }

    @Test
    void writeRejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> Asn1Oid.INSTANCE.write((BitOutput) null, new long[]{1L, 2L}));
        assertThrows(NullPointerException.class,
                     () -> Asn1Oid.INSTANCE.write(output(new ByteArrayOutputStream()), null));
    }

    @Test
    void writeRejectsInvalidArcs() {
        assertThrows(IllegalArgumentException.class, () -> _TestUtils.write(Asn1Oid.INSTANCE, new long[]{1L}));
        assertThrows(IllegalArgumentException.class, () -> _TestUtils.write(Asn1Oid.INSTANCE, new long[]{3L, 0L}));
        assertThrows(IllegalArgumentException.class, () -> _TestUtils.write(Asn1Oid.INSTANCE, new long[]{1L, 40L}));
    }

    private static void roundTrip(final long[] value) throws IOException {
        assertArrayEquals(value, _TestUtils.read(Asn1Oid.INSTANCE, _TestUtils.write(Asn1Oid.INSTANCE, value)));
    }

    private static BitOutput output(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }
}
