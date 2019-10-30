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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTests.randomSizeForByte;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForChar;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForShort;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A class for unit-testing {@link AbstractBitInput} with a spy.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see AbstractBitOutputSpyTest
 */
@ExtendWith({MockitoExtension.class})
@Slf4j
public class AbstractBitInputSpyTest {

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void stubRead() throws IOException {
        //when(bitInput.read()).thenReturn(current().nextInt(0, 256));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @AfterEach
    void alignAfterEach() throws IOException {
        bitInput.align(current().nextInt(1, 8));
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @RepeatedTest(8)
    void testReadByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForByte(unsigned);
        final byte value = bitInput.readByte(unsigned, size);
        if (unsigned) {
            assertEquals(0, value >> size);
        } else {
            if (value >= 0) {
                assertEquals(0, value >> (size - 1));
            } else {
                assertEquals(-1, value >> (size - 1));
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @RepeatedTest(8)
    void testReadShort() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForShort(unsigned);
        final short value = bitInput.readShort(unsigned, size);
        if (unsigned) {
            assertEquals(0, value >> size);
        } else {
            if (value >= 0) {
                assertEquals(0, value >> (size - 1));
            } else {
                assertEquals(-1, value >> (size - 1));
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @RepeatedTest(8)
    void testReadInt() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForInt(unsigned);
        final int value = bitInput.readInt(unsigned, size);
        if (unsigned) {
            assertEquals(0, value >> size);
        } else {
            if (value >= 0) {
                assertEquals(0, value >> (size - 1));
            } else {
                assertEquals(-1, value >> (size - 1));
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @RepeatedTest(8)
    void testReadSignedLong() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForLong(unsigned);
        final long value = bitInput.readLong(unsigned, size);
        if (unsigned) {
            assertEquals(0L, value >> size);
        } else {
            if (value >= 0L) {
                assertEquals(0L, value >> (size - 1));
            } else {
                assertEquals(-1L, value >> (size - 1));
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @RepeatedTest(8)
    void testReadChar() throws IOException {
        final int size = randomSizeForChar();
        final char value = bitInput.readChar(size);
        assertEquals(0, value >> size);
    }

    // ----------------------------------------------------------------------------------------------------------- align
    @Test
    void testAlign() throws IOException {
        final long discarded = bitInput.align(1);
        assertTrue(discarded >= 0L);
    }

    // ----------------------------------------------------------------------------------------------------------- count
    @Test
    void testCount() {
        final long count = bitInput.getCount();
        assertTrue(count >= 0L);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Spy
    private AbstractBitInput bitInput;
}
