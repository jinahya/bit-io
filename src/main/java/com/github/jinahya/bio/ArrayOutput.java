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
public class ArrayOutput extends AbstractByteOutput<byte[]> {


    public ArrayOutput(final byte[] array, final int offset, final int length) {

        super(array);

        this.offset = offset;
        this.length = length;
    }


    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        target[offset + index++] = (byte) value;
    }


    protected int offset;


    protected int length;


    protected int index;


}

