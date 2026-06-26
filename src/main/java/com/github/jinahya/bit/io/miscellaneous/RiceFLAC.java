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
 * A value-level codec for one FLAC Rice-coded signed residual.
 *
 * <p>This class encodes only the signed residual value for a supplied Rice parameter. It does not encode FLAC residual
 * partition headers, method selectors, escape/raw residuals, predictor order, or block-level constraints.</p>
 */
public final class RiceFLAC
        implements BitReader<Long>, BitWriter<Long> {

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
        this.parameter = RiceGolombCodes.requireParameter(parameter);
    }

    @Override
    public Long read(final BitInput input) throws IOException {
        return RiceGolombCodes.unfoldSigned(RiceGolombCodes.readUnsigned(input, parameter, 0));
    }

    @Override
    public void write(final BitOutput output, final Long value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        RiceGolombCodes.writeUnsigned(output, parameter, RiceGolombCodes.foldSigned(value), 0);
    }

    /**
     * Returns the Rice parameter held by this codec.
     *
     * @return the Rice parameter held by this codec.
     */
    public int parameter() {
        return parameter;
    }

    private final int parameter;
}
