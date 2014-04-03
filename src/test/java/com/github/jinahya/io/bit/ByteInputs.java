/*
 * Copyright 2014 Jin Kwon.
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


import java.io.EOFException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;


/**
 *
 * @author Jin Kwon
 */
public class ByteInputs {


    public static ByteInput newInstance(final InputStream source) {

        if (source == null) {
            throw new NullPointerException("null source");
        }

        return () -> source.read();
    }


    public static ByteInput newInstance(final ByteBuffer source) {

        if (source == null) {
            throw new NullPointerException("null source");
        }

        return () -> source.get() & 0xFF;
    }


    public static ByteInput newInstance(final ReadableByteChannel source) {

        if (source == null) {
            throw new NullPointerException("null source");
        }

        final int capacity = 1;
        final ByteBuffer buffer = ByteBuffer.allocate(capacity);
        assert buffer.capacity() != 0;
        //buffer.compact(); // position->n;limit->capacity
        //buffer.flip(); // limit->position; position->zero
        buffer.position(buffer.limit()); // drain
        assert !buffer.hasRemaining();

        return () -> {
            if (!buffer.hasRemaining()) {
                do {
                    if (source.read(buffer) == -1) {
                        throw new EOFException("no bytes in channel");
                    }
                } while (buffer.position() == 0);
            }
            buffer.flip();
            assert buffer.hasRemaining();
            return buffer.get() & 0xFF;
        };
    }


    public static ByteInput newInstance() {

        return () -> (int) (System.currentTimeMillis() & 0xFF);
    }


}

