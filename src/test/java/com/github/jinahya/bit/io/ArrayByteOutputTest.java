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
