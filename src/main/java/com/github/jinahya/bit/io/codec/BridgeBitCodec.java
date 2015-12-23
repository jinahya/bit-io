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
public abstract class BridgeBitCodec<T, U> extends AbstractBitCodec<T> {


    /**
     * Creates a new instance.
     *
     * @param nullable a flag for nullability
     * @param codec a codec that this codec adapts
     */
    public BridgeBitCodec(final boolean nullable,
                          final BitCodec<U> codec) {

        super(nullable);

        if (codec == null) {
            throw new NullPointerException("null codec");
        }

        this.codec = codec;
    }


    @Override
    protected T decodeValue(final BitInput input) throws IOException {

        return convertFrom(codec.decode(input));
    }


    protected abstract T convertFrom(U u);


    @Override
    protected void encodeValue(final BitOutput output, final T value)
        throws IOException {

        codec.encode(output, convertTo(value));
    }


    protected abstract U convertTo(T t);


    private final BitCodec<U> codec;

}

