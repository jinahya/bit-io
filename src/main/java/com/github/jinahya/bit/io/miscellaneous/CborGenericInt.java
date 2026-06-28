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

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * A codec for CBOR integer values, accepting any definite integer width.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc8949.html">RFC 8949: Concise Binary Object Representation</a>
 */
public final class CborGenericInt
        implements BitReader<Long>, BitWriter<Long> {

    public static final CborGenericInt INSTANCE = new CborGenericInt();

    private CborGenericInt() {
        super();
    }

    @Override
    public Long read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        return Long.valueOf(CborInts.read(input, false));
    }

    @Override
    public void write(final BitOutput output, final Long value) throws IOException {
        requireNonNullOutput(output);
        CborInts.write(output, requireNonNullValue(value));
    }
}
