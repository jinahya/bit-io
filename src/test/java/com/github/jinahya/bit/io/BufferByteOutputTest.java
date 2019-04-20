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
import java.nio.channels.WritableByteChannel;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * An abstract class for testing subclasses of {@link BufferByteOutput}.
 *
 * @param <T> byte output type parameter
 * @param <U> byte buffer type parameter
 * @see BufferByteInputTest
 */
public abstract class BufferByteOutputTest<T extends BufferByteOutput<U>, U extends ByteBuffer>
        extends AbstractByteOutputTest<T, U> {

    // -------------------------------------------------------------------------------------------------------------- of

    /**
     * Asserts {@link BufferByteOutput#of(int, WritableByteChannel)} method throws an {@code IllegalArgumentException}
     * when {@code capacity} is les than or equal to {@code zero}.
     */
    @Test
    public void assertOfThrowsIllegalArgumentExceptionWhenCapacityIsLessThanOrEqualToZero() {
        final WritableByteChannel channel = mock(WritableByteChannel.class);
        assertThrows(IllegalArgumentException.class, () -> BufferByteOutput.of(0, channel));
        assertThrows(IllegalArgumentException.class, () -> BufferByteOutput.of(
                current().nextInt() | Integer.MIN_VALUE, channel));
    }

    /**
     * Asserts {@link BufferByteOutput#of(int, WritableByteChannel)} throws a {@code NullPointerException} when {@code
     * channel} is {@code null}.
     */
    @Test
    public void assertOfThrowsNullPointerExceptionWhenChannelIsNull() {
        final int capacity = current().nextInt() >>> 1;
        assertThrows(NullPointerException.class, () -> BufferByteOutput.of(capacity, null));
    }

    /**
     * Tests {@link BufferByteOutput#of(int, WritableByteChannel)}.
     */
    @Test
    public void testOf() throws IOException {
        final int capacity = current().nextInt(1, 256);
        final WritableByteChannel channel = mock(WritableByteChannel.class);
        when(channel.write(any(ByteBuffer.class))).thenAnswer(invocation -> {
            final ByteBuffer buffer = invocation.getArgument(0);
            final int delta = current().nextInt(0, buffer.remaining());
            buffer.position(buffer.position() + delta);
            return delta;
        });
        final ByteOutput output = BufferByteOutput.of(capacity, channel);
        for (int i = 0; i < 1024; i++) {
            output.write(current().nextInt(0, 256));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given arguments.
     *
     * @param byteOutputClass the class of byte output to test.
     * @param byteTargetClass the class of byte target to test.
     */
    public BufferByteOutputTest(final Class<T> byteOutputClass, final Class<U> byteTargetClass) {
        super(byteOutputClass, byteTargetClass);
    }
}
