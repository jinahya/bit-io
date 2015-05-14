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


    public ArrayOutput(final byte[] array, final int offset) {

        super(array);

        this.offset = offset;
    }


    @Override
    protected byte[] requireValidTarget() {

        final byte[] target = super.requireValidTarget();

        if (target.length == 0) {
            throw new IllegalStateException(
                "The underlying byte target's length is 0");
        }

        return target;
    }


    public int getOffset() {

        return offset;
    }


    protected int requireValidOffset() {

        if (offset < 0) {
            throw new IllegalStateException("offset(" + offset + ") < 0");
        }

        if (offset >= requireValidTarget().length) {
            throw new IllegalStateException(
                "offset(" + offset + ") >= target.length("
                + requireValidTarget().length + ")");
        }

        return offset;
    }


    public void setOffset(final int offset) {

        this.offset = offset;
    }


    public int getLength() {

        return length;
    }


    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        requireValidTarget()[requireValidOffset() + length++] = (byte) value;
    }


    private int offset;


    private int length = 0;


}

