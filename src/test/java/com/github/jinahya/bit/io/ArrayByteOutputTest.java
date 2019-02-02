package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.IntSupplier;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 * A class for testing {@link ArrayByteOutput}.
 */
public class ArrayByteOutputTest extends AbstractByteOutputTest<ArrayByteOutput, byte[]> {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testOfAssertThrowsIllegalArgumentExceptionWhenStreamIsNull() {
        final OutputStream stream = null;
        final int length = ((IntSupplier) () -> {
            int value = current().nextInt() & Integer.MAX_VALUE;
            if (value == 0) {
                value = 1;
            }
            return value;
        }).getAsInt();
        assertThrows(NullPointerException.class, () -> ArrayByteOutput.of(stream, length));
    }

    @Test
    public void testOfAssertThrowsIllegalArgumentExceptionWhenLengthIsNotPositive() {
        final OutputStream stream = mock(OutputStream.class);
        final int length = current().nextBoolean() ? 0 : (current().nextInt() | Integer.MIN_VALUE);
        assertThrows(IllegalArgumentException.class, () -> ArrayByteOutput.of(stream, length));
    }

    @Test
    public void testOf() throws IOException {
        final OutputStream stream = mock(OutputStream.class);
        doNothing().when(stream).write(anyInt());
        final int length = current().nextInt(1, 128);
        final ByteOutput byteOutput = ArrayByteOutput.of(stream, length);
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
