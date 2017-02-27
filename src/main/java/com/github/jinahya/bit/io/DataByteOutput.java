/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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

import java.io.DataOutput;
import java.io.IOException;

/**
 * An {@code ByteOutput} uses an instance of {@link DataOutput} as its
 * {@link #target}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DataByteInput
 */
public class DataByteOutput extends AbstractByteOutput<DataOutput> {

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance with given byte target.
     *
     * @param target the byte target; {@code null} if it is supposed to be
     * lazily initialized and set.
     */
    public DataByteOutput(final DataOutput target) {
        super(target);
    }

    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} The {@code write(int)} method of {@code DataByteOutput}
     * class invokes {@link DataOutput#writeByte(int)} on {@link #target} with
     * specified {@code value}. Override this method if the {@link #target} is
     * supposed to be lazily initialized and set.
     *
     * @param value {@inheritDoc }
     * @throws IOException {@inheritDoc }
     */
    @Override
    public void write(final int value) throws IOException {
        target.writeByte(value);
    }

    // ------------------------------------------------------------------ target
    @Override
    public DataByteOutput target(final DataOutput target) {
        return (DataByteOutput) super.target(target);
    }
}
