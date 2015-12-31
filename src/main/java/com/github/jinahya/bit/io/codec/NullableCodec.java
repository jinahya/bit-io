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


import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import java.io.IOException;


/**
 * An abstract class for implementing {@code BitCodec}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> value type parameter
 */
public abstract class NullableCodec<T> extends Nullable implements BitCodec<T> {


    /**
     * Creates a new instance with specified decoder and encoder.
     *
     * @param <T> value type parameter
     * @param nullable the nullable flag
     * @param decoder the decoder
     * @param encoder the encoder
     *
     * @return a new instance.
     */
    public static <T> NullableCodec<T> newInstance(
        final boolean nullable, final BitDecoder<? extends T> decoder,
        final BitEncoder<? super T> encoder) {

        return new NullableCodec<T>(nullable) {

            @Override
            protected T decodeValue(final BitInput input) throws IOException {

                return decoder.decode(input);
            }


            @Override
            protected void encodeValue(final BitOutput output, final T value)
                throws IOException {

                encoder.encode(output, value);
            }

        };
    }


    /**
     * Creates a new instance with specified codec.
     *
     * @param <T> value type parameter
     * @param nullable the nullable flag
     * @param codec the codec
     *
     * @return a new instance.
     */
    public static <T> NullableCodec<T> newInstance(
        final boolean nullable, final BitCodec<T> codec) {

        if (codec == null) {
            throw new NullPointerException("null codec");
        }

        return new NullableCodec<T>(nullable) {

            @Override
            protected T decodeValue(final BitInput input) throws IOException {

                return codec.decode(input);
            }


            @Override
            protected void encodeValue(final BitOutput output, final T value)
                throws IOException {

                codec.encode(output, value);
            }

        };
    }


    /**
     * Creates a new instance.
     *
     * @param nullable a flag for nullability of the value.
     */
    public NullableCodec(final boolean nullable) {

        super(nullable);
    }


    /**
     * {@inheritDoc} This method optionally (by the value of {@link #nullable})
     * decodes additional 1-bit boolean and, if the value need to be decoded,
     * invokes {@link #decodeValue(com.github.jinahya.bit.io.BitInput)} with
     * given {@code input}.
     *
     * @param input {@inheritDoc}
     *
     * @return {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public T decode(final BitInput input) throws IOException {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        if (nullable && !input.readBoolean()) {
            return null;
        }

        return decodeValue(input);
    }


    /**
     * Decodes value from given input.
     *
     * @param input the input
     *
     * @return decoded value
     *
     * @throws IOException if an I/O error occurs.
     */
    protected abstract T decodeValue(final BitInput input) throws IOException;


    /**
     * {@inheritDoc} This method optionally (by the value of {@link #nullable})
     * encodes additional 1-bit boolean flag and, if the value should be
     * encoded, invokes
     * {@link #encodeValue(com.github.jinahya.bit.io.BitOutput, java.lang.Object)}
     * with given input and value.
     *
     * @param output {@inheritDoc}
     * @param value {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void encode(final BitOutput output, final T value)
        throws IOException {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        if (!nullable && value == null) {
            throw new NullPointerException("null value");
        }

        if (nullable) {
            output.writeBoolean(value != null);
        }

        if (!nullable || value != null) {
            encodeValue(output, value);
        }
    }


    /**
     * Encodes specified value to given output.
     *
     * @param output the output
     * @param value the value to encode
     *
     * @throws IOException if an I/O error occurs.
     */
    protected abstract void encodeValue(BitOutput output, T value)
        throws IOException;

}

