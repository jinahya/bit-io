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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class for testing {@link ArrayByteInput}.
 *
 * @see ArrayByteOutputTest
 */
class ArrayByteInputTest extends AbstractByteInputTest<ArrayByteInput, byte[]> {

    // -------------------------------------------------------------------------------------------------------------- of

    /**
     * Asserts {@link ArrayByteInput#of(int, InputStream)} method throws {@code IllegalArgumentException} when {@code
     * length} argument is less than or equal to {@code zero}.
     */
    @Test
    public void assertOfThrowsIllegalArgumentExceptionWhenLengthIsLessThanOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> ArrayByteInput.of(0, mock(InputStream.class)));
        assertThrows(IllegalArgumentException.class,
                     () -> ArrayByteInput.of(current().nextInt() | Integer.MIN_VALUE, mock(InputStream.class)));
    }

    /**
     * Asserts {@link ArrayByteInput#of(int, InputStream)} method throws {@code NullPointerException} when {@code
     * stream} argument is {@code null}.
     */
    @Test
    public void assertOfThrowsIllegalArgumentExceptionWhenStreamIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayByteInput.of(current().nextInt() >>> 1, null));
    }

    /**
     * Tests {@link ArrayByteInput#of(int, InputStream)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testOf() throws IOException {
        final int length = current().nextInt(1, 256);
        final InputStream stream = mock(InputStream.class);
        when(stream.read(any(byte[].class))).thenAnswer(invocation -> {
            final byte[] array = invocation.getArgument(0);
            return current().nextInt(1, array.length);
        });
        final ArrayByteInput byteInput = ArrayByteInput.of(length, stream);
        for (int i = 0; i < 1024; i++) {
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
