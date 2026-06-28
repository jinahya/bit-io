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

import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.BitWriter;

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.MASK_CONTINUATION;
import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.MASK_PAYLOAD;
import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.MASK_SIGN;
import static com.github.jinahya.bit.io.miscellaneous.Leb128Constants.SIZE_PAYLOAD;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * A writer for LEB128-encoded {@code long} values.
 *
 * @see <a href="https://dwarfstd.org/dwarf5std.html">DWARF Version 5 Standard</a>
 * @see Leb128Reader
 */
public abstract class Leb128Writer
        implements BitWriter<Long> {

    private static final class Unsigned
            extends Leb128Writer {

        @Override
        void writeValue(final BitOutput output, long value) throws IOException {
            requireNonNullOutput(output);
            if (value < 0L) {
                throw new IllegalArgumentException("negative value: " + value);
            }
            do {
                int group = (int) (value & MASK_PAYLOAD);
                value >>>= SIZE_PAYLOAD;
                if (value != 0L) {
                    group |= MASK_CONTINUATION;
                }
                output.writeUnsignedInt(Byte.SIZE, group);
            } while (value != 0L);
        }
    }

    private static final class Signed
            extends Leb128Writer {

        @Override
        void writeValue(final BitOutput output, long value) throws IOException {
            requireNonNullOutput(output);
            while (true) {
                final int group = (int) (value & MASK_PAYLOAD);
                value >>= SIZE_PAYLOAD;
                final boolean clear = (group & MASK_SIGN) == 0;
                if ((value == 0L && clear) || (value == -1L && !clear)) {
                    output.writeUnsignedInt(Byte.SIZE, group);
                    return;
                }
                output.writeUnsignedInt(Byte.SIZE, group | MASK_CONTINUATION);
            }
        }
    }

    /**
     * A writer for unsigned LEB128 values representable as non-negative {@code long} values.
     */
    public static final Leb128Writer UNSIGNED = new Unsigned();

    /**
     * A writer for signed LEB128 values.
     */
    public static final Leb128Writer SIGNED = new Signed();

    // -----------------------------------------------------------------------------------------------------------------
    private Leb128Writer() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public final void write(final BitOutput output, final Long value) throws IOException {
        requireNonNullOutput(output);
        final long v = requireNonNullValue(value);
        writeValue(output, v);
    }

    abstract void writeValue(BitOutput output, long value) throws IOException;
}
