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
import java.io.RandomAccessFile;

/**
 * A byte input which reads bytes from a random access file.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see RandomAccessFileByteOutput
 */
class RandomAccessFileByteInput extends AbstractByteInput<RandomAccessFile> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified random access file.
     *
     * @param source the random access file from which bytes are read; {@code null} if it's supposed to be lazily
     *               initialized and set.
     * @see #getSource()
     * @see #setSource(RandomAccessFile)
     */
    public RandomAccessFileByteInput(final RandomAccessFile source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@link RandomAccessFileByteInput} class invokes {@link
     * RandomAccessFile#readUnsignedByte()}, on what {@link #getSource()} method returns, and returns the result.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return getSource().readUnsignedByte();
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Override
    protected RandomAccessFile getSource() {
        return super.getSource();
    }

    @Override
    protected void setSource(final RandomAccessFile source) {
        super.setSource(source);
    }
}
