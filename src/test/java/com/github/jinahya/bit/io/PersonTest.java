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


import java.io.IOException;
import javax.inject.Inject;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = {WhiteBitInputModule.class, BlackBitOutputModule.class})
public class PersonTest {


    @Test
    public void decodeAsBitDecodable() throws IOException {

        Person.newRandomInstance().decode(input);
    }


    @Test
    public void encodeAsBitEncodable() throws IOException {

        Person.newRandomInstance().encode(output);
    }


    @Test
    public void decodeWithBitDecoder() throws IOException {

        new PersonDecoder().decode(input);
    }


    @Test
    public void encodeWithBitEncoder() throws IOException {

        new PersonEncoder().encode(output, Person.newRandomInstance());
    }


    @Test
    public void readObjectWithDecoder() throws IOException {

        input.readObject(new PersonDecoder());
    }


    @Test
    public void writeObjectWithEncoder() throws IOException {

        output.writeObject(Person.newRandomInstance(), new PersonEncoder());
    }


    @Test
    public void readObjectWithLambda() throws IOException {

        final Person person = input.readObject(i -> {
            return new Person()
                .age(i.readUnsignedInt(7))
                .merried(i.readBoolean());
        });
    }


    @Test
    public void writeObjectWithLambda() throws IOException {

        output.writeObject(Person.newRandomInstance(), (o, v) -> {
                           o.writeUnsignedInt(7, v.getAge());
                           o.writeBoolean(v.isMerried());
                       });
    }


    @Inject
    private BitInput input;


    @Inject
    private BitOutput output;

}

