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


import java.net.MalformedURLException;
import java.net.URL;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class UrlCodec extends BridgeCodec<URL, String> {


    public UrlCodec(final boolean nullable, final int scale) {

        super(new StringCodec(nullable, scale, true, 8, "UTF-8"));
    }


    @Override
    protected URL convertFrom(final String u) {

        try {
            return new URL(u);
        } catch (final MalformedURLException murle) {
            throw new RuntimeException(murle);
        }
    }


    @Override
    protected String convertTo(final URL t) {

        return t.toString();
    }

}

