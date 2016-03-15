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
 * A {@link ByteInput} implementation uses a {@link ByteBuffer} as
 * {@link #source}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput extends AbstractByteInput<ByteBuffer> {

    /**
     * Creates a new instance built on top of the specified byte buffer.
     *
     * @param source the byte buffer or {@code null} if it's supposed to be
     * lazily initialized and set.
     */
    public BufferByteInput(final ByteBuffer source) {
        super(source);
    }

    /**
     * {@inheritDoc} The {@code read()} method of {@code BufferByteInput}
     * invokes {@link ByteBuffer#get()} on {@link #getSource()} and return the
     * result as an unsigned int. Override this method if {@link #source} is
     * supposed to be lazily initialized or adjusted.
     *
     * @return {@inheritDoc }
     * @throws IOException {@inheritDoc }
     */
    @Override
    public int read() throws IOException {
        return getSource().get() & 0xFF;
    }
}
