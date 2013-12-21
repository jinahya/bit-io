/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;


/**
 * A {@link ByteInput} implementation for {@link ByteBuffer}s.
 */
public class BufferInput extends ByteInput<ByteBuffer> {


    /**
     * Creates a new instance on top of specified byte buffer.
     *
     * @param source the buffer to wrap.
     */
    public BufferInput(final ByteBuffer source) {
        super(source);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code readUnsignedByte()} method of {@code ByteBuffer} class
     * calls {@link ByteBuffer#get()} on {@link #source} and returns the
     * result.
     *
     * @return {@inheritDoc }
     *
     * @throws IOException {@inheritDoc}
     *
     * @see ByteBuffer#get()
     */
    @Override
    public int readUnsignedByte() throws IOException {
        try {
            return source.get() & 0xFF;
        } catch (final BufferUnderflowException bue) {
            return -1;
        }
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code close()} method of {@code BufferInput} class does nothing.
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
    }
    
}

