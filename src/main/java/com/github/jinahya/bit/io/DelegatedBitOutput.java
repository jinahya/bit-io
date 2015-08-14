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


import java.io.IOException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class DelegatedBitOutput extends AbstractBitOutput {


    public DelegatedBitOutput(final ByteOutput delegate) {

        super();

        this.delegate = delegate;
    }


    /**
     * {@inheritDoc} The {code writeUnsignedByte(int)} method of
     * {@code DelegatedBitOutput} class executes
     * <pre>delegate.writeUnsignedByte(int)</pre> with specified {@code value}.
     * Override this method if {@link #delegate} is supposed to be lazily
     * initialized.
     *
     * @param value {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void writeUnsignedByte(int value) throws IOException {

        delegate.writeUnsignedByte(value);
    }


    /**
     * The delegate on which {@link #writeUnsignedByte(int)} is invoked.
     */
    protected ByteOutput delegate;


}

