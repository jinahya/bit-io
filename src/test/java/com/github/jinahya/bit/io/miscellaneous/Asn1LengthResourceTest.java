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

import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.readBer;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.readDer;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.writeBer;
import static com.github.jinahya.bit.io.miscellaneous.Asn1LengthTestUtils.writeDer;
import static com.github.jinahya.bit.io.miscellaneous.MiscResourceTestUtils.hexBytes;
import static com.github.jinahya.bit.io.miscellaneous.MiscResourceTestUtils.readTsv;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Asn1LengthResourceTest {

    @Test
    void verifiesX690DefiniteLengthVectors() throws IOException {
        final List<String[]> rows = readTsv("asn1-length/x690-8.1.3.tsv");
        for (int i = 0; i < rows.size(); i++) {
            final String[] row = rows.get(i);
            final long value = Long.parseLong(row[0]);
            final byte[] bytes = hexBytes(row[1]);
            assertArrayEquals(bytes, writeDer(value), "DER write row " + i);
            assertEquals(value, readDer(bytes), "DER read row " + i);
            assertArrayEquals(bytes, writeBer(value), "BER write row " + i);
            assertEquals(value, readBer(bytes), "BER read row " + i);
        }
    }
}
