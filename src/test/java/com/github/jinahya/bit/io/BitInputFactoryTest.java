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


import java.io.EOFException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import org.mockito.Mockito;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitInputFactoryTest {


    @Test
    public static void newInstanceWithSupplierToIntFunction() {

        final ReadableByteChannel source
            = Mockito.mock(ReadableByteChannel.class);

        final BitInput input = BitInputFactory.newInstance(
            () -> (ByteBuffer) ByteBuffer.allocate(10).position(10),
            b -> {
                if (!b.hasRemaining()) {
                    b.clear(); // position->zero; limit->capacity;
                    int read;
                    try {
                        while ((read = source.read(b)) == 0) {
                        }
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                    if (read == -1) {
                        throw new UncheckedIOException(new EOFException());
                    }
                    b.flip();
                }
                return b.get() & 0xFF;
            });

    }


    @Test
    public static void newInstanceWithUnaryOperatorToIntFunction() {

        final ReadableByteChannel source
            = Mockito.mock(ReadableByteChannel.class);

        final BitInput input = BitInputFactory.<ByteBuffer>newInstance(
            b -> {
                if (b == null) {
                    return (ByteBuffer) ByteBuffer.allocate(10).position(10);
                }
                if (!b.hasRemaining()) {
                    b.clear(); // position->zero; limit->capacity;
                    int read;
                    try {
                        while ((read = source.read(b)) == 0) {
                        }
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                    if (read == -1) {
                        throw new UncheckedIOException(new EOFException());
                    }
                    b.flip();
                }
                return b;
            },
            b -> b.get() & 0xFF
        );
    }

}

