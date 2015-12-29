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

package com.github.jinahya.bit.io.codec;


import com.github.jinahya.bit.io.BitIoTests;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.BiConsumer;
import static org.testng.Assert.assertEquals;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BitCodecTests {


    /**
     * Encodes given value using specified codec and accepts to specified
     * consumer with the value decoded by the same codec.
     *
     * @param <T> codec type parameter
     * @param <V> value type parameter
     * @param codec the codec
     * @param expected the value to encode
     * @param consumer the consumer to accept
     *
     * @throws IOException if an I/O error occurs.
     */
    public static <T extends BitCodec<V>, V> void test(
        final T codec, final V expected,
        final BiConsumer<? super V, ? super V> consumer)
        throws IOException {

        BitIoTests.all(
            o -> {
                try {
                    codec.encode(o, expected);
                } catch (final IOException ioe) {
                    throw new UncheckedIOException(ioe);
                }
            },
            i -> {
                final V actual;
                try {
                    actual = codec.decode(i);
                } catch (final IOException ioe) {
                    throw new UncheckedIOException(ioe);
                }
                consumer.accept(actual, expected);
            });
    }


    /**
     * Encodes given value using specified codec and compares to the value
     * decoded using the same codec.
     *
     * @param <T> codec type parameter
     * @param <V> value type parameter
     * @param codec the codec
     * @param expected the value to encode
     *
     * @throws IOException if an I/O error occurs.
     */
    public static <T extends BitCodec<V>, V> void test(final T codec,
                                                       final V expected)
        throws IOException {

        if (codec == null) {
            throw new NullPointerException("null codec");
        }

        test(codec, expected, (a, e) -> {
             assertEquals(a, e);
         });

    }


}

