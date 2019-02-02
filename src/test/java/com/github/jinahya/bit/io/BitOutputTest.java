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

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An abstract class for testing subclasses of {@link BitOutput}.
 *
 * @param <T> bit output type parameter.
 */
@ExtendWith({WeldJunit5Extension.class})
public abstract class BitOutputTest<T extends BitOutput> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given bit output class.
     *
     * @param bitOutputClass the bit output class to test.
     * @see #bitOutputClass
     */
    public BitOutputTest(final Class<T> bitOutputClass) {
        super();
        this.bitOutputClass = requireNonNull(bitOutputClass, "bitOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void selectBitInput() {
        bitOutput = bitOutputInstance.select(bitOutputClass).get();
    }

    @AfterEach
    void dotAlign() throws IOException {
        final long bits = bitOutput.align(current().nextInt(1, 16));
    }

    // --------------------------------------------------------------------------------------------------------- boolean

    /**
     * Tests {@link BitOutput#writeBoolean(boolean)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(128)
    public void testWriteBoolean() throws IOException {
        bitOutput.writeBoolean(current().nextBoolean());
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @RepeatedTest(128)
    public void testWriteByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        final byte value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
        bitOutput.writeByte(unsigned, size, value);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @RepeatedTest(128)
    public void testWriteShort() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        final short value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
        bitOutput.writeShort(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @RepeatedTest(128)
    public void testWriteInt() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
        final int value = current().nextInt() >>> (Integer.SIZE - size);
        bitOutput.writeInt(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @RepeatedTest(128)
    public void testWriteLong() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
        final long value = current().nextLong() >>> (Long.SIZE - size);
        bitOutput.writeLong(unsigned, size, value);
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @RepeatedTest(128)
    public void testWriteChar() throws IOException {
        final int size = current().nextInt(1, Character.SIZE + 1);
        bitOutput.writeChar(size, (char) current().nextInt());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testAlignAssertThrowsIllegalArgumentExceptionWhenBytesIsLessThanOrEqualsToZero() {
        assertThrows(IllegalArgumentException.class, () -> bitOutput.align(0));
        assertThrows(IllegalArgumentException.class, () -> bitOutput.align(current().nextInt() | -1));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The class of bit output to test.
     */
    protected final Class<T> bitOutputClass;

    @Inject
    private Instance<BitOutput> bitOutputInstance;

    /**
     * An injected (and selected) instance of bit output.
     */
    protected T bitOutput;
}
