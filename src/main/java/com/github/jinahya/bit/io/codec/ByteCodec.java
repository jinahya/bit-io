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


import com.github.jinahya.bit.io.BitIoConstraints;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ByteCodec extends BridgeBitCodec<Byte, Integer> {


    public ByteCodec(final boolean nullable, final boolean unsigned,
                     final int size) {

        super(nullable, new IntegerCodec(
              false, unsigned,
              BitIoConstraints.requireValidIntSize(unsigned, 3, size)));
    }


    @Override
    protected Byte convertFrom(final Integer u) {

        return u.byteValue();
    }


    @Override
    protected Integer convertTo(final Byte t) {

        return t.intValue();
    }

}

