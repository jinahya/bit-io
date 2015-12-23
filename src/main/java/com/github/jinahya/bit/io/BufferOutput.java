/*
 * Copyright 2013 Jin Kwon.
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
import java.nio.channels.WritableByteChannel;


/**
 * A {@link ByteOutput} implementation for {@link ByteBuffer}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BufferOutput extends AbstractByteOutput<ByteBuffer> {


    public static BufferOutput newInstance(final WritableByteChannel channel,
                                           final int capacity,
                                           final boolean direct) {

        if (channel == null) {
            throw new NullPointerException("null channel");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "capacity(" + capacity + ") <= 0");
        }

        return new BufferOutput(null) {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (target == null) {
                    target = direct
                             ? ByteBuffer.allocateDirect(capacity)
                             : ByteBuffer.allocate(capacity);
                    assert target.position() == 0;
                    assert target.limit() == target.capacity();
                }

                super.writeUnsignedByte(value);

                if (!target.hasRemaining()) {
                    target.rewind(); // position -> zero;
                    while (target.hasRemaining()) {
                        channel.write(target);
                    }
                    target.rewind();
                }
            }

        };
    }


    /**
     * Creates a new instance with given {@code ByteBufer}.
     *
     * @param buffer the {@code ByteBuffer} to wrap or {@code null} if it's
     * supposed to be lazily initialized and set.
     */
    public BufferOutput(final ByteBuffer buffer) {

        super(buffer);
    }


    /**
     * {@inheritDoc} The {@code writeUnsignedByte(int)} method of
     * {@code BufferOutput} class calls {@link ByteBuffer#put(byte)} on
     * {@link #target} with given {@code value}. Override this method if
     * {@link #target} is supposed to be lazily initialized or adjusted.
     *
     * @param value {@inheritDoc }
     *
     * @throws IOException {@inheritDoc }
     *
     * @see #target
     * @see ByteBuffer#put(byte)
     */
    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        target.put((byte) value);
    }

}

