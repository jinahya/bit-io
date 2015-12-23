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

package com.github.jinahya.bit.io;


import com.github.jinahya.bit.io.codec.AbstractBitCodec;
import java.io.IOException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PersonCodec extends AbstractBitCodec<Person> {


    public PersonCodec(final boolean nullable) {

        super(nullable);
    }


    @Override
    protected Person decodeValue(final BitInput input) throws IOException {

        // no need to check nullability
        return new Person()
            .age(input.readUnsignedInt(7))
            .married(input.readBoolean());
    }


    @Override
    protected void encodeValue(final BitOutput otuput, final Person value)
        throws IOException {

        // no need to check nullability
        otuput.writeUnsignedInt(7, value.getAge());
        otuput.writeBoolean(value.isMarried());
    }

}

