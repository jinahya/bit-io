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
class CborSimpleValueIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesSimpleValueExamplesFromRfc8949() throws Exception {
        final String document = downloadText("https://www.rfc-editor.org/rfc/rfc8949.txt", 512 * 1024);
        assertTrue(document.contains("CBOR Simple Values Registry"), "downloaded RFC 8949");

        final int[][] rows = {
                {20, 0xF4}, {21, 0xF5}, {22, 0xF6}, {23, 0xF7}, {32, 0xF820}, {255, 0xF8FF}
        };
        for (int i = 0; i < rows.length; i++) {
            final int value = rows[i][0];
            final byte[] encoded = hex(Integer.toHexString(rows[i][1]));
            assertArrayEquals(encoded, write(CborSimpleValue.INSTANCE, value), "write row " + i);
            assertEquals(value, read(CborSimpleValue.INSTANCE, encoded), "read row " + i);
        }
    }
}
