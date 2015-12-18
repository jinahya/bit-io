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


package com.github.jinahya.bit.io;


import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * A {@link ByteInput} implementation using {@link ByteBuffer}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BufferInput extends AbstractByteInput<ByteBuffer> {


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
     * {@inheritDoc} The {@code readUnsignedByte()} method of
     * {@code BufferInput} class returns
     * <pre>source.get() &amp; 0xFF</pre>. Override this method if
     * {@link #source} is supposed to be lazily initialized and set.
     *
     * @return {@inheritDoc }
     *
     * @throws IOException {@inheritDoc }
     *
     * @see #source
     * @see ByteBuffer#get()
     */
    @Override
    public int readUnsignedByte() throws IOException {

        return source.get() & 0xFF;
    }

}

