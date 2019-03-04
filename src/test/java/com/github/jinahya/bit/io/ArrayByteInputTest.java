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
