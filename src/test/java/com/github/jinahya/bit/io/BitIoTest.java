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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTests.randomSizeForByte;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForChar;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForShort;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForByte;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForChar;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForInt;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForShort;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A class for unit-testing {@link ExtendedBitInput} class and {@link ExtendedBitOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
@Disabled("Reconstructing the test module")
class BitIoTest {

    // --------------------------------------------------------------------------------------------------------- boolean
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testBoolean(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean expected = current().nextBoolean();
        output.writeBoolean(expected);
        assertEquals(7, output.align(Byte.BYTES));
        final boolean actual = input.readBoolean();
        assertEquals(7, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testByte(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForByte(unsigned);
        final byte expected = randomValueForByte(unsigned, size);
        output.writeByte(unsigned, size, expected);
        assertTrue(output.align(Byte.BYTES) < Byte.SIZE);
        final byte actual = input.readByte(unsigned, size);
        assertTrue(input.align(Byte.BYTES) < Byte.SIZE);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testByte8(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final byte expected = (byte) current().nextInt();
        output.writeByte8(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final byte actual = input.readByte8();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testShort(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForShort(unsigned);
        final short expected = randomValueForShort(unsigned, size);
        output.writeShort(unsigned, size, expected);
        assertTrue(output.align(Byte.BYTES) < Byte.SIZE);
        final short actual = input.readShort(unsigned, size);
        assertTrue(input.align(Byte.BYTES) < Byte.SIZE);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testShort16(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final short expected = (short) current().nextInt();
        output.writeShort16(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final short actual = input.readShort16();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testShort16Le(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                       @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final short expected = (short) current().nextInt();
        output.writeShort16Le(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final short actual = input.readShort16Le();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testInt(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                 @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForInt(unsigned);
        final int expected = randomValueForInt(unsigned, size);
        output.writeInt(unsigned, size, expected);
        assertTrue(output.align(Byte.BYTES) < Byte.SIZE);
        final int actual = input.readInt(unsigned, size);
        assertTrue(input.align(Byte.BYTES) < Byte.SIZE);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testInt32(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int expected = current().nextInt();
        output.writeInt32(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final int actual = input.readInt32();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testInt32Le(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int expected = current().nextInt();
        output.writeInt32Le(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final int actual = input.readInt32Le();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testLong(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForLong(unsigned);
        final long expected = randomValueForLong(unsigned, size);
        output.writeLong(unsigned, size, expected);
        assertTrue(output.align(Byte.BYTES) < Byte.SIZE);
        final long actual = input.readLong(unsigned, size);
        assertTrue(input.align(Byte.BYTES) < Byte.SIZE);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testLong64(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final long expected = current().nextLong();
        output.writeLong64(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final long actual = input.readLong64();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testLong64Le(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                      @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final long expected = current().nextLong();
        output.writeLong64Le(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final long actual = input.readLong64Le();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testChar(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForChar();
        final char expected = randomValueForChar(size);
        output.writeChar(size, expected);
        assertTrue(output.align(Byte.BYTES) < Byte.SIZE);
        final char actual = input.readChar(size);
        assertTrue(input.align(Byte.BYTES) < Byte.SIZE);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testChar16(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final char expected = (char) current().nextInt(0x00, Character.MAX_VALUE);
        output.writeChar16(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final char actual = input.readChar16();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testChar16Le(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                      @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final char expected = (char) current().nextInt(0x00, Character.MAX_VALUE);
        output.writeChar16Le(expected);
        assertEquals(0L, output.align(Byte.BYTES));
        final char actual = input.readChar16Le();
        assertEquals(0L, input.align(Byte.BYTES));
        assertEquals(expected, actual);
    }

    // ---------------------------------------------------------------------------------------------------------- byte[]
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testBytes(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int lengthSize = current().nextInt(1, Integer.SIZE);       // 1..31
        final int elementSize = current().nextInt(1, Byte.SIZE + 1);     // 1..8 (signed)
        // keep the packed payload within the test sinks (ByteIoSource.CAPACITY == 128 bytes)
        final long maxLength = Math.min((1L << lengthSize) - 1L, 100L);
        final int length = current().nextInt(0, (int) maxLength + 1);
        final byte[] expected = new byte[length];
        for (int i = 0; i < length; i++) {
            expected[i] = (byte) (current().nextInt() >> (Integer.SIZE - elementSize)); // fits signed elementSize bits
        }
        output.writeBytes(lengthSize, elementSize, expected);
        output.align(Byte.BYTES);
        final byte[] actual = input.readBytes(lengthSize, elementSize);
        input.align(Byte.BYTES);
        assertArrayEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------------------- ascii
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testAscii(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                         @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final String expected = "Hello, bit-io! 0123456789 ~ABCxyz";
        output.writeAscii(16, expected);                           // 7-bit unsigned elements
        output.align(Byte.BYTES);
        final String actual = input.readAscii(16);
        input.align(Byte.BYTES);
        assertEquals(expected, actual);

        output.writeAscii31(expected);
        output.align(Byte.BYTES);
        assertEquals(expected, input.readAscii31());
        input.align(Byte.BYTES);
    }

    // ---------------------------------------------------------------------------------------------------------- string
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testString(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final String expected = new StringBuilder("bit-io ")
                .appendCodePoint(0x00E9)   // e-acute
                .appendCodePoint(0x4E16)   // CJK
                .appendCodePoint(0x754C)   // CJK
                .append(" 0123 ~").toString();                          // multi-byte UTF-8
        output.writeString(16, "UTF-8", expected);
        output.align(Byte.BYTES);
        final String actual = input.readString(16, "UTF-8");
        input.align(Byte.BYTES);
        assertEquals(expected, actual);

        output.writeString31("UTF-8", expected);
        output.align(Byte.BYTES);
        assertEquals(expected, input.readString31("UTF-8"));
        input.align(Byte.BYTES);
    }

    // ------------------------------------------------------------------------------------------------------------ skip

    /**
     * Tests {@link BitOutput#skip(int)} method and {@link BitInput#skip(int)} method.
     *
     * @param output an instance of bit output.
     * @param input  an instance of bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testSkip(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int bits = current().nextInt(1, 9);
        output.skip(bits);
        output.align(1);
        input.skip(bits);
    }
}
