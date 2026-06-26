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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

final class MiscResourceTestUtils {

    static List<String[]> readTsv(final String name) throws IOException {
        final InputStream stream = MiscResourceTestUtils.class.getResourceAsStream(name);
        if (stream == null) {
            throw new IOException("resource not found: " + name);
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        try {
            final List<String[]> rows = new ArrayList<String[]>();
            for (String line; (line = reader.readLine()) != null; ) {
                line = line.trim();
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue;
                }
                rows.add(line.split("\\t"));
            }
            return rows;
        } finally {
            reader.close();
        }
    }

    static byte[] hexBytes(final String value) {
        final String hex = value.replace(" ", "");
        if ((hex.length() & 0x01) != 0) {
            throw new IllegalArgumentException("odd hex length: " + value);
        }
        final byte[] bytes = new byte[hex.length() >> 1];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i << 1, (i << 1) + 2), 16);
        }
        return bytes;
    }

    static byte[] bitsToBytes(final String bits) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int value = 0;
        int count = 0;
        for (int i = 0; i < bits.length(); i++) {
            final char bit = bits.charAt(i);
            if (bit != '0' && bit != '1') {
                throw new IllegalArgumentException("invalid bit: " + bit);
            }
            value = (value << 1) | (bit == '1' ? 1 : 0);
            if (++count == Byte.SIZE) {
                baos.write(value);
                value = 0;
                count = 0;
            }
        }
        if (count > 0) {
            baos.write(value << (Byte.SIZE - count));
        }
        return baos.toByteArray();
    }

    private MiscResourceTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
