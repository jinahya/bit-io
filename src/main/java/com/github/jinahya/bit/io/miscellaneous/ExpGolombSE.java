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
import com.github.jinahya.bit.io.LongBitReader;
import com.github.jinahya.bit.io.LongBitWriter;

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous.ExpGolombConstants.MAX_SE;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombConstants.MIN_SE;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

/**
 * A codec for signed Exp-Golomb {@code se(v)} values.
 *
 * <p>Because this codec stores the underlying {@code codeNum} in a non-negative {@code long}, the supported signed
 * range is {@code -4611686018427387903} through {@code 4611686018427387904}, both inclusive.</p>
 *
 * @see <a href="https://www.itu.int/rec/T-REC-H.264">ITU-T H.264: Advanced video coding</a>
 * @see ExpGolombUE
 */
public final class ExpGolombSE
        implements LongBitReader, LongBitWriter {

    /**
     * The singleton instance of this codec.
     */
    public static final ExpGolombSE INSTANCE = new ExpGolombSE();

    private static long codeNum(final long value) {
        if (value < MIN_SE || value > MAX_SE) {
            throw new IllegalArgumentException("value out of range: " + value);
        }
        return value > 0L ? ((value - 1L) << 1) | 0x01L : (-value) << 1;
    }

    private static long value(final long codeNum) {
        return (codeNum & 0x01L) == 0L ? -(codeNum >>> 1) : (codeNum >>> 1) + 1L;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ExpGolombSE() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long readLong(final BitInput input) throws IOException {
        requireNonNullInput(input);
        return value(ExpGolombUE.readCodeNum(input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        ExpGolombUE.writeCodeNum(output, codeNum(value));
    }
}
