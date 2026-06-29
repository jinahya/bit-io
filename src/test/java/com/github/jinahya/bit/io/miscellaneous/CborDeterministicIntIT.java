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
class CborDeterministicIntIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesShortestIntegerExamplesFromRfc8949() throws Exception {
        final String document = downloadText("https://www.rfc-editor.org/rfc/rfc8949.txt", 512 * 1024);
        assertTrue(document.contains("Appendix A.  Examples of Encoded CBOR Data Items"), "downloaded RFC 8949");

        final long[][] rows = {
                {0L, 0x00L}, {1L, 0x01L}, {10L, 0x0AL}, {23L, 0x17L}, {24L, 0x1818L}, {1000L, 0x1903E8L},
                {-1L, 0x20L}, {-10L, 0x29L}, {-100L, 0x3863L}, {-1000L, 0x3903E7L}
        };
        for (int i = 0; i < rows.length; i++) {
            final long value = rows[i][0];
            final byte[] encoded = hex(Long.toHexString(rows[i][1]));
            assertArrayEquals(encoded, write(CborDeterministicInt.INSTANCE, value), "write row " + i);
            assertEquals(value, read(CborDeterministicInt.INSTANCE, encoded), "read row " + i);
        }
    }
}
