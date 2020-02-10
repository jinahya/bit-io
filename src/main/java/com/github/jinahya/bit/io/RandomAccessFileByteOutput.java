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
 * A byte input which writes bytes to a random access file.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see RandomAccessFileByteInput
 */
class RandomAccessFileByteOutput extends AbstractByteOutput<RandomAccessFile> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified random access file.
     *
     * @param target the random access to which bytes are written; {@code null} if it's supposed to be lazily
     *               initialized and set.
     * @see #getTarget()
     * @see #setTarget(RandomAccessFile)
     */
    public RandomAccessFileByteOutput(final RandomAccessFile target) {
        super(target);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code RandomAccessFileByteOutput} class invokes {@link
     * RandomAccessFile#writeByte(int)}, on what {@link #getTarget()} method returns, with specified value.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget().writeByte(value);
    }

    // ---------------------------------------------------------------------------------------------------------- target
    @Override
    protected RandomAccessFile getTarget() {
        return super.getTarget();
    }

    @Override
    protected void setTarget(final RandomAccessFile target) {
        super.setTarget(target);
    }
}
