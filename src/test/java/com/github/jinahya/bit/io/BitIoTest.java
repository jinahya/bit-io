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
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class for unit-testing {@link ExtendedBitInput} class and {@link ExtendedBitOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class BitIoTest {

    // --------------------------------------------------------------------------------------------------------- boolean
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testBoolean(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean expected = current().nextBoolean();
        output.writeBoolean(expected);
        output.align(1);
        final boolean actual = input.readBoolean();
        input.align(1);
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testByte(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(BitInputConverter.class) final BitInput input) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForByte(unsigned);
        final byte expected = randomValueForByte(unsigned, size);
        output.writeByte(unsigned, size, expected);
        output.align(1);
        final byte actual = input.readByte(unsigned, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testShort(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(BitInputConverter.class) final BitInput input) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForShort(unsigned);
        final short expected = randomValueForShort(unsigned, size);
        output.writeShort(unsigned, size, expected);
        output.align(1);
        final short actual = input.readShort(unsigned, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testInt(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                 @ConvertWith(BitInputConverter.class) final BitInput input) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForInt(unsigned);
        final int expected = randomValueForInt(unsigned, size);
        output.writeInt(unsigned, size, expected);
        output.align(1);
        final int actual = input.readInt(unsigned, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void testSignedLong(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                        @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForLong(unsigned);
        final long expected = randomValueForLong(unsigned, size);
        output.writeLong(unsigned, size, expected);
        output.align(1);
        final long actual = input.readLong(unsigned, size);
        input.align(1);
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
        output.align(1);
        final char actual = input.readChar(size);
        input.align(1);
        assertEquals(expected, actual);
    }
}
