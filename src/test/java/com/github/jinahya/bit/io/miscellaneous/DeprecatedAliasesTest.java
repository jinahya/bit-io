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
import com.github.jinahya.bit.io.BooleanBitReader;
import com.github.jinahya.bit.io.BooleanBitWriter;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.IntBitReader;
import com.github.jinahya.bit.io.IntBitWriter;
import com.github.jinahya.bit.io.LongBitReader;
import com.github.jinahya.bit.io.LongBitWriter;
import com.github.jinahya.bit.io.StreamByteInput;
import com.github.jinahya.bit.io.StreamByteOutput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests that the deprecated aliases in {@code com.github.jinahya.bit.io.miscellaneous} behave exactly as the classes
 * they delegate to, which were relocated to the publisher subpackages.
 */
@SuppressWarnings({"deprecation"})
class DeprecatedAliasesTest {

    @Test
    void longCodecs() throws IOException {
        assertLong(Asn1Base128TagNumber.INSTANCE,
                   com.github.jinahya.bit.io.miscellaneous.itu.Asn1Base128TagNumber.INSTANCE, 300L);
        assertLong(Asn1BerLength.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerLength.INSTANCE, 300L);
        assertLong(Asn1DerLength.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.Asn1DerLength.INSTANCE, 300L);
        assertLong(Asn1OidSubidentifier.INSTANCE,
                   com.github.jinahya.bit.io.miscellaneous.itu.Asn1OidSubidentifier.INSTANCE, 113549L);
        assertLong(ExpGolombSE.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.ExpGolombSE.INSTANCE, -7L);
        assertLong(ExpGolombUE.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.ExpGolombUE.INSTANCE, 42L);
        assertLong(GolombJPEGLS.of(2), com.github.jinahya.bit.io.miscellaneous.itu.GolombJPEGLS.of(2), -5L);
        assertLong(CborDeterministicInt.INSTANCE,
                   com.github.jinahya.bit.io.miscellaneous.ietf.CborDeterministicInt.INSTANCE, -24L);
        assertLong(CborGenericInt.INSTANCE,
                   com.github.jinahya.bit.io.miscellaneous.ietf.CborGenericInt.INSTANCE, 1000L);
        assertLong(HpackPrefixInteger.of(5),
                   com.github.jinahya.bit.io.miscellaneous.ietf.HpackPrefixInteger.of(5), 1337L);
        assertLong(RiceFLAC.of(4), com.github.jinahya.bit.io.miscellaneous.ietf.RiceFLAC.of(4), -3L);
        assertLong(Leb128Reader.UNSIGNED, Leb128Writer.UNSIGNED,
                   com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Reader.UNSIGNED,
                   com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer.UNSIGNED, 624485L);
        assertLong(Leb128Reader.SIGNED, Leb128Writer.SIGNED,
                   com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Reader.SIGNED,
                   com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer.SIGNED, -123456L);
    }

    @Test
    void otherCodecs() throws IOException {
        assertBoolean(Asn1BerBoolean.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerBoolean.INSTANCE);
        assertBoolean(Asn1DerBoolean.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.Asn1DerBoolean.INSTANCE);
        assertInt(CborSimpleValue.INSTANCE, com.github.jinahya.bit.io.miscellaneous.ietf.CborSimpleValue.INSTANCE, 20);
        assertObject(Asn1BerInteger.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerInteger.INSTANCE,
                     BigInteger.valueOf(-129L));
        assertObject(Asn1DerInteger.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.Asn1DerInteger.INSTANCE,
                     BigInteger.valueOf(-129L));
        assertArrayEquals(new long[]{1L, 2L, 840L, 113549L}, (long[]) assertObject(
                Asn1Oid.INSTANCE, com.github.jinahya.bit.io.miscellaneous.itu.Asn1Oid.INSTANCE,
                new long[]{1L, 2L, 840L, 113549L}));
        final byte[] uuid = new byte[UuidRfc9562Bytes.LENGTH];
        uuid[6] = 0x40;
        assertArrayEquals(uuid, (byte[]) assertObject(
                UuidRfc9562Bytes.INSTANCE, com.github.jinahya.bit.io.miscellaneous.ietf.UuidRfc9562Bytes.INSTANCE,
                uuid));
    }

    @Test
    void constantsAndAccessors() throws IOException {
        assertEquals(com.github.jinahya.bit.io.miscellaneous.ietf.UuidRfc9562Bytes.LENGTH, UuidRfc9562Bytes.LENGTH);
        assertEquals(3, GolombJPEGLS.of(3).parameter());
        assertEquals(6, HpackPrefixInteger.of(6).prefixSize());
        assertEquals(7, RiceFLAC.of(7).parameter());
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(bytes));
        Asn1BerLength.writeBerIndefiniteLength(output);
        output.align(1);
        assertArrayEquals(new byte[]{(byte) 0x80}, bytes.toByteArray());
        Asn1BerLength.readBerIndefiniteLength(input(bytes.toByteArray()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static <C extends LongBitReader & LongBitWriter> void assertLong(final C alias, final C target,
                                                                            final long value)
            throws IOException {
        assertLong(alias, alias, target, target, value);
    }

    private static void assertLong(final LongBitReader aliasReader, final LongBitWriter aliasWriter,
                                   final LongBitReader targetReader, final LongBitWriter targetWriter,
                                   final long value)
            throws IOException {
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        final BitOutput o1 = new DefaultBitOutput(new StreamByteOutput(expected));
        targetWriter.writeLong(o1, value);
        o1.align(1);
        final ByteArrayOutputStream actual = new ByteArrayOutputStream();
        final BitOutput o2 = new DefaultBitOutput(new StreamByteOutput(actual));
        aliasWriter.writeLong(o2, value);
        o2.align(1);
        assertArrayEquals(expected.toByteArray(), actual.toByteArray());
        assertEquals(value, aliasReader.readLong(input(actual.toByteArray())));
        assertEquals(targetReader.readLong(input(actual.toByteArray())),
                     aliasReader.readLong(input(actual.toByteArray())));
    }

    private static <C extends BooleanBitReader & BooleanBitWriter> void assertBoolean(final C alias, final C target)
            throws IOException {
        for (final boolean value : new boolean[]{true, false}) {
            final ByteArrayOutputStream expected = new ByteArrayOutputStream();
            final BitOutput o1 = new DefaultBitOutput(new StreamByteOutput(expected));
            target.writeBoolean(o1, value);
            o1.align(1);
            final ByteArrayOutputStream actual = new ByteArrayOutputStream();
            final BitOutput o2 = new DefaultBitOutput(new StreamByteOutput(actual));
            alias.writeBoolean(o2, value);
            o2.align(1);
            assertArrayEquals(expected.toByteArray(), actual.toByteArray());
            assertEquals(value, alias.readBoolean(input(actual.toByteArray())));
        }
    }

    private static <C extends IntBitReader & IntBitWriter> void assertInt(final C alias, final C target,
                                                                          final int value)
            throws IOException {
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        final BitOutput o1 = new DefaultBitOutput(new StreamByteOutput(expected));
        target.writeInt(o1, value);
        o1.align(1);
        final ByteArrayOutputStream actual = new ByteArrayOutputStream();
        final BitOutput o2 = new DefaultBitOutput(new StreamByteOutput(actual));
        alias.writeInt(o2, value);
        o2.align(1);
        assertArrayEquals(expected.toByteArray(), actual.toByteArray());
        assertEquals(value, alias.readInt(input(actual.toByteArray())));
    }

    private static <T, C extends BitReader<T> & BitWriter<T>> Object assertObject(final C alias, final C target,
                                                                                  final T value)
            throws IOException {
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        final BitOutput o1 = new DefaultBitOutput(new StreamByteOutput(expected));
        target.write(o1, value);
        o1.align(1);
        final ByteArrayOutputStream actual = new ByteArrayOutputStream();
        final BitOutput o2 = new DefaultBitOutput(new StreamByteOutput(actual));
        alias.write(o2, value);
        o2.align(1);
        assertArrayEquals(expected.toByteArray(), actual.toByteArray());
        final T read = alias.read(input(actual.toByteArray()));
        if (!(value instanceof long[]) && !(value instanceof byte[])) {
            assertEquals(value, read);
        }
        return read;
    }

    private static BitInput input(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }
}
