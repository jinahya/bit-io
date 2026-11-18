package com.github.jinahya.bit.io.miscellaneous.mongodb;

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

import com.github.jinahya.bit.io.ArrayByteInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;
import com.github.jinahya.bit.io.miscellaneous.OfficialDocumentITSupport;
import com.github.jinahya.bit.io.miscellaneous.ieee.Decimal;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Runs every valid {@code Decimal128} entry of the BSON Corpus through {@link BsonDecimal128}. The corpus is not
 * redistributable with this project, so this test downloads it, and runs only with the {@code live-sources} profile.
 *
 * @see <a href="https://github.com/mongodb/specifications/tree/master/source/bson-corpus">BSON Corpus</a>
 */
class BsonDecimal128IT
        extends OfficialDocumentITSupport {

    private static final String BASE =
            "https://raw.githubusercontent.com/mongodb/specifications/master/source/bson-corpus/tests/decimal128-";

    private static final Pattern DESCRIPTION = Pattern.compile("\"description\"\\s*:\\s*\"([^\"]*)\"");

    private static final Pattern CANONICAL_BSON = Pattern.compile("\"canonical_bson\"\\s*:\\s*\"([0-9A-Fa-f]+)\"");

    private static final Pattern CANONICAL_EXTJSON = Pattern.compile(
            "\"canonical_extjson\"\\s*:\\s*\"[^\\n]*?\\$numberDecimal\\\\\"\\s*:\\s*\\\\\"([^\\\\]*)\\\\\"");

    @Test
    void verifiesEveryValidEntryOfTheBsonCorpus() throws IOException {
        assumeTrue(liveSources(), "live sources are disabled");
        int entries = 0;
        for (int i = 1; i <= 7; i++) {
            final String document = checkSource(BASE + i + ".json", 512 * 1024,
                                                d -> d.contains("\"bson_type\": \"0x13\""), "decimal128-" + i);
            final Matcher matcher = CANONICAL_BSON.matcher(document);
            while (matcher.find()) {
                final String bson = matcher.group(1);
                // the entry's description precedes its canonical_bson; its canonical_extjson follows
                final Matcher descriptions = DESCRIPTION.matcher(document.substring(0, matcher.start()));
                String last = null;
                while (descriptions.find()) {
                    last = descriptions.group(1);
                }
                final String description = String.valueOf(last);
                final Matcher extjson = CANONICAL_EXTJSON.matcher(document);
                assertTrue(extjson.find(matcher.end()), "no canonical_extjson after " + bson);
                final String string = extjson.group(1);
                // the value bytes: int32 length, 0x13, "d", 0x00, then 16 bytes
                final byte[] bytes = Arrays.copyOfRange(hex(bson), 7, 7 + BsonDecimal128.BYTES);
                final Decimal value = BsonDecimal128.INSTANCE.read(new DefaultBitInput(new ArrayByteInput(bytes)));
                assertEquals(string, value.isNaN() ? "NaN" : value.toString(), description);
                if (!description.contains("Invalid representation")) {
                    assertArrayEquals(bytes, encode(value), description);
                    if (!value.isNaN()) {
                        assertArrayEquals(bytes, encode(Decimal.parse(string)), description);
                    }
                }
                entries++;
            }
        }
        assertTrue(entries > 600, "entries: " + entries);
    }

    private static byte[] encode(final Decimal value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(bytes));
        BsonDecimal128.INSTANCE.write(output, value);
        output.align(1);
        return bytes.toByteArray();
    }
}
