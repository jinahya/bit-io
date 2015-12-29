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


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class DoubleCodec extends BridgeCodec<Double, Long> {


    public DoubleCodec(final boolean nullable) {

        super(new LongCodec(nullable, false, 64));
    }


    @Override
    protected Double convertFrom(final Long u) {

        return Double.longBitsToDouble(u);
    }


    @Override
    protected Long convertTo(final Double t) {

        return Double.doubleToRawLongBits(t);
    }


}

