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


package com.github.jinahya.bio;


import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * A {@link ByteOutput} implementation for {@link ByteBuffer}s.
 *
 * @author Jin Kwon
 */
public class BufferOutput extends AbstractByteOutput<ByteBuffer> {


    /**
     * Creates a new instance with given {@code ByteBufer}.
     *
     * @param target the {@code ByteBuffer} to wrap.
     */
    public BufferOutput(final ByteBuffer target) {

        super(target);
    }


    @Override
    protected ByteBuffer requireValidTarget() {

        final ByteBuffer target = super.requireValidTarget();

        if (!target.hasRemaining()) {
            throw new IllegalStateException(
                "The underlying byte buffer has no remaining");
        }

        return target;
    }


    /**
     * {@inheritDoc} The {@code writeUnsignedByte(int)} method of
     * {@code BufferOutput} class returns
     * <pre>{@link #requireValidTarget()}.put((byte) value)</pre>.
     *
     * @param value {@inheritDoc }
     *
     * @throws IOException {@inheritDoc }
     *
     * @see #requireValidTarget()
     * @see ByteBuffer#put(byte)
     */
    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        requireValidTarget().put((byte) value);
    }


}

