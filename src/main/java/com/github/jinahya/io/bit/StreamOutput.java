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
import java.io.OutputStream;


/**
 * A {@link ByteOutput} implementation for {@link OutputStream}s.
 */
public class StreamOutput extends ByteOutput<OutputStream> {


    /**
     * Creates a new instance built on top of the specified output stream.
     *
     * @param target {@inheritDoc}
     */
    public StreamOutput(final OutputStream target) {

        super(target);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code writeUnsginedByte(int)} method of {@code StreamReader} class
     * calls {@link OutputStream#write(int)} on {@link #target} with
     * {@code value}. Override this method if {@link #target} is intended to be
     * lazily initialized and set.
     *
     * @param value {@inheritDoc }
     *
     * @throws IllegalStateException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     *
     * @see OutputStream#write(int)
     */
    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        if (target == null) {
            throw new IllegalStateException("#target is currently null");
        }

        target.write(value);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code close} method of {@code StreamOutput} class calls, if
     * {@link #target} is not {@code null}, {@link OutputStream#flush()} and
     * {@link OutputStream#close()} in series on {@link #target}.
     *
     * @throws IOException {@inheritDoc }
     *
     * @see OutputStream#flush()
     * @see OutputStream#close()
     */
    @Override
    public void close() throws IOException {

        if (target != null) {
            target.flush();
            target.close();
        }
    }


}

