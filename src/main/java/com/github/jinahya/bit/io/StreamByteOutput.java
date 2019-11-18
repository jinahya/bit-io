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
import java.io.OutputStream;

/**
 * A byte output writes bytes to an {@link OutputStream}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StreamByteInput
 */
public class StreamByteOutput extends AbstractByteOutput<OutputStream> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which writes bytes to specified output stream.
     *
     * @param target the output stream to which bytes are written; {@code null} if it's supposed to be lazily
     *               initialized and set.
     */
    public StreamByteOutput(final OutputStream target) {
        super(target);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code StreamByteOutput} class invokes {@link
     * OutputStream#write(int) write(int)} method on {@link #getTarget() target} with specified value.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getTarget()
     * @see OutputStream#write(int)
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget().write(value);
    }

    // ---------------------------------------------------------------------------------------------------------- target
    @Override
    protected OutputStream getTarget() {
        return super.getTarget();
    }

    @Override
    protected void setTarget(final OutputStream target) {
        super.setTarget(target);
    }
}
