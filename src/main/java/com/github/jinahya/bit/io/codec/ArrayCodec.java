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
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> element type parameter
 */
public class ArrayCodec<T> extends ScaleCodec<T[], T> {

    public ArrayCodec(final boolean nullable, final int scale,
            final BitCodec<T> codec) {

        super(nullable, scale, codec);
    }

    @Override
    protected T[] decodeValue(final BitInput input) throws IOException {

        final int length = readCount(input);

        @SuppressWarnings("unchecked")
        final T[] value = (T[]) new Object[length];
        for (int i = 0; i < value.length; i++) {
            value[i] = codec.decode(input);
        }

        return value;
    }

    @Override
    protected void encodeValue(final BitOutput output, final T[] value)
            throws IOException {

        writeCount(output, value.length);
        for (final T e : value) {
            codec.encode(output, e);
        }
    }

}
