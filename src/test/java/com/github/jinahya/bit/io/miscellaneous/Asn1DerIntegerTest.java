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
import com.github.jinahya.bit.io.ByteArrayWriter;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Asn1DerIntegerTest {

    @Test
    void instanceIsNotNull() {
        assertNotNull(Asn1DerInteger.INSTANCE);
    }

    @Test
    void roundTripsValues() throws IOException {
        roundTrip(BigInteger.ZERO);
        roundTrip(BigInteger.ONE);
        roundTrip(BigInteger.valueOf(-1L));
        roundTrip(BigInteger.valueOf(127L));
        roundTrip(BigInteger.valueOf(128L));
        roundTrip(BigInteger.valueOf(-128L));
        roundTrip(BigInteger.valueOf(-129L));
    }

    @Test
    void readRejectsZeroLengthContent() throws IOException {
        assertThrows(IOException.class,
                     () -> _TestUtils.read(Asn1DerInteger.INSTANCE, encodeContent(new byte[0])));
    }

    @Test
    void readRejectsNonMinimalContent() throws IOException {
        assertThrows(IOException.class,
                     () -> _TestUtils.read(Asn1DerInteger.INSTANCE, encodeContent(new byte[]{0x00, 0x7F})));
        assertThrows(IOException.class,
                     () -> _TestUtils.read(Asn1DerInteger.INSTANCE,
                                           encodeContent(new byte[]{(byte) 0xFF, (byte) 0x80})));
    }

    @Test
    void readRejectsNullInput() {
        assertThrows(NullPointerException.class, () -> Asn1DerInteger.INSTANCE.read((BitInput) null));
    }

    @Test
    void writeRejectsNullArguments() {
        assertThrows(NullPointerException.class,
                     () -> Asn1DerInteger.INSTANCE.write((BitOutput) null, BigInteger.ZERO));
        assertThrows(NullPointerException.class, () -> Asn1DerInteger.INSTANCE.write(
                output(new ByteArrayOutputStream()), null));
    }

    private static void roundTrip(final BigInteger value) throws IOException {
        assertEquals(value, _TestUtils.read(Asn1DerInteger.INSTANCE, _TestUtils.write(Asn1DerInteger.INSTANCE, value)));
    }

    private static BitOutput output(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }

    private static byte[] encodeContent(final byte[] content) throws IOException {
        return _TestUtils.write(new ByteArrayWriter.Signed8(31), content);
    }
}
