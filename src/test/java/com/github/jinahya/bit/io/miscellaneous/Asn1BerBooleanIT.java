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
class Asn1BerBooleanIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesBerBooleanContentOctetsFromX690() throws Exception {
        final String document = downloadText("https://www.itu.int/rec/T-REC-X.690", 128 * 1024);
        assertTrue(document.contains("X.690") || document.contains("ASN.1"), "downloaded ITU-T X.690 page");

        assertEquals(Boolean.FALSE, read(Asn1BerBoolean.INSTANCE, hex("00")));
        assertEquals(Boolean.TRUE, read(Asn1BerBoolean.INSTANCE, hex("01")));
        assertEquals(Boolean.TRUE, read(Asn1BerBoolean.INSTANCE, hex("ff")));
        assertArrayEquals(hex("ff"), write(Asn1BerBoolean.INSTANCE, Boolean.TRUE));
    }
}
