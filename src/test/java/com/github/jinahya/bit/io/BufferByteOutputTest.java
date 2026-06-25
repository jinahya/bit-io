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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing of {@link BufferByteOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BufferByteInputTest
 */
class BufferByteOutputTest
        extends AbstractByteOutputTest<BufferByteOutput, ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    BufferByteOutputTest() {
        super(BufferByteOutput.class, ByteBuffer.class);
    }

    // ------------------------------------------------------------------------------------------------------------ from

    @Test
    void from_NullPointerException_NullChannel() {
        assertThrows(NullPointerException.class, () -> BufferByteOutput.from(null));
    }

    /**
     * Verifies that {@link BufferByteOutput#from(WritableByteChannel)} is write-through: every written byte reaches the
     * channel immediately, with no flushing required.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void from_WritesThrough_ToChannel() throws IOException {
        final byte[] bytes = new byte[ThreadLocalRandom.current().nextInt(1, 1024)];
        ThreadLocalRandom.current().nextBytes(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableByteChannel channel = Channels.newChannel(baos);
        final ByteOutput output = BufferByteOutput.from(channel);
        for (final byte b : bytes) {
            output.write(b & 0xFF);
        }
        // write-through: all bytes are already in the stream, without any flush/close
        assertArrayEquals(bytes, baos.toByteArray());
    }
}
