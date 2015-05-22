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


package com.github.jinahya.bio;


import java.io.IOException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayInput extends AbstractByteInput<byte[]> {


    public ArrayInput(final byte[] array, final int offset, final int length) {

        super(array);

        this.offset = offset;
        this.length = length;
    }


    /**
     * {@inheritDoc} The {@link #readUnsignedByte()} of {@code ArrayInput} class
     * returns
     * <pre>source[offset + index++] &amp; 0xFF</pre>.
     *
     * @return {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}.
     *
     * @see #source
     * @see #offset
     * @see #index
     */
    @Override
    public int readUnsignedByte() throws IOException {

        return source[offset + index++] & 0xFF;
    }


    protected int offset;


    protected int length;


    protected int index;


}

