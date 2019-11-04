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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A byte input reads bytes from an {@link InputStream}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @see StreamByteOutput
 */
public class StreamByteInput extends AbstractByteInput<InputStream> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which read bytes from specified input stream.
     *
     * @param source the input stream from which bytes are read; {@code null} if it's supposed to be lazily initialized
     *               and set
     */
    public StreamByteInput(final InputStream source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code StreamByteInput} class {@link InputStream#read() reads} a byte
     * from the {@link #getSource() source} and returns the result.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getSource()
     * @see InputStream#read()
     */
    @Override
    public int read() throws IOException {
        final int value = getSource().read();
        if (value == -1) {
            throw new EOFException("reached to an end");
        }
        return value;
    }
}
