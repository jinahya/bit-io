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

import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.readRiceFlac;
import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.writeRiceFlac;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class RiceFLACIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesRiceCodedResidualExamplesFromRfc9639() throws Exception {
        final String document = downloadText("https://www.rfc-editor.org/rfc/rfc9639.txt", 768 * 1024);
        assertTrue(document.contains("Rice-coded residual") && document.contains("Table 48"), "downloaded RFC 9639");

        assertArrayEquals(bits("1110"), writeRiceFlac(3, 3L));        // RFC 9639 Table 48: q=0, r=6
        assertEquals(3L, readRiceFlac(3, bits("1110")));
        assertArrayEquals(bits("1001"), writeRiceFlac(3, -1L));       // RFC 9639 Table 48: q=0, r=1
        assertEquals(-1L, readRiceFlac(3, bits("1001")));
        assertArrayEquals(bits("0001001"), writeRiceFlac(3, -13L));   // RFC 9639 Table 48: q=3, r=1
        assertEquals(-13L, readRiceFlac(3, bits("0001001")));
    }
}
