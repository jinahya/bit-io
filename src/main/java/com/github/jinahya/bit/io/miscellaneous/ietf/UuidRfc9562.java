package com.github.jinahya.bit.io.miscellaneous.ietf;

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
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

/**
 * A codec for RFC 9562 UUID values as {@link UUID}s, in canonical 16-octet byte order; the {@link UUID} view of
 * {@link UuidRfc9562Bytes}, with which it shares the wire format.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9562.html">RFC 9562: Universally Unique IDentifiers</a>
 * @see UuidRfc9562Bytes
 */
public final class UuidRfc9562
        implements BitReader<UUID>, BitWriter<UUID> {

    /**
     * The singleton instance of this codec.
     */
    public static final UuidRfc9562 INSTANCE = new UuidRfc9562();

    /**
     * Returns the {@code UUID} of specified 16 octets in RFC 9562 canonical byte order.
     *
     * @param bytes the octets.
     * @return the {@code UUID} of {@code bytes}.
     */
    static UUID fromBytes(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(0), buffer.getLong(Long.BYTES));
    }

    /**
     * Returns the 16 octets, in RFC 9562 canonical byte order, of specified {@code UUID}.
     *
     * @param value the {@code UUID}.
     * @return the octets of {@code value}.
     */
    static byte[] toBytes(final UUID value) {
        return ByteBuffer.allocate(UuidRfc9562Bytes.LENGTH)
                .putLong(value.getMostSignificantBits())
                .putLong(value.getLeastSignificantBits())
                .array();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private UuidRfc9562() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public UUID read(final BitInput input) throws IOException {
        return fromBytes(UuidRfc9562Bytes.INSTANCE.read(input));
    }

    @Override
    public void write(final BitOutput output, final UUID value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        UuidRfc9562Bytes.INSTANCE.write(output, toBytes(value));
    }
}
