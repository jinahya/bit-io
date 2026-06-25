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
