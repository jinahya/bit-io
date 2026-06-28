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

import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.readSe;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.writeSe;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class ExpGolombSEIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesSignedExpGolombTableFromH264H265() throws Exception {
        final String h264 = downloadText("https://www.itu.int/rec/T-REC-H.264", 128 * 1024);
        final String h265 = downloadText("https://www.itu.int/rec/T-REC-H.265", 128 * 1024);
        assertTrue(h264.contains("H.264") || h264.contains("14496-10"), "downloaded ITU-T H.264 page");
        assertTrue(h265.contains("H.265") || h265.contains("23008-2"), "downloaded ITU-T H.265 page");

        final Object[][] rows = {
                {Long.valueOf(0L), "1"}, {Long.valueOf(1L), "010"}, {Long.valueOf(-1L), "011"},
                {Long.valueOf(2L), "00100"}, {Long.valueOf(-2L), "00101"}
        };
        for (int i = 0; i < rows.length; i++) {
            final long value = ((Long) rows[i][0]).longValue();
            final byte[] encoded = bits((String) rows[i][1]);
            assertArrayEquals(encoded, writeSe(value), "write row " + i);
            assertEquals(value, readSe(encoded), "read row " + i);
        }
    }
}
