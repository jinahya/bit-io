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
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;

import java.io.IOException;

/**
 * A codec for unsigned Exp-Golomb {@code ue(v)} values.
 *
 * <p>The codec accepts and returns non-negative {@code long} values; encoded values beyond
 * {@link Long#MAX_VALUE} are rejected.</p>
 *
 * @see ExpGolombSE
 */
public final class ExpGolombUE
        implements BitReader<Long>, BitWriter<Long> {

    /**
     * The singleton instance of this codec.
     */
    public static final ExpGolombUE INSTANCE = new ExpGolombUE();

    static long readCodeNum(final BitInput input) throws IOException {
        requireInput(input);
        int leadingZeros = 0;
        while (input.readInt(true, 1) == 0) {
            if (++leadingZeros > Long.SIZE - 1) {
                throw new IOException("Exp-Golomb codeNum exceeds signed long range");
            }
        }
        if (leadingZeros == 0) {
            return 0L;
        }
        final long suffix = input.readLong(true, leadingZeros);
        if (leadingZeros == Long.SIZE - 1) {
            if (suffix != 0L) {
                throw new IOException("Exp-Golomb codeNum exceeds signed long range");
            }
            return Long.MAX_VALUE;
        }
        return ((1L << leadingZeros) - 1L) + suffix;
    }

    static void writeCodeNum(final BitOutput output, final long codeNum) throws IOException {
        requireOutput(output);
        if (codeNum < 0L) {
            throw new IllegalArgumentException("negative codeNum: " + codeNum);
        }
        final int leadingZeros;
        final long suffix;
        if (codeNum == Long.MAX_VALUE) {
            leadingZeros = Long.SIZE - 1;
            suffix = 0L;
        } else {
            final long info = codeNum + 1L;
            leadingZeros = Long.SIZE - 1 - Long.numberOfLeadingZeros(info);
            suffix = info - (1L << leadingZeros);
        }
        if (leadingZeros > 0) {
            output.writeLong(true, leadingZeros, 0L);
        }
        output.writeInt(true, 1, 1);
        if (leadingZeros > 0) {
            output.writeLong(true, leadingZeros, suffix);
        }
    }

    static void requireInput(final BitInput input) {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
    }

    static void requireOutput(final BitOutput output) {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
    }

    private ExpGolombUE() {
        super();
    }

    @Override
    public Long read(final BitInput input) throws IOException {
        return readCodeNum(input);
    }

    @Override
    public void write(final BitOutput output, final Long value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (value < 0L) {
            throw new IllegalArgumentException("negative value: " + value);
        }
        writeCodeNum(output, value);
    }
}
