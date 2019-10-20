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

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link AbstractBitOutput#writeSignedByte(int, byte)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    public void testWriteSignedByte() throws IOException {
        final int size = current().nextInt(1, Byte.SIZE + 1);
        final byte value = (byte) (current().nextInt() >> (Integer.SIZE - size));
        bitOutput.writeSignedByte(size, value);
    }

    /**
     * Tests {@link AbstractBitOutput#writeUnsignedByte(int, byte)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    public void testWriteUnsignedByte() throws IOException {
        final int size = current().nextInt(1, Byte.SIZE);
        final byte value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
        assertTrue(value >= 0);
        bitOutput.writeUnsignedByte(size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @RepeatedTest(8)
    public void testWriteSignedShort() throws IOException {
        final int size = current().nextInt(1, Short.SIZE + 1);
        final short value = (short) (current().nextInt() >> (Integer.SIZE - size));
        bitOutput.writeSignedShort(size, value);
    }

    @RepeatedTest(8)
    public void testWriteUnsignedShort() throws IOException {
        final int size = current().nextInt(1, Short.SIZE);
        final short value = (short) (current().nextInt() >>> (Integer.SIZE - size));
        assertTrue(value >= 0);
        bitOutput.writeUnsignedShort(size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @RepeatedTest(8)
    public void testWriteSignedInt() throws IOException {
        final int size = current().nextInt(1, Integer.SIZE + 1);
        final int value = current().nextInt() >> (Integer.SIZE - size);
        bitOutput.writeSignedInt(size, value);
    }

    @RepeatedTest(8)
    public void testWriteUnsignedInt() throws IOException {
        final int size = current().nextInt(1, Integer.SIZE);
        final int value = current().nextInt() >>> (Integer.SIZE - size);
        assertTrue(value >= 0);
        bitOutput.writeUnsignedInt(size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Test {@link AbstractBitOutput#writeSignedLong(int, long)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    public void testWriteSignedLong() throws IOException {
        final int size = current().nextInt(1, Long.SIZE + 1);
        final long value = current().nextLong() >> (Long.SIZE - size);
        bitOutput.writeSignedLong(size, value);
    }

    /**
     * Tests {@link AbstractBitOutput#writeUnsignedLong(int, long)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    public void testWriteUnsignedLong() throws IOException {
        final int size = current().nextInt(1, Long.SIZE);
        final long value = current().nextLong() >>> (Long.SIZE - size);
        assertTrue(value >= 0L);
        bitOutput.writeUnsignedLong(size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link AbstractBitOutput#writeChar(int, char)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    public void testWriteChar() throws IOException {
        final int size = current().nextInt(1, Character.SIZE + 1);
        final char value = (char) (current().nextInt() >>> (Integer.SIZE - size));
        bitOutput.writeChar(size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Spy
    private AbstractBitOutput bitOutput;
}
