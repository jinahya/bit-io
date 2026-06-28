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
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteInput;
import com.github.jinahya.bit.io.StreamByteOutput;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

abstract class OfficialDocumentITSupport {

    protected String downloadText(final String url, final int maxBytes) throws IOException {
        final URLConnection connection = new URL(url).openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream input = connection.getInputStream()) {
            final byte[] buffer = new byte[4096];
            int count;
            while ((count = input.read(buffer)) != -1) {
                if (baos.size() + count > maxBytes) {
                    throw new IOException("download exceeds " + maxBytes + " bytes: " + url);
                }
                baos.write(buffer, 0, count);
            }
        }
        final byte[] bytes = baos.toByteArray();
        Files.write(tempDir.resolve(url.replaceAll("[^A-Za-z0-9._-]", "_")), bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected static <T> T read(final BitReader<T> reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return reader.read(input);
    }

    protected static <T> byte[] write(final BitWriter<T> writer, final T value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        writer.write(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static BigInteger readDerInteger(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return Asn1DerInteger.readContent(input, bytes.length);
    }

    protected static byte[] writeDerInteger(final BigInteger value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1DerInteger.writeContent(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static BigInteger readBerInteger(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return Asn1BerInteger.readContent(input, bytes.length);
    }

    protected static byte[] writeBerInteger(final BigInteger value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1BerInteger.writeContent(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static long[] readOid(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return Asn1Oid.readContent(input, bytes.length);
    }

    protected static byte[] writeOid(final long[] value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1Oid.writeContent(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static byte[] hex(final String value) {
        return _Utils.fromHex(value);
    }

    protected static String hex(final byte[] value) {
        return _Utils.toHex(value);
    }

    protected static byte[] bits(final String value) {
        final byte[] bytes = new byte[(value.length() + Byte.SIZE - 1) / Byte.SIZE];
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '1') {
                bytes[i / Byte.SIZE] |= (byte) (1 << (Byte.SIZE - 1 - (i % Byte.SIZE)));
            } else if (value.charAt(i) != '0') {
                throw new IllegalArgumentException("invalid bit: " + value.charAt(i));
            }
        }
        return bytes;
    }

    @TempDir
    Path tempDir;
}
