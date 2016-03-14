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
import java.util.Collection;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> collection type parameter
 * @param <E> element type parameter
 */
public class CollectionCodec<T extends Collection<E>, E>
        extends ScaleCodec<T, E> {

    public CollectionCodec(final boolean nullable, final int scale,
            final BitCodec<E> codec, final Class<T> type) {

        super(nullable, scale, codec);

        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T decodeValue(final BitInput input) throws IOException {

        final int size = readCount(input);

        final Collection<E> value;
        try {
            value = type.newInstance();
        } catch (final InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

        for (int i = 0; i < size; i++) {
            value.add(codec.decode(input));
        }

        return null;
    }

    @Override
    protected void encodeValue(final BitOutput output, final T collection)
            throws IOException {

        writeCount(output, collection.size());

        for (final E element : collection) {
            codec.encode(output, element);
        }
    }

    private final Class<T> type;

}
