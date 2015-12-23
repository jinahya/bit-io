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
 * @param <T>
 */
public abstract class AbstractBitCodec<T> implements BitCodec<T> {


    public AbstractBitCodec(final boolean nullable) {

        super();

        this.nullable = nullable;
    }


    @Override
    public T decode(final BitInput input) throws IOException {

        if (nullable) {
            if (!input.readBoolean()) {
                return null;
            }
        }

        return decodeValue(input);
    }


    protected abstract T decodeValue(final BitInput input) throws IOException;


    @Override
    public void encode(final BitOutput output, final T value)
        throws IOException {

        if (nullable) {
            output.writeBoolean(value != null);
        }

        if (!nullable || value != null) {
            encodeValue(output, value);
        }
    }


    protected abstract void encodeValue(BitOutput otuput, T value)
        throws IOException;


    private final boolean nullable;

}

