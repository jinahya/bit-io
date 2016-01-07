/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */

package com.github.jinahya.bit.io;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ByteBufferInputTest {


    @Test
    public static void newInstance() throws IOException {

        assertThrows(
            NullPointerException.class,
            () -> {
                BufferByteInput.newInstance(null, 0, current().nextBoolean());
            });

        final ReadableByteChannel channel = mock(ReadableByteChannel.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> BufferByteInput.newInstance(
                channel, 0, current().nextBoolean())
        );

        when(channel.read(any(ByteBuffer.class))).then(invocation -> {
            final ByteBuffer buffer
                = invocation.getArgumentAt(0, ByteBuffer.class);
            while (buffer.hasRemaining()) {
                buffer.put((byte) current().nextInt(256));
            }
            return null;
        });

        final ByteInput byteInput = BufferByteInput.newInstance(
            channel, current().nextInt(1, 256), current().nextBoolean());
        final BitInput bitInput = BitInputFactory.newInstance(byteInput);
        BitInputTest.test(bitInput);
    }

}

