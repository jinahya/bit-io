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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.readSe;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.readUe;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.writeSe;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.writeUe;
import static com.github.jinahya.bit.io.miscellaneous.MiscResourceTestUtils.bitsToBytes;
import static com.github.jinahya.bit.io.miscellaneous.MiscResourceTestUtils.readTsv;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpGolombResourceTest {

    @Test
    void verifiesH264Table92UnsignedVectors() throws IOException {
        final List<String[]> rows = readTsv("exp-golomb/h264-table-9-2-ue.tsv");
        for (int i = 0; i < rows.size(); i++) {
            final String[] row = rows.get(i);
            final long value = Long.parseLong(row[0]);
            final byte[] bytes = bitsToBytes(row[1]);
            assertArrayEquals(bytes, writeUe(value), "write row " + i);
            assertEquals(value, readUe(bytes), "read row " + i);
        }
    }

    @Test
    void verifiesH264Table93SignedVectors() throws IOException {
        final List<String[]> rows = readTsv("exp-golomb/h264-table-9-3-se.tsv");
        for (int i = 0; i < rows.size(); i++) {
            final String[] row = rows.get(i);
            final long value = Long.parseLong(row[0]);
            final byte[] bytes = bitsToBytes(row[1]);
            assertArrayEquals(bytes, writeSe(value), "write row " + i);
            assertEquals(value, readSe(bytes), "read row " + i);
        }
    }
}
