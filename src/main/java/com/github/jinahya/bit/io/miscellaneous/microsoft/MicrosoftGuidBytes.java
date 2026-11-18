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
import com.github.jinahya.bit.io.miscellaneous.ietf.UuidRfc9562Bytes;

import java.io.IOException;

/**
 * A codec for Microsoft GUID values in their 16-octet packet representation: {@code Data1} (4 octets), {@code Data2}
 * (2 octets), and {@code Data3} (2 octets) in little-endian byte order, followed by the 8 single-octet elements of
 * {@code Data4} in order.
 *
 * <p>Values read and written are in RFC 9562 canonical byte order, as {@link UuidRfc9562Bytes} reads and writes
 * them; this codec reverses the bytes of the first three fields at the I/O boundary. For the UUID
 * {@code 01234567-89ab-cdef-0123-456789abcdef}, the canonical bytes are {@code 0123456789abcdef0123456789abcdef} and
 * the wire bytes are {@code 67452301ab89efcd0123456789abcdef}.</p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-dtyp/001eec5a-7f8b-4293-9e21-ca349392db40">
 * [MS-DTYP] 2.3.4.2 GUID--Packet Representation</a>
 * @see UuidRfc9562Bytes
 */
public final class MicrosoftGuidBytes
        implements BitReader<byte[]>, BitWriter<byte[]> {

    /**
     * The number of octets of a Microsoft GUID value. The value is {@value}.
     */
    public static final int LENGTH = UuidRfc9562Bytes.LENGTH;

    /**
     * The singleton instance of this codec.
     */
    public static final MicrosoftGuidBytes INSTANCE = new MicrosoftGuidBytes();

    /**
     * Reverses the bytes of {@code Data1}, {@code Data2}, and {@code Data3} of specified value, in place.
     *
     * @param bytes the value.
     */
    private static void reverseFields(final byte[] bytes) {
        reverse(bytes, 0, 4); // Data1
        reverse(bytes, 4, 2); // Data2
        reverse(bytes, 6, 2); // Data3
    }

    private static void reverse(final byte[] bytes, final int offset, final int length) {
        for (int i = 0; i < length / 2; i++) {
            final int j = length - 1 - i;
            final byte b = bytes[offset + i];
            bytes[offset + i] = bytes[offset + j];
            bytes[offset + j] = b;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private MicrosoftGuidBytes() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public byte[] read(final BitInput input) throws IOException {
        final byte[] value = UuidRfc9562Bytes.INSTANCE.read(input);
        reverseFields(value);
        return value;
    }

    @Override
    public void write(final BitOutput output, final byte[] value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (value.length != LENGTH) {
            throw new IllegalArgumentException("value.length(" + value.length + ") != " + LENGTH);
        }
        final byte[] wire = value.clone(); // the caller's array is left unchanged
        reverseFields(wire);
        UuidRfc9562Bytes.INSTANCE.write(output, wire);
    }
}
