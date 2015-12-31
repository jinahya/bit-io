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


import com.github.jinahya.bit.io.bind.annotation.BitProperty;
import com.github.jinahya.bit.io.bind.annotation.BitType;
import java.io.IOException;
import static java.util.concurrent.ThreadLocalRandom.current;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@BitType(order = {"age", "married"})
public class Employee implements BitReadable, BitWritable {


    public static Employee newRandomInstance() {

        final Employee instance = new Employee();

        instance.age = current().nextInt(128);
        instance.married = current().nextBoolean();

        return instance;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        if (this.age != other.age) {
            return false;
        }
        if (this.married != other.married) {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.age;
        hash = 17 * hash + (this.married ? 1 : 0);
        return hash;
    }


    @Override
    public void read(final BitInput input) throws IOException {

        setAge(input.readInt(true, 7));
        setMerried(input.readBoolean());
    }


    @Override
    public void write(final BitOutput output) throws IOException {

        output.writeInt(true, 7, getAge());
        output.writeBoolean(isMarried());
    }


    public int getAge() {

        return age;
    }


    public void setAge(final int age) {

        this.age = age;
    }


    public Employee age(final int age) {

        setAge(age);

        return this;
    }


    public boolean isMarried() {

        return married;
    }


    public void setMerried(final boolean married) {

        this.married = married;
    }


    public Employee married(final boolean married) {

        setMerried(married);

        return this;
    }


    @BitProperty(size = 7, unsigned = true)
    private int age;


    private boolean married;

}

