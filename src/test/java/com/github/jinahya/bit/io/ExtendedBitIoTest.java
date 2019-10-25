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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.jinahya.bit.io.ExtendedBitInput.readAscii;
import static com.github.jinahya.bit.io.ExtendedBitInput.readObjects;
import static com.github.jinahya.bit.io.ExtendedBitInput.readString;
import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedVariable3;
import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedVariable4;
import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedVariable5;
import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedVariable6;
import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedVariableInt;
import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedVariableLong;
import static com.github.jinahya.bit.io.ExtendedBitInput.readVariableLengthQuantityInt;
import static com.github.jinahya.bit.io.ExtendedBitInput.readVariableLengthQuantityLong;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeAscii;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeObjects;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeString;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedVariable3;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedVariable4;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedVariable5;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedVariable6;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedVariableInt;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedVariableLong;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeVariableLengthQuantityInt;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeVariableLengthQuantityLong;
import static java.lang.StrictMath.pow;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class for unit-testing {@link ExtendedBitInput} class and {@link ExtendedBitOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class ExtendedBitIoTest {

    // --------------------------------------------------------------------------------------------- unsignedVariableInt
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariableInt(final BitOutput output, final BitInput input) throws IOException {
        final int expected = current().nextInt() >>> 1;
        writeUnsignedVariableInt(output, expected);
        output.align(1);
        final int actual = readUnsignedVariableInt(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------------------------- unsignedVariableInt
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariableLong(final BitOutput output, final BitInput input) throws IOException {
        final long expected = current().nextLong() >>> 1;
        writeUnsignedVariableLong(output, expected);
        output.align(1);
        final long actual = readUnsignedVariableLong(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------- unsignedVariable3

    /**
     * Tests {@link ExtendedBitOutput#writeUnsignedVariable3(BitOutput, byte)} method and {@link
     * ExtendedBitInput#readUnsignedVariable3(BitInput)} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable3_Zero(final BitOutput output, final BitInput input) throws IOException {
        final byte expected = 0;
        writeUnsignedVariable3(output, expected);
        output.align(1);
        final byte actual = readUnsignedVariable3(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable3_Max(final BitOutput output, final BitInput input) throws IOException {
        final byte expected = Byte.MAX_VALUE;
        writeUnsignedVariable3(output, expected);
        output.align(1);
        final byte actual = readUnsignedVariable3(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable3(final BitOutput output, final BitInput input) throws IOException {
        final byte expected = (byte) (current().nextInt() >>> 25);
        writeUnsignedVariable3(output, expected);
        output.align(1);
        final byte actual = readUnsignedVariable3(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // -------------------------------------------------------------------------------------------- unsignedIntVariable4

    /**
     * Tests {@link ExtendedBitOutput#writeUnsignedVariable4(BitOutput, short)} method and {@link
     * ExtendedBitInput#readUnsignedVariable4(BitInput)} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable4_Zero(final BitOutput output, final BitInput input) throws IOException {
        final short expected = 0;
        writeUnsignedVariable4(output, expected);
        output.align(1);
        final short actual = readUnsignedVariable4(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable4_Max(final BitOutput output, final BitInput input) throws IOException {
        final short expected = Short.MAX_VALUE;
        writeUnsignedVariable4(output, expected);
        output.align(1);
        final short actual = readUnsignedVariable4(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable4(final BitOutput output, final BitInput input) throws IOException {
        final short expected = (short) (current().nextInt() >>> 17);
        writeUnsignedVariable4(output, expected);
        output.align(1);
        final short actual = readUnsignedVariable4(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------- unsignedVariable5

    /**
     * Tests {@link ExtendedBitOutput#writeUnsignedVariable5(BitOutput, int)} method and {@link
     * ExtendedBitInput#readUnsignedVariable5(BitInput)} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable5_Zero(final BitOutput output, final BitInput input) throws IOException {
        final int expected = 0;
        writeUnsignedVariable5(output, expected);
        output.align(1);
        final int actual = readUnsignedVariable5(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable5_Max(final BitOutput output, final BitInput input) throws IOException {
        final int expected = Integer.MAX_VALUE;
        writeUnsignedVariable5(output, expected);
        output.align(1);
        final int actual = readUnsignedVariable5(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable5(final BitOutput output, final BitInput input) throws IOException {

        final int expected = current().nextInt() >>> 1;
        writeUnsignedVariable5(output, expected);
        output.align(1);
        final int actual = readUnsignedVariable5(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------- unsignedVariable6
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable6_Zero(final BitOutput output, final BitInput input) throws IOException {
        final long expected = 0L;
        writeUnsignedVariable6(output, expected);
        output.align(1);
        final long actual = readUnsignedVariable6(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable6_Max(final BitOutput output, final BitInput input) throws IOException {
        final long expected = Long.MAX_VALUE;
        writeUnsignedVariable6(output, expected);
        output.align(1);
        final long actual = readUnsignedVariable6(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testUnsignedVariable6(final BitOutput output, final BitInput input) throws IOException {
        final long expected = current().nextLong() >>> 1;
        writeUnsignedVariable6(output, expected);
        output.align(1);
        final long actual = readUnsignedVariable6(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testString(final BitOutput output, final BitInput input) throws IOException {
        final boolean nullable = current().nextBoolean();
        final String expected = nullable && current().nextBoolean()
                                ? null : new RandomStringGenerator.Builder().build().generate(current().nextInt(128));
        final Charset charset = UTF_8;
        writeString(nullable, output, expected, charset);
        output.align(1);
        final String actual = readString(nullable, input, charset);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testAscii(final BitOutput output, final BitInput input) throws IOException {
        final boolean nullable = current().nextBoolean();
        final String expected = nullable && current().nextBoolean()
                                ? null : new RandomStringGenerator.Builder().withinRange(0, 127).build()
                                        .generate(current().nextInt(128));
        writeAscii(nullable, output, expected);
        output.align(1);
        final String actual = readAscii(nullable, input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testVariableLengthQuantityInt_Zero(final BitOutput output, final BitInput input) throws IOException {
        final int expected = 0;
        final int size = current().nextInt(1, Integer.SIZE);
        writeVariableLengthQuantityInt(output, size, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testVariableLengthQuantityInt_Max(final BitOutput output, final BitInput input) throws IOException {
        final int expected = current().nextInt() >>> 1;
        final int size = current().nextInt(1, Integer.SIZE);
        writeVariableLengthQuantityInt(output, size, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testVariableLengthQuantityInt(final BitOutput output, final BitInput input) throws IOException {
        final int expected = current().nextInt() >>> 1;
        final int size = current().nextInt(1, Integer.SIZE);
        writeVariableLengthQuantityInt(output, size, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @Test
    void testVariableLengthQuantityInt_7() {
        // https://en.wikipedia.org/wiki/Variable-length_quantity#Examples
        final Map<Integer, byte[]> map = new HashMap<>();
        map.put(0x00000000, new byte[] {0b00000000});
        map.put(0x0000007F, new byte[] {0b01111111});
        map.put(0x00000080, new byte[] {(byte) 0b10000001, 0b00000000});
        map.put(0x00002000, new byte[] {(byte) 0b11000000, 0b00000000});
        map.put(0x00003FFF, new byte[] {(byte) 0b11111111, 0b01111111});
        map.put(0x00004000, new byte[] {(byte) 0b10000001, (byte) 0b10000000, 0b00000000});
        map.put(0x001FFFFF, new byte[] {(byte) 0b11111111, (byte) 0b11111111, 0b01111111});
        map.put(0x00200000, new byte[] {(byte) 0b10000001, (byte) 0b10000000, (byte) 0b10000000, 0b00000000});
        map.put(0x08000000, new byte[] {(byte) 0b11000000, (byte) 0b10000000, (byte) 0b10000000, 0b00000000});
        map.put(0x0FFFFFFF, new byte[] {(byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, 0b01111111});
        map.forEach((k, v) -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
            final BitInput input = new DefaultBitInput(new StreamByteInput(null) {
                @Override
                public int read() throws IOException {
                    if (getSource() == null) {
                        setSource(new ByteArrayInputStream(baos.toByteArray()));
                    }
                    return super.read();
                }
            });
            try {
                writeVariableLengthQuantityInt(output, 7, k);
                output.align(1);
                final int actual = readVariableLengthQuantityInt(input, 7);
                input.align(1);
                assertEquals(k, actual);
                assertArrayEquals(v, baos.toByteArray());
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testVariableLengthQuantityLong_Zero(final BitOutput output, final BitInput input) throws IOException {
        final long expected = 0L;
        final int size = current().nextInt(1, Long.SIZE);
        writeVariableLengthQuantityLong(output, size, expected);
        output.align(1);
        final long actual = readVariableLengthQuantityLong(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testVariableLengthQuantityLong_Max(final BitOutput output, final BitInput input) throws IOException {
        final long expected = Long.MAX_VALUE;
        final int size = current().nextInt(1, Long.SIZE);
        writeVariableLengthQuantityLong(output, size, expected);
        output.align(1);
        final long actual = readVariableLengthQuantityLong(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testVariableLengthQuantityLong(final BitOutput output, final BitInput input) throws IOException {
        final long expected = current().nextLong() >>> 1;
        final int size = current().nextInt(1, Long.SIZE);
        writeVariableLengthQuantityLong(output, size, expected);
        output.align(1);
        final long actual = readVariableLengthQuantityLong(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @Data
    static final class User implements BitReadable, BitWritable {

        @Override
        public void read(BitInput input) throws IOException {
            name = readString(true, input, UTF_8);
            age = input.readInt(true, 7);
        }

        @Override
        public void write(BitOutput output) throws IOException {
            writeString(true, output, name, UTF_8);
            output.writeInt(true, 7, age);
        }

        String name;

        int age;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.BitIoSource#sourceBitIo"})
    @ParameterizedTest
    void testObjects(final BitOutput output, final BitInput input) throws IOException {
        final boolean nullable = current().nextBoolean();
        List<User> expected = null;
        if (!nullable) {
            expected = new ArrayList<>();
        } else if (current().nextBoolean()) {
            final RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
            final int size = current().nextInt(128);
            expected = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                final User user = new User();
                user.name = generator.generate(current().nextInt(128));
                user.age = current().nextInt(128);
                expected.add(user);
            }
        }
        writeObjects(nullable, output, expected);
        output.align(1);
        final List<User> actual = readObjects(nullable, input, User.class);
        input.align(1);
        assertEquals(expected, actual);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Disabled
    @Test
    void write() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        final File file = new File("/users/onacit/Documents/test.csv");
        try (Writer os = new FileWriter(file)) {
            os.write("e, UVI,UVI5,VQL\r\n");
            range(0, Integer.MAX_VALUE).map(i -> (int) pow(2, i)).limit(1000).filter(i -> i >= 0).forEach(i -> {
                try {
                    os.write(i + ",");
                    {
                        writeUnsignedVariableInt(output, i);
                        final long d = output.align(1);
                        os.write(baos.size() * Byte.SIZE - d + ",");
                        baos.reset();
                    }
                    {
                        writeUnsignedVariable5(output, i);
                        final long d = output.align(1);
                        os.write(baos.size() * Byte.SIZE - d + ",");
                        baos.reset();
                    }
                    {
                        writeVariableLengthQuantityInt(output, 7, i);
                        final long d = output.align(1);
                        os.write(baos.size() * Byte.SIZE - d + ",");
                        baos.reset();
                    }
                    os.write("\r\n");
                } catch (final IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            });
            os.flush();
        }
    }
}
