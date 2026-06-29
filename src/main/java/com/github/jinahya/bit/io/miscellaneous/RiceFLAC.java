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

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

/**
 * A value-level codec for one FLAC Rice-coded signed residual.
 *
 * <p>This class encodes only the signed residual value for a supplied Rice parameter. It does not encode FLAC residual
 * partition headers, method selectors, escape/raw residuals, predictor order, or block-level constraints.</p>
 *
 * @see <a href="https://xiph.org/flac/format.html">FLAC format specification</a>
 */
public final class RiceFLAC
        implements LongBitReader, LongBitWriter {

    /**
     * Returns a codec for specified Rice parameter.
     *
     * @param parameter the Rice parameter; between {@code 0} and {@code 62}, both inclusive.
     * @return a codec for {@code parameter}.
     */
    public static RiceFLAC of(final int parameter) {
        return new RiceFLAC(parameter);
    }

    private RiceFLAC(final int parameter) {
        super();
        this.parameter = RiceGolombCodeUtil.requireParameter(parameter);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long readLong(final BitInput input) throws IOException {
        requireNonNullInput(input);
        return RiceGolombCodeUtil.unfoldSigned(RiceGolombCodeUtil.readUnsigned(input, parameter, 0));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        RiceGolombCodeUtil.writeUnsigned(output, parameter, RiceGolombCodeUtil.foldSigned(value), 0);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the Rice parameter held by this codec.
     *
     * @return the Rice parameter held by this codec.
     */
    public int parameter() {
        return parameter;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final int parameter;
}
