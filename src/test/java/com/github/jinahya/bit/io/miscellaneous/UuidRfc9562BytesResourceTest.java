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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteInput;
import com.github.jinahya.bit.io.StreamByteOutput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.github.jinahya.bit.io.miscellaneous.MiscResourceTestUtils.hexBytes;
import static com.github.jinahya.bit.io.miscellaneous.MiscResourceTestUtils.readTsv;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UuidRfc9562BytesResourceTest {

    @Test
    void verifiesRfc9562AppendixABytes() throws IOException {
        final List<String[]> rows = readTsv("uuid/rfc9562-appendix-a.tsv");
        for (int i = 0; i < rows.size(); i++) {
            final String[] row = rows.get(i);
            final String canonical = row[1];
            final byte[] bytes = hexBytes(row[2]);
            assertEquals(canonical.replace("-", ""), row[2], "canonical bytes row " + i);
            assertArrayEquals(bytes, read(bytes), "read row " + i);
            assertArrayEquals(bytes, write(bytes), "write row " + i);
        }
    }

    private static byte[] read(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return UuidRfc9562Bytes.INSTANCE.read(input);
    }

    private static byte[] write(final byte[] value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        UuidRfc9562Bytes.INSTANCE.write(output, value);
        output.align(1);
        return baos.toByteArray();
    }
}
