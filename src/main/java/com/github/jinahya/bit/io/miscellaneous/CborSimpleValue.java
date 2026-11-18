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
import com.github.jinahya.bit.io.IntBitReader;
import com.github.jinahya.bit.io.IntBitWriter;

import java.io.IOException;

/**
 * A deprecated alias of {@link com.github.jinahya.bit.io.miscellaneous.ietf.CborSimpleValue}, which this class
 * delegates to.
 *
 * @deprecated Use {@link com.github.jinahya.bit.io.miscellaneous.ietf.CborSimpleValue} instead;
 *             this class will be removed in the next major version.
 */
@Deprecated
public final class CborSimpleValue
        implements IntBitReader, IntBitWriter {

    /**
     * The singleton instance of this class.
     */
    public static final CborSimpleValue INSTANCE = new CborSimpleValue();

    // -----------------------------------------------------------------------------------------------------------------
    private CborSimpleValue() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int readInt(final BitInput input) throws IOException {
        return com.github.jinahya.bit.io.miscellaneous.ietf.CborSimpleValue.INSTANCE.readInt(input);
    }

    @Override
    public void writeInt(final BitOutput output, final int value) throws IOException {
        com.github.jinahya.bit.io.miscellaneous.ietf.CborSimpleValue.INSTANCE.writeInt(output, value);
    }
}
