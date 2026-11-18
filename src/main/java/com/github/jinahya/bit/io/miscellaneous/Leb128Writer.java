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
import com.github.jinahya.bit.io.LongBitWriter;

import java.io.IOException;

/**
 * A deprecated alias of {@link com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer}, which this class
 * delegates to.
 *
 * @deprecated Use {@link com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer} instead;
 *             this class will be removed in the next major version.
 */
@Deprecated
public abstract class Leb128Writer
        implements LongBitWriter {

    private static final class Delegating
            extends Leb128Writer {

        private Delegating(final com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer delegate) {
            super();
            this.delegate = delegate;
        }

        @Override
        void writeValue(final BitOutput output, final long value) throws IOException {
            delegate.writeLong(output, value);
        }

        private final com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer delegate;
    }

    /**
     * An instance for unsigned LEB128 values.
     */
    public static final Leb128Writer UNSIGNED =
            new Delegating(com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer.UNSIGNED);

    /**
     * An instance for signed LEB128 values.
     */
    public static final Leb128Writer SIGNED =
            new Delegating(com.github.jinahya.bit.io.miscellaneous.w3c.Leb128Writer.SIGNED);

    // -----------------------------------------------------------------------------------------------------------------
    private Leb128Writer() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public final void writeLong(final BitOutput output, final long value) throws IOException {
        writeValue(output, value);
    }

    abstract void writeValue(final BitOutput output, final long value) throws IOException;
}
