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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import org.mockito.Mockito;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitInputFactoryTest {


    public static void newInstanceWithSupplierToIntFunction() {

        final ReadableByteChannel source
            = Mockito.mock(ReadableByteChannel.class);

        final BitInput input = BitInputFactory.<ByteBuffer>newInstance(
            () -> (ByteBuffer) ByteBuffer.allocate(10).position(10),
            b -> {
                if (!b.hasRemaining()) {
                    b.flip();
                    try {
                        source.read(b);
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                }
                return b.get() & 0xFF;
            });

    }


    public static void newInstanceWithUnaryOperatorToIntFunction() {

        final ReadableByteChannel source
            = Mockito.mock(ReadableByteChannel.class);

        final BitInput input = BitInputFactory.<ByteBuffer>newInstance(
            b -> {
                if (b == null) {
                    return (ByteBuffer) ByteBuffer.allocate(10).position(10);
                }
                if (!b.hasRemaining()) {
                    b.flip();
                    try {
                        source.read(b);
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                }
                return b;
            },
            b -> b.get() & 0xFF
        );
    }

}

