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
import com.github.jinahya.bit.io.BitIoConstraints;
import com.github.jinahya.bit.io.BitOutput;
import java.io.IOException;


/**
 * An abstract class for implementing {@code BitCodec}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> value type parameter
 * @param <U> adapting value type parameter
 */
abstract class ScaledBitCodec<T, U> extends AbstractBitCodec<T> {


    protected static int readLength(final BitInput input, final int scale) throws IOException {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return input.readUnsignedInt(scale);
    }


    protected static void writeLength(final BitOutput output, final int scale, final int length)
        throws IOException {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        BitIoConstraints.requireValidLength(scale, length);

        output.writeUnsignedInt(scale, length);
    }


    /**
     * Creates a new instance.
     *
     * @param nullable a flag for nullability
     * @param scale length scale
     * @param codec a codec that this codec adapts
     */
    public ScaledBitCodec(final boolean nullable, final int scale,
                          final BitCodec<U> codec) {

        super(nullable);

        if (codec == null) {
            throw new NullPointerException("null codec");
        }

        this.scale = BitIoConstraints.requireValidScale(scale);
        this.codec = codec;
    }


    protected int readLength(final BitInput input) throws IOException {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return input.readUnsignedInt(scale);
    }


    protected void writeLength(final BitOutput output, final int length)
        throws IOException {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        BitIoConstraints.requireValidLength(scale, length);

        output.writeUnsignedInt(scale, length);
    }


    private final int scale;


    protected final BitCodec<U> codec;

}

