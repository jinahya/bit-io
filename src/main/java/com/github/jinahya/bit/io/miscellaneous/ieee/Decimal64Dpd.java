package com.github.jinahya.bit.io.miscellaneous.ieee;

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
 * A codec for the IEEE 754 {@code decimal64} interchange format with its coefficient in Densely Packed Decimal
 * (DPD); 64 bits of a sign, a 5-bit combination field, a 8-bit exponent continuation, and 50 bits of
 * coefficient continuation.
 *
 * <p>A finite value holds up to 16 coefficient digits and an exponent between {@code -398} and
 * {@code 369}, both inclusive. Writing never rounds or clamps; a value out of these ranges, or a NaN whose payload
 * has more than 15 digits, is rejected with an {@link IllegalArgumentException}. Reading accepts every
 * encoding, including non-canonical declets.</p>
 *
 * @see <a href="https://speleotrove.com/decimal/dbspec.html">Decimal Encoding Specification</a>
 */
public final class Decimal64Dpd
        implements BitReader<Decimal>, BitWriter<Decimal> {

    /**
     * The number of bits of the format. The value is {@value}.
     */
    public static final int SIZE = 64;

    /**
     * The singleton instance of this codec.
     */
    public static final Decimal64Dpd INSTANCE = new Decimal64Dpd();

    private static final DecimalDpdFormat FORMAT = new DecimalDpdFormat(8, 16, 398);

    // -----------------------------------------------------------------------------------------------------------------
    private Decimal64Dpd() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Decimal read(final BitInput input) throws IOException {
        return FORMAT.read(input);
    }

    @Override
    public void write(final BitOutput output, final Decimal value) throws IOException {
        FORMAT.write(output, value);
    }
}
