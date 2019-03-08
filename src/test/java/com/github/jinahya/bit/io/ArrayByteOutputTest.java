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
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    public void assertOfThrowsIllegalArgumentExceptionWhenLengthIsLessThanOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> ArrayByteOutput.of(0, mock(OutputStream.class)));
        assertThrows(IllegalArgumentException.class,
                     () -> ArrayByteOutput.of(current().nextInt() | Integer.MIN_VALUE, mock(OutputStream.class)));
    }

    /**
     * Asserts {@link ArrayByteOutput#of(int, OutputStream)} throws {@code NullPointerException} when {@code stream}
     * argument is {@code null}.
     */
    @Test
    public void assertOfThrowsNullPointerExceptionWhenStreamIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayByteOutput.of(current().nextInt() >>> 1, null));
    }

    /**
     * Tests {@link ArrayByteOutput#of(int, OutputStream)}. The {@code testOf} method of {@code ArrayByteOutputTest}
     * class invokes {@link ArrayByteOutput#of(int, OutputStream)} with a random for {@code length} parameter and a mock
     * of {@link OutputStream} for {@code stream} parameter and then invokes {@link ByteOutput#write(int)} a few times.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testOf() throws IOException {
        final int length = current().nextInt(1, 256);
        final OutputStream stream = mock(OutputStream.class);
        doNothing().when(stream).write(any(byte[].class));
        final ArrayByteOutput byteOutput = ArrayByteOutput.of(length, stream);
        for (int i = 0; i < 1024; i++) {
            byteOutput.write(current().nextInt() & 0xFF);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public ArrayByteOutputTest() {
        super(ArrayByteOutput.class, byte[].class);
    }
}
