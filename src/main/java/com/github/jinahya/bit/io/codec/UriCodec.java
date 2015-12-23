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
import com.github.jinahya.bit.io.BitOutput;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class UriCodec extends AbstractBitCodec<URI> {


    public UriCodec(final boolean nullable, final int scale) {

        super(nullable);

        codec = new StringCodec(nullable, scale, "US-ASCII", 7);
    }


    @Override
    protected URI decodeValue(final BitInput input) throws IOException {

        try {
            return new URI(codec.decodeValue(input));
        } catch (final URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
    }


    @Override
    protected void encodeValue(final BitOutput output, final URI value)
        throws IOException {

        codec.encodeValue(output, value.toASCIIString());
    }


    private final StringCodec codec;

}

