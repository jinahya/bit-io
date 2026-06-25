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

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link BufferByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BufferByteOutputTest
 */
class BufferByteInputTest
        extends AbstractByteInputTest<BufferByteInput, ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    BufferByteInputTest() {
        super(BufferByteInput.class, ByteBuffer.class);
    }

    // ------------------------------------------------------------------------------------------------------------ from

    @Test
    void from_NullPointerException_NullChannel() {
        assertThrows(NullPointerException.class, () -> BufferByteInput.from(null));
    }

    @Test
    void from_ReadsAllBytes_FromChannel() throws IOException {
        final byte[] bytes = new byte[ThreadLocalRandom.current().nextInt(1, 1024)];
        ThreadLocalRandom.current().nextBytes(bytes);
        final ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(bytes));
        final ByteInput input = BufferByteInput.from(channel);
        for (final byte b : bytes) {
            assertEquals(b & 0xFF, input.read());
        }
    }

    @Test
    void from_EOFException_WhenChannelExhausted() throws IOException {
        final ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(new byte[0]));
        final ByteInput input = BufferByteInput.from(channel);
        assertThrows(EOFException.class, input::read);
    }
}
