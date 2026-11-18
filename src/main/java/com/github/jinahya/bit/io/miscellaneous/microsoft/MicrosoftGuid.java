package com.github.jinahya.bit.io.miscellaneous.microsoft;

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
 * A codec for Microsoft GUID values as {@link UUID}s, in the 16-octet packet representation; the {@link UUID} view of
 * {@link MicrosoftGuidBytes}, with which it shares the wire format.
 *
 * <p>For the GUID {@code 01234567-89ab-cdef-0123-456789abcdef}, the wire bytes are
 * {@code 67452301ab89efcd0123456789abcdef}.</p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-dtyp/001eec5a-7f8b-4293-9e21-ca349392db40">
 * [MS-DTYP] 2.3.4.2 GUID--Packet Representation</a>
 * @see MicrosoftGuidBytes
 */
public final class MicrosoftGuid
        implements BitReader<UUID>, BitWriter<UUID> {

    /**
     * The singleton instance of this codec.
     */
    public static final MicrosoftGuid INSTANCE = new MicrosoftGuid();

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
        return ByteBuffer.allocate(MicrosoftGuidBytes.LENGTH)
                .putLong(value.getMostSignificantBits())
                .putLong(value.getLeastSignificantBits())
                .array();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private MicrosoftGuid() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public UUID read(final BitInput input) throws IOException {
        return fromBytes(MicrosoftGuidBytes.INSTANCE.read(input));
    }

    @Override
    public void write(final BitOutput output, final UUID value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        MicrosoftGuidBytes.INSTANCE.write(output, toBytes(value));
    }
}
