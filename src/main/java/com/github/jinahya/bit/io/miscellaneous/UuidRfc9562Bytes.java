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
 * A codec for RFC 9562 UUID values in canonical 16-octet byte order.
 *
 * <p>This codec intentionally uses {@code byte[]} rather than {@code java.util.UUID}, so it remains usable after
 * retro-translation to runtimes where {@code java.util.UUID} is unavailable.</p>
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9562.html">RFC 9562: Universally Unique IDentifiers</a>
 */
public final class UuidRfc9562Bytes
        implements BitReader<byte[]>, BitWriter<byte[]> {

    /**
     * The number of octets in an RFC 9562 UUID value.
     */
    public static final int LENGTH = 16;

    /**
     * The singleton instance of this codec.
     */
    public static final UuidRfc9562Bytes INSTANCE = new UuidRfc9562Bytes();

    // -----------------------------------------------------------------------------------------------------------------
    private UuidRfc9562Bytes() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public byte[] read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        final byte[] value = new byte[LENGTH];
        for (int i = 0; i < value.length; i++) {
            value[i] = input.readByte8();
        }
        return value;
    }

    @Override
    public void write(final BitOutput output, final byte[] value) throws IOException {
        requireNonNullOutput(output);
        requireNonNullValue(value);
        if (value.length != LENGTH) {
            throw new IllegalArgumentException("value.length(" + value.length + ") != " + LENGTH);
        }
        for (int i = 0; i < value.length; i++) {
            output.writeByte8(value[i]);
        }
    }
}
