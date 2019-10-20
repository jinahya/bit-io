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

import static com.github.jinahya.bit.io.BitIoTests.acceptRandomSizeByte;
import static com.github.jinahya.bit.io.BitIoTests.acceptRandomSizeInt;
import static com.github.jinahya.bit.io.BitIoTests.acceptRandomSizeLong;
import static com.github.jinahya.bit.io.BitIoTests.acceptRandomSizeShort;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * An abstract class for testing subclasses of {@link BitInput}.
 *
 * @param <T> bit input type parameter.
 */
@ExtendWith({WeldJunit5Extension.class})
@Slf4j
public abstract class BitInputTest<T extends BitInput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given bit input class.
     *
     * @param bitInputClass the bit input class.
     */
    public BitInputTest(final Class<T> bitInputClass) {
        super();
        this.bitInputClass = requireNonNull(bitInputClass, "bitInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void select() {
        bitInput = bitInputInstance.select(bitInputClass).get();
    }

    @AfterEach
    void align() throws IOException {
        final long bits = bitInput.align(current().nextInt(1, 16));
        assertTrue(bits >= 0L);
    }

    // --------------------------------------------------------------------------------------------------------- boolean
    @RepeatedTest(8)
    public void testReadBoolean() throws IOException {
        final boolean value = bitInput.readBoolean();
    }

    // ------------------------------------------------------------------------------------------------------------ byte

    /**
     * Asserts {@link BitInput#readByte(boolean, int)} throws an {@link IllegalArgumentException} when {@code size} is
     * less than {@code 1}.
     */
    @Test
    public void assertReadByteThrowsIllegalArgumentExceptionWhenSizeIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> bitInput.readByte(current().nextBoolean(), 0));
        assertThrows(IllegalArgumentException.class,
                     () -> bitInput.readByte(current().nextBoolean(), current().nextInt() | Integer.MIN_VALUE));
    }

    @RepeatedTest(8)
    public void testReadByte() {
        acceptRandomSizeByte((unsigned, size) -> {
            final byte value;
            try {
                value = bitInput.readByte(unsigned, size);
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
            BitIoTests.assertValidValueInt(unsigned, size, value);
        });
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @RepeatedTest(8)
    public void testReadShort() {
        acceptRandomSizeShort((unsigned, size) -> {
            final short value;
            try {
                value = bitInput.readShort(unsigned, size);
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
            BitIoTests.assertValidValueInt(unsigned, size, value);
        });
    }

    // ------------------------------------------------------------------------------------------------------------- int

    /**
     * Tests {@link BitInput#readInt(boolean, int)}.
     */
    @RepeatedTest(8)
    public void testReadInt() {
        acceptRandomSizeInt((unsigned, size) -> {
            final int value;
            try {
                value = bitInput.readInt(unsigned, size);
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
            BitIoTests.assertValidValueInt(unsigned, size, value);
        });
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @RepeatedTest(8)
    public void testReadLong() {
        acceptRandomSizeLong((unsigned, size) -> {
            final long value;
            try {
                value = bitInput.readLong(unsigned, size);
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
            BitIoTests.assertValidValueLong(unsigned, size, value);
        });
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @Test
    public void testReadCharAssertThrowsIllegalArgumentExceptionWhenSizeIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> bitInput.readChar(0));
        assertThrows(IllegalArgumentException.class, () -> bitInput.readChar(current().nextInt() | Integer.MIN_VALUE));
    }

    @RepeatedTest(8)
    public void testReadChar() throws IOException {
        final int size = current().nextInt(1, Character.SIZE + 1);
        final char value = bitInput.readChar(size);
    }

    // ----------------------------------------------------------------------------------------------------------- align

    /**
     * Asserts {@link BitInput#align(int)} throws {@link IllegalArgumentException} when {@code bytes} is less than
     * {@code 1}.
     */
    @Test
    public void assertIllegalArgumentExceptionThrownWhenBytesIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> bitInput.align(0));
        assertThrows(IllegalArgumentException.class, () -> bitInput.align(current().nextInt() | Integer.MIN_VALUE));
    }

    /**
     * Tests {@link BitInput#align(int)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testAlign() throws IOException {
        final long bits = bitInput.align(current().nextInt(1, 128));
        assert bits >= 0L;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The bit input class to test.
     */
    final Class<T> bitInputClass;

    @Inject
    private Instance<BitInput> bitInputInstance;

    /**
     * An injected instance of {@link #bitInputClass}.
     */
    protected T bitInput;
}
