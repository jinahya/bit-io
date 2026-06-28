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
class CborGenericIntIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesGenericIntegerExamplesFromRfc8949() throws Exception {
        final String document = downloadText("https://www.rfc-editor.org/rfc/rfc8949.txt", 512 * 1024);
        assertTrue(document.contains("Appendix A.  Examples of Encoded CBOR Data Items"), "downloaded RFC 8949");

        assertArrayEquals(hex("1818"), write(CborGenericInt.INSTANCE, Long.valueOf(24L)));
        assertEquals(Long.valueOf(0L), read(CborGenericInt.INSTANCE, hex("1800")));
        assertEquals(Long.valueOf(-100L), read(CborGenericInt.INSTANCE, hex("3863")));
    }
}
