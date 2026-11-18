package com.github.jinahya.bit.io.miscellaneous.ieee;

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

import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;
import com.github.jinahya.bit.io.miscellaneous.OfficialDocumentITSupport;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Runs the encoding testcases of the General Decimal Arithmetic testcases ({@code dsEncode.decTest},
 * {@code ddEncode.decTest}, {@code dqEncode.decTest}) through {@link Decimal32Dpd}, {@link Decimal64Dpd}, and
 * {@link Decimal128Dpd}: every decoding, every canonicalization, and every encoding which needs neither rounding nor
 * clamping. The testcases are not redistributable with this project, so this test downloads them, and runs only with
 * the {@code live-sources} profile.
 *
 * @see <a href="https://speleotrove.com/decimal/dectest.html">General Decimal Arithmetic Testcases</a>
 */
class DecimalDpdIT
        extends OfficialDocumentITSupport {

    private static final Pattern TESTCASE = Pattern.compile("^(\\S+)\\s+apply\\s+(\\S+)\\s+->\\s+(\\S+)\\s*(.*)$");

    @Test
    void verifiesTheEncodingTestcases() throws IOException {
        assumeTrue(liveSources(), "live sources are disabled");
        final byte[] zip = checkSourceBytes("https://speleotrove.com/decimal/dectest.zip", 4 * 1024 * 1024);
        final Map<String, Object> codecs = new HashMap<String, Object>();
        codecs.put("dsEncode.decTest", Decimal32Dpd.INSTANCE);
        codecs.put("ddEncode.decTest", Decimal64Dpd.INSTANCE);
        codecs.put("dqEncode.decTest", Decimal128Dpd.INSTANCE);
        int files = 0;
        final ZipInputStream entries = new ZipInputStream(new ByteArrayInputStream(zip));
        for (ZipEntry entry; (entry = entries.getNextEntry()) != null; ) {
            final String name = entry.getName().substring(entry.getName().lastIndexOf('/') + 1);
            final Object codec = codecs.get(name);
            if (codec == null) {
                continue;
            }
            @SuppressWarnings("unchecked") final BitReader<Decimal> reader = (BitReader<Decimal>) codec;
            @SuppressWarnings("unchecked") final BitWriter<Decimal> writer = (BitWriter<Decimal>) codec;
            int checked = 0;
            final BufferedReader lines = new BufferedReader(new InputStreamReader(entries, StandardCharsets.UTF_8));
            for (String line; (line = lines.readLine()) != null; ) {
                final Matcher matcher = TESTCASE.matcher(line.replaceAll("--.*$", "").trim());
                if (!matcher.matches()) {
                    continue;
                }
                final String id = matcher.group(1);
                final String input = matcher.group(2);
                final String result = matcher.group(3);
                final String conditions = matcher.group(4).trim();
                if (input.startsWith("#") && result.startsWith("#")) { // canonicalization
                    assertEquals(result.substring(1).toLowerCase(),
                                 DecimalDpdTest.write(writer, DecimalDpdTest.read(reader, input.substring(1))), id);
                    checked++;
                } else if (input.startsWith("#")) { // decoding
                    assertEquals(result, DecimalDpdTest.read(reader, input.substring(1)).toString(), id);
                    checked++;
                } else if (result.startsWith("#") && (conditions.isEmpty() || conditions.equals("Subnormal"))) {
                    assertEquals(result.substring(1).toLowerCase(),
                                 DecimalDpdTest.write(writer, Decimal.parse(input.replace("'", ""))), id);
                    checked++;
                }
            }
            assertTrue(checked > 200, name + ": " + checked);
            files++;
        }
        assertEquals(3, files);
    }
}
