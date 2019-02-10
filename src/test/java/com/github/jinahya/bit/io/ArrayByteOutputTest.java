package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * A class for testing {@link ArrayByteOutput}.
 *
 * @see ArrayByteInputTest
 */
public class ArrayByteOutputTest extends AbstractByteOutputTest<ArrayByteOutput, byte[]> {

    // -------------------------------------------------------------------------------------------------------------- of

    /**
     * Asserts {@link ArrayByteOutput#of(int, OutputStream)} throws {@code IllegalArgumentException} when {@code length}
     * argument is less than or equal to {@code zero}.
     */
    @Test
    public void testOfAssertThrowsIllegalArgumentExceptionWhenLengthIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> ArrayByteOutput.of(0, mock(OutputStream.class)));
        assertThrows(IllegalArgumentException.class,
                () -> ArrayByteOutput.of(current().nextInt() | Integer.MIN_VALUE, mock(OutputStream.class)));
    }

    /**
     * Asserts {@link ArrayByteOutput#of(int, OutputStream)} throws {@code NullPointerException} when {@code stream}
     * argument is {@code null}.
     */
    @Test
    public void testOfAssertThrowsNullPointerExceptionWhenStreamIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayByteOutput.of(current().nextInt() >>> 1, null));
    }

    @Test
    public void testOf() throws IOException {
        final ArrayByteOutput byteOutput = ArrayByteOutput.of(1, new BlackOutputStream());
        byteOutput.write(current().nextInt() & 0xFF);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public ArrayByteOutputTest() {
        super(ArrayByteOutput.class, byte[].class);
    }
}
