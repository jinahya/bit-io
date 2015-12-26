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


import java.io.UnsupportedEncodingException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class StringCodec extends BridgeCodec<String, byte[]> {


    public StringCodec(final boolean nullable, final int scale,
                       final boolean unsigned, final int size,
                       final String charsetName) {

        super(new ByteArrayCodec(nullable, scale, unsigned, size));

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        this.charsetName = charsetName;
    }


    @Override
    protected String convertFrom(final byte[] u) {

        try {
            return new String(u, charsetName);
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }


    @Override
    protected byte[] convertTo(final String t) {

        try {
            return t.getBytes(charsetName);
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }


    protected final String charsetName;

}

