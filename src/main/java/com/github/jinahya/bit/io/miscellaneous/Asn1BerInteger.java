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
import java.math.BigInteger;

/**
 * A deprecated alias of {@link com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerInteger}, which this class
 * delegates to.
 *
 * @deprecated Use {@link com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerInteger} instead;
 *             this class will be removed in the next major version.
 */
@Deprecated
public final class Asn1BerInteger
        implements BitReader<BigInteger>, BitWriter<BigInteger> {

    /**
     * The singleton instance of this class.
     */
    public static final Asn1BerInteger INSTANCE = new Asn1BerInteger();

    // -----------------------------------------------------------------------------------------------------------------
    private Asn1BerInteger() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public BigInteger read(final BitInput input) throws IOException {
        return com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerInteger.INSTANCE.read(input);
    }

    @Override
    public void write(final BitOutput output, final BigInteger value) throws IOException {
        com.github.jinahya.bit.io.miscellaneous.itu.Asn1BerInteger.INSTANCE.write(output, value);
    }
}
