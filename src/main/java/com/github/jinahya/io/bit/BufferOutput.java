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


package com.github.jinahya.io.bit;


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


    /**
     * {@inheritDoc} The {@code writeUnsignedByte(int)} method of
     * {@code BufferOutput} class calls {@link ByteBuffer#put(byte)} on the
     * underlying byte buffer with given {@code value}. Override this method if
     * {@link #target} is intended to be lazily initialized and set.
     *
     * @param value {@inheritDoc }
     *
     * @throws IllegalStateException if {@link #target} is currently
     * {@code null}.
     * @throws IOException {@inheritDoc }
     *
     * @see ByteBuffer#put(byte)
     * @see #target
     * @see #setTarget(java.lang.Object)
     */
    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        requireNonNullTarget().put((byte) value);
    }


}

