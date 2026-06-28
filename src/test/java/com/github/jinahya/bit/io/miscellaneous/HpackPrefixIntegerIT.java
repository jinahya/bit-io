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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class HpackPrefixIntegerIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesIntegerExamplesFromRfc7541() throws Exception {
        final String hpack = downloadText("https://www.rfc-editor.org/rfc/rfc7541.txt", 384 * 1024);
        final String qpack = downloadText("https://www.rfc-editor.org/rfc/rfc9204.txt", 384 * 1024);
        assertTrue(hpack.contains("5.1.  Integer Representation"), "downloaded RFC 7541");
        assertTrue(qpack.contains("prefixed integers") || qpack.contains("Prefixed Integers"), "downloaded RFC 9204");

        assertArrayEquals(hex("0a"), writeStandalone(5, 10L));
        assertEquals(10L, readStandalone(5, hex("0a")));

        assertArrayEquals(hex("1f9a0a"), writeStandalone(5, 1337L));
        assertEquals(1337L, readStandalone(5, hex("1f9a0a")));

        assertArrayEquals(hex("2a"), write(HpackPrefixInteger.of(8), Long.valueOf(42L)));
        assertEquals(Long.valueOf(42L), read(HpackPrefixInteger.of(8), hex("2a")));
    }

    private static long readStandalone(final int prefixSize, final byte[] bytes) throws Exception {
        final com.github.jinahya.bit.io.BitInput input = new com.github.jinahya.bit.io.DefaultBitInput(
                new com.github.jinahya.bit.io.StreamByteInput(new java.io.ByteArrayInputStream(bytes)));
        if (prefixSize < Byte.SIZE) {
            input.skip(Byte.SIZE - prefixSize);
        }
        return HpackPrefixInteger.of(prefixSize).read(input).longValue();
    }

    private static byte[] writeStandalone(final int prefixSize, final long value) throws Exception {
        final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        final com.github.jinahya.bit.io.BitOutput output = new com.github.jinahya.bit.io.DefaultBitOutput(
                new com.github.jinahya.bit.io.StreamByteOutput(baos));
        if (prefixSize < Byte.SIZE) {
            output.writeUnsignedInt(Byte.SIZE - prefixSize, 0);
        }
        HpackPrefixInteger.of(prefixSize).write(output, Long.valueOf(value));
        output.align(1);
        return baos.toByteArray();
    }
}
