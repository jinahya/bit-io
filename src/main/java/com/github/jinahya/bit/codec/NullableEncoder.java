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


import com.github.jinahya.bit.io.BitOutput;
import java.io.IOException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> value type parameter
 */
public abstract class NullableEncoder<T> implements BitEncoder<T> {


    public NullableEncoder(final boolean nullable) {

        super();

        this.nullable = nullable;
    }


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

        if (nullable || value != null) {
            encodeValue(output, value);
        }
    }


    /**
     * Encodes given value to specified output.
     *
     * @param output the output
     * @param value the value to encode
     *
     * @throws IOException if an I/O error occurs.
     */
    protected abstract void encodeValue(BitOutput output, T value)
        throws IOException;


    public boolean isNullable() {

        return nullable;
    }


    protected final boolean nullable;

}

