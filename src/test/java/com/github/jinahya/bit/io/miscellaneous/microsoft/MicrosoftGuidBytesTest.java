package com.github.jinahya.bit.io.miscellaneous.microsoft;

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
import com.github.jinahya.bit.io.BitInputs;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.BitOutputs;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests {@link MicrosoftGuidBytes}.
 *
 * @see <a href="https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-dtyp/001eec5a-7f8b-4293-9e21-ca349392db40">
 * [MS-DTYP] 2.3.4.2 GUID--Packet Representation</a>
 */
class MicrosoftGuidBytesTest {

    // UUID 01234567-89ab-cdef-0123-456789abcdef
    private static final byte[] CANONICAL = hex("0123456789abcdef0123456789abcdef");

    private static final byte[] WIRE = hex("67452301ab89efcd0123456789abcdef");

    @Test
    void writesData1Data2Data3LittleEndianAndData4InOrder() throws IOException {
        final byte[] value = CANONICAL.clone();
        assertArrayEquals(WIRE, write(value));
        assertArrayEquals(CANONICAL, value, "the caller's array is left unchanged");
    }

    @Test
    void readsBackToCanonicalOrder() throws IOException {
        assertArrayEquals(CANONICAL, MicrosoftGuidBytes.INSTANCE.read(BitInputs.from(WIRE.clone())));
    }

    @Test
    void roundTripsAtANonOctetOffset() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = BitOutputs.from(bytes);
        output.writeUnsignedInt(3, 5);
        output.writeObject(MicrosoftGuidBytes.INSTANCE, CANONICAL);
        output.writeUnsignedInt(5, 19);
        output.align(1);
        final BitInput input = BitInputs.from(bytes.toByteArray());
        assertEquals(5, input.readUnsignedInt(3));
        assertArrayEquals(CANONICAL, input.readObject(MicrosoftGuidBytes.INSTANCE));
        assertEquals(19, input.readUnsignedInt(5));
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(NullPointerException.class, () -> MicrosoftGuidBytes.INSTANCE.write(null, CANONICAL));
        assertThrows(NullPointerException.class, () -> write(null));
        assertThrows(IllegalArgumentException.class, () -> write(new byte[15]));
        assertThrows(EOFException.class, () -> MicrosoftGuidBytes.INSTANCE.read(
                BitInputs.from(new java.io.ByteArrayInputStream(new byte[15]))));
        assertEquals(16, MicrosoftGuidBytes.LENGTH);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static byte[] write(final byte[] value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = BitOutputs.from(bytes);
        MicrosoftGuidBytes.INSTANCE.write(output, value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static byte[] hex(final String hex) {
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }
}
