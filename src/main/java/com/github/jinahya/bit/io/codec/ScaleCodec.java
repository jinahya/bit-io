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
 * A codec class uses a {@code scale} for counting.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> value type parameter
 * @param <U> adapting value type parameter
 */
public abstract class ScaleCodec<T, U> extends NullableCodec<T> {


    public static int readCount(final BitInput input, final int scale)
        throws IOException {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        BitIoConstraints.requireValidSize(true, 5, scale);

        return input.readInt(true, scale);
    }


    public static void writeCount(final BitOutput output, final int scale,
                                  final int count)
        throws IOException {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        BitIoConstraints.requireValidSize(true, 5, scale);

        if (count < 0) {
            throw new IllegalArgumentException("count(" + count + ") < 0");
        }
        final int shifted = count >> scale;
        if (shifted > 0) {
            throw new IllegalArgumentException(
                shifted + "(" + count + " >> " + scale + ") > 0");
        }

        output.writeInt(true, scale, count);
    }


    /**
     * Creates a new instance.
     *
     * @param nullable a flag for nullability
     * @param scale length scale
     * @param codec a codec that this codec adapts
     */
    public ScaleCodec(final boolean nullable, final int scale,
                      final BitCodec<U> codec) {

        super(nullable);

        BitIoConstraints.requireValidSize(true, 5, scale);

        if (codec == null) {
            throw new NullPointerException("null codec");
        }

        this.scale = scale;
        this.codec = codec;
    }


    protected int readCount(final BitInput input) throws IOException {

        return readCount(input, scale);
    }


    protected void writeCount(final BitOutput output, final int count)
        throws IOException {

        writeCount(output, scale, count);
    }


    protected final int scale;

    protected final BitCodec<U> codec;

}

