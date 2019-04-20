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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * An abstract class for testing subclasses of {@link BufferByteInput}.
 *
 * @param <T> byte input type parameter
 * @param <U> byte buffer type parameter
 * @see BufferByteOutputTest
 */
abstract class BufferByteInputTest<T extends BufferByteInput<U>, U extends ByteBuffer>
        extends AbstractByteInputTest<T, U> {

    // -------------------------------------------------------------------------------------------------------------- of

    /**
     * Asserts {@link BufferByteInput#of(int, ReadableByteChannel)} throws an {@code IllegalArgumentException} when
     * {@code capacity} argument is less than or equal to {@code zero}.
     */
    @Test
    public void assertOfThrowsIllegalArgumentExceptionWhenCapacityIsLessThanOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> BufferByteInput.of(0, mock(ReadableByteChannel.class)));
        assertThrows(IllegalArgumentException.class, () -> BufferByteInput.of(
                current().nextInt() | Integer.MIN_VALUE, mock(ReadableByteChannel.class)));
    }

    /**
     * Asserts {@link BufferByteInput#of(int, ReadableByteChannel)} throws a {@code NullPointerException} then {@code
     * channel} argument is {@code null}.
     */
    @Test
    public void assertOfThrowsNullPointerExceptionWhenChannelIsNull() {
        assertThrows(NullPointerException.class, () -> BufferByteInput.of(current().nextInt() >>> 1, null));
    }

    /**
     * Tests {@link BufferByteInput#of(int, ReadableByteChannel)}, creates with a mock and invokes {@link
     * ByteInput#read()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testOf() throws IOException {
        final ReadableByteChannel channel = mock(ReadableByteChannel.class);
        when(channel.read(any(ByteBuffer.class))).thenAnswer(invocationOnMock -> {
            final ByteBuffer buffer = invocationOnMock.getArgument(0);
            buffer.put((byte) current().nextInt(0, 256));
            return 1;
        });
        final BufferByteInput<ByteBuffer> input = BufferByteInput.of(current().nextInt(0, 255), channel);
        for (int i = 0; i < 1024; i++) {
            final int b = input.read();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteInputClass  a byte input class.
     * @param byteSourceClass a source class.
     */
    BufferByteInputTest(final Class<T> byteInputClass, final Class<U> byteSourceClass) {
        super(byteInputClass, byteSourceClass);
    }
}
