/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */

package com.github.jinahya.bit.io.codec;


import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitIoConstraints;
import com.github.jinahya.bit.io.BitOutput;
import java.io.IOException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class StringCodec extends AbstractBitCodec<String> {


    public StringCodec(final boolean nullable, final int scale,
                       final String charsetName, final int size) {

        super(nullable);

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        this.scale = BitIoConstraints.requireValidUnsignedIntSize(scale);
        this.charsetName = charsetName;
        this.size = BitIoConstraints.requireValidUnsignedByteSize(size);
    }


    @Override
    protected String decodeValue(final BitInput input) throws IOException {

        final int length = input.readUnsignedInt(scale);

        final byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) input.readUnsignedInt(size);
        }

        return new String(bytes, charsetName);
    }


    @Override
    protected void encodeValue(final BitOutput output, final String value)
        throws IOException {

        final byte[] bytes = value.getBytes(charsetName);

        output.writeUnsignedInt(scale, bytes.length);

        for (final byte b : bytes) {
            output.writeUnsignedInt(size, b & 0xFF);
        }
    }


    private final int scale;


    private final String charsetName;


    private final int size;

}

