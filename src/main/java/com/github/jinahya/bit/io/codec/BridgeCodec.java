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
 * @param <U> adapting value type parameter
 */
public abstract class BridgeCodec<T, U> implements BitCodec<T> {


    /**
     * Creates a new instance.
     *
     * @param codec the codec that this codec bridges.
     */
    protected BridgeCodec(final BitCodec<U> codec) {

        super();

        if (codec == null) {
            throw new NullPointerException("null codec");
        }

        this.codec = codec;
    }


    @Override
    public T decode(final BitInput input) throws IOException {

        final U value = codec.decode(input);
        if (value == null) {
            return null;
        }

        return convertFrom(value);
    }


    @Override
    public void encode(final BitOutput output, final T value)
        throws IOException {

        if (value == null) {
            codec.encode(output, null);
            return;
        }

        codec.encode(output, convertTo(value));
    }


    /**
     * Converts given value to the decoding result of this codec.
     *
     * @param u the value to convert; never {@code null}.
     *
     * @return converted value.
     */
    protected abstract T convertFrom(U u);


    /**
     * Converts given value to the encoding value of {@link #codec}.
     *
     * @param t the value to convert; never {@code null}
     *
     * @return converted value.
     */
    protected abstract U convertTo(T t);


    /**
     * The codec this codec wraps.
     */
    protected final BitCodec<U> codec;

}

