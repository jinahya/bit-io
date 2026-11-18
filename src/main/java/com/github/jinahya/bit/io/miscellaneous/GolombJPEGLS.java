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

/**
 * A deprecated alias of {@link com.github.jinahya.bit.io.miscellaneous.itu.GolombJPEGLS}, which this class
 * delegates to.
 *
 * @deprecated Use {@link com.github.jinahya.bit.io.miscellaneous.itu.GolombJPEGLS} instead;
 *             this class will be removed in the next major version.
 */
@Deprecated
public final class GolombJPEGLS
        implements LongBitReader, LongBitWriter {

    /**
     * Returns an instance for specified parameter.
     *
     * @param parameter the parameter.
     * @return an instance for {@code parameter}.
     * @see com.github.jinahya.bit.io.miscellaneous.itu.GolombJPEGLS#of(int)
     */
    public static GolombJPEGLS of(final int parameter) {
        return new GolombJPEGLS(com.github.jinahya.bit.io.miscellaneous.itu.GolombJPEGLS.of(parameter));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private GolombJPEGLS(final com.github.jinahya.bit.io.miscellaneous.itu.GolombJPEGLS delegate) {
        super();
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long readLong(final BitInput input) throws IOException {
        return delegate.readLong(input);
    }

    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        delegate.writeLong(output, value);
    }

    /**
     * Returns the parameter of this instance.
     *
     * @return the parameter of this instance.
     */
    public int parameter() {
        return delegate.parameter();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final com.github.jinahya.bit.io.miscellaneous.itu.GolombJPEGLS delegate;
}
