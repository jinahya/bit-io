package com.github.jinahya.bit.io.miscellaneous.ietf;

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
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests {@link UuidRfc9562}.
 */
class UuidRfc9562Test {

    private static final UUID VALUE = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");

    private static final byte[] WIRE = hex("0123456789abcdef0123456789abcdef");

    @Test
    void readsKnownBytes() throws IOException {
        assertEquals(VALUE, UuidRfc9562.INSTANCE.read(BitInputs.from(WIRE.clone())));
    }

    @Test
    void writesKnownBytes() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = BitOutputs.from(bytes);
        UuidRfc9562.INSTANCE.write(output, VALUE);
        output.align(1);
        assertArrayEquals(WIRE, bytes.toByteArray());
    }

    @Test
    void roundTripsRandomValuesAtANonOctetOffset() throws IOException {
        final Random random = new Random(1L);
        for (int i = 0; i < 1000; i++) {
            final UUID value = new UUID(random.nextLong(), random.nextLong());
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final BitOutput output = BitOutputs.from(bytes);
            output.writeUnsignedInt(3, 5);
            output.writeObject(UuidRfc9562.INSTANCE, value);
            output.align(1);
            final BitInput input = BitInputs.from(bytes.toByteArray());
            assertEquals(5, input.readUnsignedInt(3));
            assertEquals(value, input.readObject(UuidRfc9562.INSTANCE));
        }
    }

    @Test
    void rejectsNullArguments() {
        assertEquals("value is null", assertThrows(
                NullPointerException.class, () -> UuidRfc9562.INSTANCE.write(BitOutputs.from(new byte[16]), null))
                .getMessage());
        assertEquals("output is null", assertThrows(
                NullPointerException.class, () -> UuidRfc9562.INSTANCE.write(null, VALUE)).getMessage());
        assertEquals("input is null", assertThrows(
                NullPointerException.class, () -> UuidRfc9562.INSTANCE.read(null)).getMessage());
    }

    private static byte[] hex(final String hex) {
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }
}
