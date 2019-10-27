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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTests.randomSizeForChar;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForShort;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForChar;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForInt;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForShort;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A class for unit-testing {@link AbstractBitOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@ExtendWith({MockitoExtension.class})
@Slf4j
public class AbstractBitOutputSpyTest {

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void stubWrite() throws IOException {
        //doNothing().when(bitOutput).write(anyInt());
    }

    // --------------------------------------------------------------------------------------------------------- boolean
    @RepeatedTest(8)
    void testWriteBoolean() throws IOException {
        final boolean value = current().nextBoolean();
        bitOutput.writeBoolean(value);
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @RepeatedTest(8)
    void testWriteByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = BitIoTests.randomSizeForByte(unsigned);
        final byte value = BitIoTests.randomValueForByte(unsigned, size);
        bitOutput.writeByte(unsigned, size, value);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @RepeatedTest(8)
    void testWriteShort() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForShort(unsigned);
        final short value = randomValueForShort(unsigned, size);
        bitOutput.writeShort(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @RepeatedTest(8)
    void testWriteInt() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForInt(unsigned);
        final int value = randomValueForInt(unsigned, size);
        bitOutput.writeInt(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @RepeatedTest(8)
    void testWriteSignedLong() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForLong(unsigned);
        final long value = randomValueForLong(unsigned, size);
        bitOutput.writeLong(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @RepeatedTest(8)
    void testWriteChar() throws IOException {
        final int size = randomSizeForChar();
        final char value = randomValueForChar(size);
        bitOutput.writeChar(size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Spy
    private AbstractBitOutput bitOutput;
}
