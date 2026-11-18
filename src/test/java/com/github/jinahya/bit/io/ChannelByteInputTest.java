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

import static java.nio.ByteBuffer.allocate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link ChannelByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutputTest
 */
public class ChannelByteInputTest {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link ChannelByteInput#read()} reading from a channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void readFromChannel() throws IOException {
        final byte[] bytes = {0x00, 0x7F, (byte) 0xFF};
        final ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(bytes));
        final ChannelByteInput input = new ChannelByteInput(channel, allocate(2));

        assertEquals(0x00, input.read());
        assertEquals(0x7F, input.read());
        assertEquals(0xFF, input.read());
        assertThrows(EOFException.class, input::read);
    }

    /**
     * Tests that, once the channel is exhausted, every subsequent read keeps throwing {@link EOFException} rather than
     * returning stale buffered bytes.
     *
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://github.com/jinahya/bit-io/issues/19">#19</a>
     */
    @Test
    public void readStaysAtEofAfterSingleByte() throws IOException {
        final ByteInput input = BufferByteInput.from(Channels.newChannel(new ByteArrayInputStream(new byte[]{0x5A})));

        assertEquals(0x5A, input.read());
        for (int i = 0; i < 4; i++) {
            assertThrows(EOFException.class, input::read);
        }
    }

    /**
     * Tests that reads from an empty channel keep throwing {@link EOFException}.
     *
     * @see <a href="https://github.com/jinahya/bit-io/issues/19">#19</a>
     */
    @Test
    public void readStaysAtEofForEmptyChannel() {
        final ByteInput input = BufferByteInput.from(Channels.newChannel(new ByteArrayInputStream(new byte[0])));

        for (int i = 0; i < 4; i++) {
            assertThrows(EOFException.class, input::read);
        }
    }

    /**
     * Tests that a read which hits EOF does not lose bytes once the channel yields more.
     *
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://github.com/jinahya/bit-io/issues/19">#19</a>
     */
    @Test
    public void readResumesAfterEofWhenChannelYieldsMore() throws IOException {
        final int[] reads = {0};
        final ReadableByteChannel channel = new ReadableByteChannel() {
            @Override
            public int read(final ByteBuffer dst) {
                switch (reads[0]++) {
                    case 0:
                        dst.put((byte) 0x01);
                        return 1;
                    case 1:
                        return -1;
                    case 2:
                        dst.put((byte) 0x02);
                        return 1;
                    default:
                        return -1;
                }
            }

            @Override
            public boolean isOpen() {
                return true;
            }

            @Override
            public void close() {
            }
        };
        final ChannelByteInput input = new ChannelByteInput(channel, allocate(4));

        assertEquals(0x01, input.read());
        assertThrows(EOFException.class, input::read);
        assertEquals(0x02, input.read());
        assertThrows(EOFException.class, input::read);
    }

    @Test
    public void constructorRejectsNullChannelAndBuffer() {
        assertThrows(NullPointerException.class, () -> new ChannelByteInput(null, allocate(1)));
        assertThrows(NullPointerException.class, () -> new ChannelByteInput(new WhiteByteChannel(), null));
    }

    @Test
    public void constructorRejectsZeroCapacityBuffer() {
        assertThrows(IllegalArgumentException.class,
                     () -> new ChannelByteInput(new WhiteByteChannel(), allocate(0)));
    }
}
