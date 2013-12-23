/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class MockedByteInput extends ByteInput<Void> {


    public static ByteInput<Void> newUnlimitedInstance() {

        return new MockedByteInput(-1L);
    }


    public MockedByteInput(final long limit) {

        super(null);

        if (limit < -1L) {
            throw new IllegalArgumentException("limit(" + limit + ") < -1L");
        }

        this.limit = limit;
    }


    @Override
    public int readUnsignedByte() throws IOException {

        if (limit != -1L && count++ >= limit) {
            return -1;
        }

        return (int) (System.currentTimeMillis() & 0xFFL);
    }


    @Override
    public void close() throws IOException {

        // do nothing
    }


    private final long limit;


    private long count;


}

