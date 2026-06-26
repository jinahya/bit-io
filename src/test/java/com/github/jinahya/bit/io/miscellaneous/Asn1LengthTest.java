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

import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.readBer;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.readBerIndefinite;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.readDer;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.writeBer;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.writeBerIndefinite;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.writeDer;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.writeDerObject;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Asn1LengthTest {

    @Test
    void derWritesKnownVectors() throws IOException {
        assertArrayEquals(new byte[]{0x00}, writeDer(0L));
        assertArrayEquals(new byte[]{0x01}, writeDer(1L));
        assertArrayEquals(new byte[]{0x7F}, writeDer(127L));
        assertArrayEquals(new byte[]{(byte) 0x81, (byte) 0x80}, writeDer(128L));
        assertArrayEquals(new byte[]{(byte) 0x81, (byte) 0xFF}, writeDer(255L));
        assertArrayEquals(new byte[]{(byte) 0x82, 0x01, 0x00}, writeDer(256L));
        assertArrayEquals(new byte[]{
                (byte) 0x88, 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
        }, writeDer(Long.MAX_VALUE));
    }

    @Test
    void derReadsKnownVectors() throws IOException {
        assertEquals(0L, readDer(new byte[]{0x00}));
        assertEquals(1L, readDer(new byte[]{0x01}));
        assertEquals(127L, readDer(new byte[]{0x7F}));
        assertEquals(128L, readDer(new byte[]{(byte) 0x81, (byte) 0x80}));
        assertEquals(255L, readDer(new byte[]{(byte) 0x81, (byte) 0xFF}));
        assertEquals(256L, readDer(new byte[]{(byte) 0x82, 0x01, 0x00}));
        assertEquals(Long.MAX_VALUE, readDer(new byte[]{
                (byte) 0x88, 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
        }));
    }

    @Test
    void berWritesDefiniteAsMinimalForm() throws IOException {
        assertArrayEquals(writeDer(0L), writeBer(0L));
        assertArrayEquals(writeDer(127L), writeBer(127L));
        assertArrayEquals(writeDer(128L), writeBer(128L));
        assertArrayEquals(writeDer(Long.MAX_VALUE), writeBer(Long.MAX_VALUE));
    }

    @Test
    void berAcceptsNonMinimalDefiniteForms() throws IOException {
        assertEquals(127L, readBer(new byte[]{(byte) 0x81, 0x7F}));
        assertEquals(128L, readBer(new byte[]{(byte) 0x82, 0x00, (byte) 0x80}));
    }

    @Test
    void berHandlesIndefiniteMarkerExplicitly() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0x80}, writeBerIndefinite());
        readBerIndefinite(new byte[]{(byte) 0x80});
        assertThrows(IOException.class, () -> readBer(new byte[]{(byte) 0x80}));
        assertThrows(IOException.class, () -> readBerIndefinite(new byte[]{0x00}));
    }

    @Test
    void derRejectsBerOnlyForms() {
        assertThrows(IOException.class, () -> readDer(new byte[]{(byte) 0x80}));
        assertThrows(IOException.class, () -> readDer(new byte[]{(byte) 0x81, 0x7F}));
        assertThrows(IOException.class, () -> readDer(new byte[]{(byte) 0x82, 0x00, (byte) 0x80}));
    }

    @Test
    void rejectsReservedAndOverflowForms() {
        assertThrows(IOException.class, () -> readBer(new byte[]{(byte) 0xFF}));
        assertThrows(IOException.class, () -> readDer(new byte[]{(byte) 0xFF}));
        assertThrows(IOException.class, () -> readBer(new byte[]{
                (byte) 0x89, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
        assertThrows(IOException.class, () -> readDer(new byte[]{
                (byte) 0x89, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
    }

    @Test
    void rejectsNegativeLengths() {
        assertThrows(IllegalArgumentException.class, () -> writeBer(-1L));
        assertThrows(IllegalArgumentException.class, () -> writeDer(-1L));
    }

    @Test
    void rejectsNullArguments() {
        final DefaultBitOutput output = new DefaultBitOutput(new StreamByteOutput(new ByteArrayOutputStream()));
        assertThrows(NullPointerException.class, () -> Asn1BerLength.INSTANCE.read(null));
        assertThrows(NullPointerException.class, () -> Asn1BerLength.INSTANCE.readIndefinite(null));
        assertThrows(NullPointerException.class, () -> Asn1BerLength.INSTANCE.readBerIndefiniteLength(null));
        assertThrows(NullPointerException.class, () -> Asn1DerLength.INSTANCE.read(null));
        assertThrows(NullPointerException.class, () -> Asn1BerLength.INSTANCE.write(null, 0L));
        assertThrows(NullPointerException.class, () -> Asn1BerLength.INSTANCE.writeIndefinite(null));
        assertThrows(NullPointerException.class, () -> Asn1BerLength.INSTANCE.writeBerIndefiniteLength(null));
        assertThrows(NullPointerException.class, () -> Asn1DerLength.INSTANCE.write(null, 0L));
        assertThrows(NullPointerException.class, () -> Asn1BerLength.INSTANCE.write(output, null));
        assertThrows(NullPointerException.class, () -> writeDerObject(null));
    }
}
