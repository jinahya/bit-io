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
 * A {@link ByteOutput} implementation uses a byte array.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayOutput extends AbstractByteOutput<byte[]> {


    public ArrayOutput(final byte[] target, final int index, final int limit) {

        super(target);

        this.index = index;
        this.limit = limit;
    }


    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        if (index >= limit) {
            throw new IndexOutOfBoundsException();
        }

        target[index++] = (byte) value;
    }


    public ArrayOutput target(final byte[] target) {

        setTarget(target);

        return this;
    }


    public int getIndex() {

        return index;
    }


    public void setIndex(int index) {

        this.index = index;
    }


    public ArrayOutput index(final int index) {

        setIndex(index);

        return this;
    }


    public int getLimit() {

        return limit;
    }


    public void setLimit(int limit) {

        this.limit = limit;
    }


    public ArrayOutput limit(final int limit) {

        setLimit(limit);

        return this;
    }


    /**
     * The index in the {@link #target} to write.
     */
    protected int index;


    /**
     * The position in the {@link #target} which {@link #index} can't exceed.
     */
    protected int limit;

}

