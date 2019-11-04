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

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A byte output writes bytes to a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput extends AbstractByteOutput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which writes bytes to specified byte buffer.
     *
     * @param target the byte buffer to which bytes are written; {@code null} if it's supposed to be lazily initialized
     *               and set.
     */
    public BufferByteOutput(final ByteBuffer target) {
        super(target);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code BufferByteOutput} class {@link ByteBuffer#put(byte) puts}
     * specified value to {@link #getTarget() target}.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getTarget()
     * @see ByteBuffer#put(byte)
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget().put((byte) value);
    }
}
