package com.github.jinahya.bit.io;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An abstract class for testing subclasses of {@link BitInput}.
 *
 * @param <T> bit input type parameter.
 */
@ExtendWith({WeldJunit5Extension.class})
public abstract class BitInputTest<T extends BitInput> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given bit input class.
     *
     * @param bitInputClass the bit input class.
     */
    public BitInputTest(final Class<T> bitInputClass) {
        super();
        this.bitInputClass = Objects.requireNonNull(bitInputClass, "bitInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void selectBitInput() {
        bitInput = bitInputInstance.select(bitInputClass).get();
    }

    @AfterEach
    void dotAlign() throws IOException {
        final long bits = bitInput.align(current().nextInt(1, 16));
    }

    // --------------------------------------------------------------------------------------------------------- boolean
    @RepeatedTest(128)
    public void testReadBoolean() throws IOException {
        final boolean value = bitInput.readBoolean();
    }

    // ------------------------------------------------------------------------------------------------------------ byte

    /**
     * Tests {@link BitInput#readByte(boolean, int)} asserting an {@link IllegalArgumentException} is thrown when {@code
     * size} is less than {@code one}.
     */
    @Test
    public void testReadByteAssertThrowsIllegalArgumentExceptionWhenSizeIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> bitInput.readByte(current().nextBoolean(), 0));
        assertThrows(IllegalArgumentException.class,
                () -> bitInput.readByte(current().nextBoolean(), current().nextInt() | Integer.MIN_VALUE));
    }

    @RepeatedTest(128)
    public void testReadByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE - 1) + (unsigned ? 0 : 1);
        final byte value = bitInput.readByte(unsigned, size);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @RepeatedTest(128)
    public void testReadShort() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE - 1) + (unsigned ? 0 : 1);
        final short value = bitInput.readShort(unsigned, size);
    }

    // ------------------------------------------------------------------------------------------------------------- int

    /**
     * Tests {@link BitInput#readInt(boolean, int)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(128)
    public void testReadInt() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Integer.SIZE - 1) + (unsigned ? 0 : 1);
        final int value = bitInput.readInt(unsigned, size);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @RepeatedTest(128)
    public void testReadLong() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Long.SIZE - 1) + (unsigned ? 0 : 1);
        final long value = bitInput.readLong(unsigned, size);
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @Test
    public void testReadCharAssertThrowsIllegalArgumentExceptionWhenSizeIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> bitInput.readChar(0));
        assertThrows(IllegalArgumentException.class, () -> bitInput.readChar(current().nextInt() | Integer.MIN_VALUE));
    }

    @RepeatedTest(128)
    public void testReadChar() throws IOException {
        final int size = current().nextInt(1, Character.SIZE + 1);
        final char value = bitInput.readChar(size);
    }

    // ----------------------------------------------------------------------------------------------------------- align
    @Test
    public void testAlignAssertIllegalArgumentExceptionThrownWhenBytesIsLessThanOne() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> bitInput.align(0));
        assertThrows(IllegalArgumentException.class, () -> bitInput.align(current().nextInt() | Integer.MIN_VALUE));
    }

    @Test
    public void testAlign() throws IOException {
        final long bits = bitInput.align(current().nextInt(1, 128));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The bit input class to test.
     */
    protected final Class<T> bitInputClass;

    @Inject
    private Instance<BitInput> bitInputInstance;

    protected T bitInput;
}
