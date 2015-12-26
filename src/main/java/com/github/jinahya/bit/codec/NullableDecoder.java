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

package com.github.jinahya.bit.codec;


import com.github.jinahya.bit.io.BitInput;
import java.io.IOException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> value type parameter
 */
public abstract class NullableDecoder<T> implements BitDecoder<T> {


    public NullableDecoder(final boolean nullable) {

        super();

        this.nullable = nullable;
    }


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
     * Decodes value from specified input.
     *
     * @param input the input
     *
     * @return a decoded value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected abstract T decodeValue(BitInput input) throws IOException;


    public boolean isNullable() {

        return nullable;
    }


    protected final boolean nullable;

}

