package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * A class for testing {@link ArrayByteInput}.
 *
 * @see ArrayByteOutputTest
 */
class ArrayByteInputTest extends AbstractByteInputTest<ArrayByteInput, byte[]> {

    // -------------------------------------------------------------------------------------------------------------- of

    /**
     * Asserts {@link ArrayByteInput#of(int, InputStream)} throws {@code IllegalArgumentException} when {@code length}
     * argument is less than or equal to {@code zero}.
     */
    @Test
    public void testOfAssertThrowsIllegalArgumentExceptionWhenLengthIsLessThanOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> ArrayByteInput.of(0, mock(InputStream.class)));
        assertThrows(IllegalArgumentException.class,
                     () -> ArrayByteInput.of(current().nextInt() | Integer.MIN_VALUE, mock(InputStream.class)));
    }

    /**
     * Asserts {@link ArrayByteInput#of(int, InputStream)} throws {@code NullPointerException} when {@code stream}
     * argument is {@code null}.
     */
    @Test
    public void testOfAssertThrowsIllegalArgumentExceptionWhenStreamIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayByteInput.of(current().nextInt() >>> 1, null));
    }

    @Test
    public void testOf() throws IOException {
        final ArrayByteInput byteInput = ArrayByteInput.of(1, new WhiteInputStream());
        for (int i = 0; i < 16; i++) {
            final int b = byteInput.read();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    ArrayByteInputTest() {
        super(ArrayByteInput.class, byte[].class);
    }
}
