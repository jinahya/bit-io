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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T>
 */
public class SerializableCodec<T extends Serializable>
    extends AbstractBitCodec<T> {


    public SerializableCodec(final boolean nullable) {

        super(nullable);
    }


    @Override
    @SuppressWarnings("unchecked")
    protected T decodeValue(final BitInput input) throws IOException {

        try {
            return (T) new ObjectInputStream(new InputStream() {

                @Override
                public int read() throws IOException {

                    return input.readUnsignedInt(8);
                }

            }).readObject();
        } catch (final ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }


    @Override
    protected void encodeValue(final BitOutput output, final T value)
        throws IOException {

        new ObjectOutputStream(new OutputStream() {

            @Override
            public void write(final int b) throws IOException {

                output.writeUnsignedInt(8, b);
            }

        }).writeObject(value);
    }

}

