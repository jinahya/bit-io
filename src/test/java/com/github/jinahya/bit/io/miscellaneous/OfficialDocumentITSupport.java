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
import com.github.jinahya.bit.io.BooleanBitReader;
import com.github.jinahya.bit.io.BooleanBitWriter;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.IntBitReader;
import com.github.jinahya.bit.io.IntBitWriter;
import com.github.jinahya.bit.io.LongBitReader;
import com.github.jinahya.bit.io.LongBitWriter;
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

    protected static boolean read(final BooleanBitReader reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return input.readBoolean(reader);
    }

    protected static int read(final IntBitReader reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return input.readInt(reader);
    }

    protected static long read(final LongBitReader reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return input.readLong(reader);
    }

    protected static <T> byte[] write(final BitWriter<T> writer, final T value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        writer.write(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static byte[] write(final BooleanBitWriter writer, final boolean value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeBoolean(writer, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static byte[] write(final IntBitWriter writer, final int value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeInt(writer, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static byte[] write(final LongBitWriter writer, final long value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeLong(writer, value);
        output.align(1);
        return baos.toByteArray();
    }

    protected static BigInteger readDerInteger(final byte[] bytes) throws IOException {
        if (bytes.length == 0) {
            throw new IllegalArgumentException("bytes.length == 0");
        }
        if (bytes.length > 1) {
            final int first = bytes[0] & 0xFF;
            final int second = bytes[1] & 0xFF;
            if (first == 0x00 && (second & 0x80) == 0) {
                throw new IOException("non-minimal DER INTEGER content");
            }
            if (first == 0xFF && (second & 0x80) != 0) {
                throw new IOException("non-minimal DER INTEGER content");
            }
        }
        return new BigInteger(bytes);
    }

    protected static byte[] writeDerInteger(final BigInteger value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return value.toByteArray();
    }

    protected static BigInteger readBerInteger(final byte[] bytes) throws IOException {
        if (bytes.length == 0) {
            throw new IllegalArgumentException("bytes.length == 0");
        }
        return new BigInteger(bytes);
    }

    protected static byte[] writeBerInteger(final BigInteger value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return value.toByteArray();
    }

    protected static long[] readOid(final byte[] bytes) throws IOException {
        final ByteArrayOutputStream prefixed = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(prefixed));
        output.writeObject(new com.github.jinahya.bit.io.ByteArrayWriter.Signed8(31), bytes);
        output.align(1);
        return read(Asn1Oid.INSTANCE, prefixed.toByteArray());
    }

    protected static byte[] writeOid(final long[] value) throws IOException {
        final byte[] encoded = write(Asn1Oid.INSTANCE, value);
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(encoded)));
        return input.readObject(new com.github.jinahya.bit.io.ByteArrayReader.Signed8(31));
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
