/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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


package com.github.jinahya.bit.io.octet;


import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;


/**
 * A {@link ByteInput} implementation uses a {@link ByteBuffer} as
 * {@link #source}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BufferInput extends AbstractByteInput<ByteBuffer> {


    public static BufferInput newInstance(final ReadableByteChannel channel,
                                          final int capacity,
                                          final boolean direct) {

        if (channel == null) {
            throw new NullPointerException("null channel");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "capacity(" + capacity + ") <= 0");
        }

        return new BufferInput(null) {

            @Override
            public int read() throws IOException {

                if (source == null) {
                    source = direct
                             ? ByteBuffer.allocateDirect(capacity)
                             : ByteBuffer.allocate(capacity);
                    source.position(source.limit());
                }

                if (!source.hasRemaining()) {
                    source.clear(); // position -> zero; limit -> capacity;
                    do {
                        final int read = channel.read(source);
                        if (read == -1) {
                            throw new EOFException();
                        }
                    } while (source.position() == 0);
                    source.flip(); // limit -> position; position -> zero;
                }

                return super.read();
            }

        };
    }


    /**
     * Creates a new instance built on top of the specified byte buffer.
     *
     * @param source the byte buffer or {@code null} if it's supposed to be
     * lazily initialized and set.
     */
    public BufferInput(final ByteBuffer source) {

        super(source);
    }


    /**
     * {@inheritDoc} The {@code read()} method of {@code BufferInput} class
     * returns the value of following code.
     * <blockquote><pre>source.get() &amp; 0xFF</pre></blockquote> Override this
     * method if {@link #source source} is supposed to be lazily initialized or
     * adjusted.
     *
     * @return {@inheritDoc }
     *
     * @throws IOException {@inheritDoc }
     *
     * @see #source
     * @see ByteBuffer#get()
     */
    @Override
    public int read() throws IOException {

        return source.get() & 0xFF;
    }

}

