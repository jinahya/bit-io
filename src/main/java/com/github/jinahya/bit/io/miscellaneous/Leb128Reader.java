package com.github.jinahya.bit.io.miscellaneous;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
 * %%
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
 * #L%
 */

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitReader;

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.MASK_CONTINUATION;
import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.MASK_PAYLOAD;
import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.MASK_SIGN;
import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.SIZE_PAYLOAD;

/**
 * A reader for LEB128-encoded {@code long} values.
 *
 * @see Leb128Writer
 */
public abstract class Leb128Reader
        implements BitReader<Long> {

    private static void requireInput(final BitInput input) {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
    }

    private static final class Unsigned
            extends Leb128Reader {

        @Override
        long readValue(final BitInput input) throws IOException {
            requireInput(input);
            long value = 0L;
            for (int shift = 0; ; shift += SIZE_PAYLOAD) {
                final int group = input.readInt(true, Byte.SIZE);
                final int payload = group & MASK_PAYLOAD;
                if (shift == Long.SIZE - 1 && payload != 0) {
                    throw new IOException("unsigned LEB128 value exceeds signed long range");
                }
                value |= ((long) payload) << shift;
                if ((group & MASK_CONTINUATION) == 0) {
                    return value;
                }
                if (shift >= Long.SIZE - 1) {
                    throw new IOException("unterminated unsigned LEB128 value");
                }
            }
        }
    }

    private static final class Signed
            extends Leb128Reader {

        @Override
        long readValue(final BitInput input) throws IOException {
            requireInput(input);
            long value = 0L;
            for (int shift = 0; ; shift += SIZE_PAYLOAD) {
                final int group = input.readInt(true, Byte.SIZE);
                final int payload = group & MASK_PAYLOAD;
                if (shift == Long.SIZE - 1 && payload != 0x00 && payload != MASK_PAYLOAD) {
                    throw new IOException("signed LEB128 value exceeds long range");
                }
                value |= ((long) payload) << shift;
                if ((group & MASK_CONTINUATION) == 0) {
                    final int nextShift = shift + SIZE_PAYLOAD;
                    if (nextShift < Long.SIZE && (payload & MASK_SIGN) != 0) {
                        value |= -1L << nextShift;
                    }
                    return value;
                }
                if (shift >= Long.SIZE - 1) {
                    throw new IOException("unterminated signed LEB128 value");
                }
            }
        }
    }

    /**
     * A reader for unsigned LEB128 values representable as non-negative {@code long} values.
     */
    public static final Leb128Reader UNSIGNED = new Unsigned();

    /**
     * A reader for signed LEB128 values.
     */
    public static final Leb128Reader SIGNED = new Signed();

    // -----------------------------------------------------------------------------------------------------------------
    private Leb128Reader() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public final Long read(final BitInput input) throws IOException {
        return readValue(input);
    }

    abstract long readValue(BitInput input) throws IOException;
}
