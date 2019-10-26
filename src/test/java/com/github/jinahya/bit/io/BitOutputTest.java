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
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTests.randomSizeForByte;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForByte;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * An abstract class for testing subclasses of {@link BitOutput}.
 *
 * @param <T> bit output type parameter.
 * @see BitInputTest
 * @see AbstractBitOutputTest
 */
@ExtendWith({WeldJunit5Extension.class})
@Slf4j
abstract class BitOutputTest<T extends BitOutput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given bit output class.
     *
     * @param bitOutputClass the bit output class to test.
     * @see #bitOutputClass
     */
    BitOutputTest(final Class<T> bitOutputClass) {
        super();
        this.bitOutputClass = requireNonNull(bitOutputClass, "bitOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void select() {
        bitOutput = bitOutputInstance.select(bitOutputClass).get();
    }

    @AfterEach
    void align() throws IOException {
        final long bits = bitOutput.align(current().nextInt(1, 16));
        assertTrue(bits >= 0L);
    }

    // --------------------------------------------------------------------------------------------------------- boolean

    /**
     * Tests {@link BitOutput#writeBoolean(boolean)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    void testWriteBoolean() throws IOException {
        bitOutput.writeBoolean(current().nextBoolean());
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @RepeatedTest(8)
    void testWriteByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForByte(unsigned);
        final byte value = randomValueForByte(unsigned, size);
        bitOutput.writeByte(unsigned, size, value);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @RepeatedTest(8)
    void testWriteShort() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        final short value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
        bitOutput.writeShort(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @RepeatedTest(8)
    void testWriteInt() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
        final int value = current().nextInt() >>> (Integer.SIZE - size);
        bitOutput.writeInt(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @RepeatedTest(8)
    void testWriteLong() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
        final long value = current().nextLong() >>> (Long.SIZE - size);
        bitOutput.writeLong(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @RepeatedTest(8)
    void testWriteChar() throws IOException {
        final int size = current().nextInt(1, Character.SIZE + 1);
        bitOutput.writeChar(size, (char) current().nextInt());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testAlignAssertThrowsIllegalArgumentExceptionWhenBytesIsLessThanOrEqualsToZero() {
        assertThrows(IllegalArgumentException.class, () -> bitOutput.align(0));
        assertThrows(IllegalArgumentException.class, () -> bitOutput.align(current().nextInt() | -1));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The class of bit output to test.
     */
    final Class<T> bitOutputClass;

    @Inject
    private Instance<BitOutput> bitOutputInstance;

    /**
     * An injected instance of bit output.
     */
    T bitOutput;
}
