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
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
abstract class ByteInputTest<T extends ByteInput> {


    public ByteInputTest(final Class<T> type) {

        super();

        this.type = requireNonNull(type);
    }


    abstract T instance(final int capacity);


    @Test
    public void writeUnsignedByte() throws IOException {

        final int capacity = current().nextInt(1024);
        final T output = instance(capacity);
        for (int i = 0; i < capacity; i++) {
            final int value = output.read();
            assertTrue(value >= 0x00 && value <= 0xFF);
        }
    }


    protected final Class<T> type;

}

