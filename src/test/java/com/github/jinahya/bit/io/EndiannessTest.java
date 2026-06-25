package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class EndiannessTest {

    @Test
    void writesBigEndianFixedWidthValues() throws IOException {
        final byte[] bytes = write(new BitWriter() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeShort16((short) 0x1234);
                output.writeInt32(0x12345678);
                output.writeLong64(0x0123456789ABCDEFL);
                output.writeChar16((char) 0xCAFE);
            }
        });
        assertArrayEquals(new byte[]{
                0x12, 0x34,
                0x12, 0x34, 0x56, 0x78,
                0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
                (byte) 0xCA, (byte) 0xFE
        }, bytes);
    }

    @Test
    void writesLittleEndianFixedWidthValues() throws IOException {
        final byte[] bytes = write(new BitWriter() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeShort16Le((short) 0x1234);
                output.writeInt32Le(0x12345678);
                output.writeLong64Le(0x0123456789ABCDEFL);
                output.writeChar16Le((char) 0xCAFE);
            }
        });
        assertArrayEquals(new byte[]{
                0x34, 0x12,
                0x78, 0x56, 0x34, 0x12,
                (byte) 0xEF, (byte) 0xCD, (byte) 0xAB, (byte) 0x89, 0x67, 0x45, 0x23, 0x01,
                (byte) 0xFE, (byte) 0xCA
        }, bytes);
    }

    private static byte[] write(final BitWriter writer) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        writer.write(output);
        output.align(1);
        return baos.toByteArray();
    }

    private interface BitWriter {

        void write(BitOutput output) throws IOException;
    }
}
