package com.github.jinahya.bit.io.miscellaneous.itu;

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

import com.github.jinahya.bit.io.miscellaneous.OfficialDocumentITSupport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class Asn1BerIntegerIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesBerIntegerContentOctetsFromX690() throws Exception {
        checkSource("https://www.itu.int/rec/T-REC-X.690", 128 * 1024,
                    document -> document.contains("X.690") || document.contains("ASN.1"),
                    "downloaded ITU-T X.690 page");

        assertArrayEquals(hex("00ff"), writeBerInteger(BigInteger.valueOf(255L)));
        assertArrayEquals(hex("ff01"), writeBerInteger(BigInteger.valueOf(-255L)));
        assertEquals(BigInteger.valueOf(255L), readBerInteger(hex("00ff")));
    }
}
