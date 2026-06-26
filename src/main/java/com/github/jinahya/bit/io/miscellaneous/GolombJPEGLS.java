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
 * A value-level codec for one JPEG-LS-style Golomb-coded signed prediction error.
 *
 * <p>This class encodes only a signed error value for a supplied Golomb parameter. It applies the usual non-negative
 * mapped-error folding, then writes a JPEG-LS-style unary quotient using one bits followed by a zero stop bit. It does
 * not implement JPEG-LS prediction, context modeling, adaptive parameter update, run mode, {@code LIMIT},
 * {@code RESET}, or near-lossless reconstruction.</p>
 */
public final class GolombJPEGLS
        implements BitReader<Long>, BitWriter<Long> {

    /**
     * Returns a codec for specified Golomb parameter.
     *
     * @param parameter the Golomb parameter; between {@code 0} and {@code 62}, both inclusive.
     * @return a codec for {@code parameter}.
     */
    public static GolombJPEGLS of(final int parameter) {
        return new GolombJPEGLS(parameter);
    }

    private GolombJPEGLS(final int parameter) {
        super();
        this.parameter = RiceGolombCodes.requireParameter(parameter);
    }

    @Override
    public Long read(final BitInput input) throws IOException {
        return RiceGolombCodes.unfoldSigned(RiceGolombCodes.readUnsigned(input, parameter, 1));
    }

    @Override
    public void write(final BitOutput output, final Long value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        RiceGolombCodes.writeUnsigned(output, parameter, RiceGolombCodes.foldSigned(value), 1);
    }

    /**
     * Returns the Golomb parameter held by this codec.
     *
     * @return the Golomb parameter held by this codec.
     */
    public int parameter() {
        return parameter;
    }

    private final int parameter;
}
