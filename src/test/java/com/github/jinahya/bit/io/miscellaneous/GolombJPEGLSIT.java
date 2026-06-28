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

import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.readGolombJpegLs;
import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.writeGolombJpegLs;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class GolombJPEGLSIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesGolombErrorCodingExamplesFromT87() throws Exception {
        final String document = downloadText("https://www.itu.int/rec/T-REC-T.87", 128 * 1024);
        assertTrue(document.contains("T.87") || document.contains("JPEG-LS"), "downloaded ITU-T T.87 page");

        assertArrayEquals(bits("000"), writeGolombJpegLs(2, 0L));
        assertArrayEquals(bits("001"), writeGolombJpegLs(2, -1L));
        assertArrayEquals(bits("010"), writeGolombJpegLs(2, 1L));
        assertArrayEquals(bits("1000"), writeGolombJpegLs(2, 2L));
        assertEquals(2L, readGolombJpegLs(2, bits("1000")));
    }
}
