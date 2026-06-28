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
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteInput;
import com.github.jinahya.bit.io.StreamByteOutput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class StandardizedMiscCodecsTest {

    @Test
    void asn1Base128VariantsUseMinimalBase128() throws IOException {
        assertArrayEquals(new byte[]{0x00}, write(Asn1Base128TagNumber.INSTANCE, Long.valueOf(0L)));
        assertArrayEquals(new byte[]{0x7F}, write(Asn1Base128TagNumber.INSTANCE, Long.valueOf(127L)));
        assertArrayEquals(new byte[]{(byte) 0x81, 0x00}, write(Asn1Base128TagNumber.INSTANCE, Long.valueOf(128L)));
        assertEquals(Long.valueOf(128L), read(Asn1OidSubidentifier.INSTANCE, new byte[]{(byte) 0x81, 0x00}));
        assertThrows(IOException.class, () -> read(Asn1Base128TagNumber.INSTANCE, new byte[]{(byte) 0x80, 0x00}));
    }

    @Test
    void asn1BooleanHandlesBerAndDerRules() throws IOException {
        assertEquals(Boolean.TRUE, read(Asn1BerBoolean.INSTANCE, new byte[]{0x01}));
        assertEquals(Boolean.TRUE, read(Asn1DerBoolean.INSTANCE, new byte[]{(byte) 0xFF}));
        assertArrayEquals(new byte[]{(byte) 0xFF}, write(Asn1BerBoolean.INSTANCE, Boolean.TRUE));
        assertArrayEquals(new byte[]{0x00}, write(Asn1DerBoolean.INSTANCE, Boolean.FALSE));
        assertThrows(IOException.class, () -> read(Asn1DerBoolean.INSTANCE, new byte[]{0x01}));
    }

    @Test
    void asn1IntegerContentUsesTwoComplementAndDerMinimality() throws IOException {
        assertArrayEquals(new byte[]{0x7F}, writeDerInteger(BigInteger.valueOf(127L)));
        assertArrayEquals(new byte[]{0x00, (byte) 0x80}, writeDerInteger(BigInteger.valueOf(128L)));
        assertArrayEquals(new byte[]{(byte) 0x80}, writeDerInteger(BigInteger.valueOf(-128L)));
        assertArrayEquals(new byte[]{(byte) 0xFF, 0x7F}, writeDerInteger(BigInteger.valueOf(-129L)));
        assertEquals(BigInteger.valueOf(128L), readDerInteger(new byte[]{0x00, (byte) 0x80}));
        assertThrows(IOException.class, () -> readDerInteger(new byte[]{0x00, 0x7F}));
        assertThrows(IOException.class, () -> readDerInteger(new byte[]{(byte) 0xFF, (byte) 0x80}));
    }

    @Test
    void asn1OidContentPacksFirstTwoArcs() throws IOException {
        final long[] arcs = {1L, 2L, 840L, 113549L};
        final byte[] encoded = writeOid(arcs);
        assertArrayEquals(new byte[]{0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0x0D}, encoded);
        assertArrayEquals(arcs, readOid(encoded));
    }

    @Test
    void cborIntegersUseShortestWritesAndDeterministicReads() throws IOException {
        assertArrayEquals(new byte[]{0x00}, write(CborDeterministicInt.INSTANCE, Long.valueOf(0L)));
        assertArrayEquals(new byte[]{0x17}, write(CborDeterministicInt.INSTANCE, Long.valueOf(23L)));
        assertArrayEquals(new byte[]{0x18, 0x18}, write(CborDeterministicInt.INSTANCE, Long.valueOf(24L)));
        assertArrayEquals(new byte[]{0x20}, write(CborDeterministicInt.INSTANCE, Long.valueOf(-1L)));
        assertArrayEquals(new byte[]{0x37}, write(CborDeterministicInt.INSTANCE, Long.valueOf(-24L)));
        assertArrayEquals(new byte[]{0x38, 0x18}, write(CborDeterministicInt.INSTANCE, Long.valueOf(-25L)));
        assertArrayEquals(new byte[]{0x19, 0x03, (byte) 0xE8},
                          write(CborDeterministicInt.INSTANCE, Long.valueOf(1000L)));
        assertEquals(Long.valueOf(0L), read(CborGenericInt.INSTANCE, new byte[]{0x18, 0x00}));
        assertThrows(IOException.class, () -> read(CborDeterministicInt.INSTANCE, new byte[]{0x18, 0x00}));
    }

    @Test
    void cborSimpleValuesExcludeFloatsAndBreak() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0xF4}, write(CborSimpleValue.INSTANCE, Integer.valueOf(20)));
        assertArrayEquals(new byte[]{(byte) 0xF8, 0x20}, write(CborSimpleValue.INSTANCE, Integer.valueOf(32)));
        assertEquals(Integer.valueOf(255), read(CborSimpleValue.INSTANCE, new byte[]{(byte) 0xF8, (byte) 0xFF}));
        assertThrows(IllegalArgumentException.class, () -> write(CborSimpleValue.INSTANCE, Integer.valueOf(24)));
        assertThrows(IOException.class, () -> read(CborSimpleValue.INSTANCE, new byte[]{(byte) 0xF9, 0x00, 0x00}));
    }

    private static <T> T read(final BitReader<T> reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return reader.read(input);
    }

    private static <T> byte[] write(final BitWriter<T> writer, final T value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        writer.write(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    private static BigInteger readDerInteger(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return Asn1DerInteger.readContent(input, bytes.length);
    }

    private static byte[] writeDerInteger(final BigInteger value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1DerInteger.writeContent(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    private static long[] readOid(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return Asn1Oid.readContent(input, bytes.length);
    }

    private static byte[] writeOid(final long[] value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1Oid.writeContent(output, value);
        output.align(1);
        return baos.toByteArray();
    }
}
