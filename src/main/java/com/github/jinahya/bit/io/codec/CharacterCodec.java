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
public class CharacterCodec extends BridgeBitCodec<Character, Integer> {


    public CharacterCodec(final boolean nullable) {

        super(nullable, new IntegerCodec(false, true, 16));
    }


    @Override
    protected Character convertFrom(final Integer u) {

        return (char) u.intValue();
    }


    @Override
    protected Integer convertTo(final Character t) {

        return (int) t;
    }

}

