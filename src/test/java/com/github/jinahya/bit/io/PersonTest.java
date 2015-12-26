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
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PersonTest {


    private static final Logger logger = getLogger(PersonTest.class);


    @Test
    public void test() throws IOException {

        final int count = current().nextInt(1024, 2048);
        final List<Person> people;
        {
            final List<Person> list = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                list.add(current().nextBoolean()
                         ? Person.newRandomInstance() : null);
            }
            people = Collections.unmodifiableList(list);
        }

        BitIoTest.all(
            o -> {
                for (final Person person : people) {
                    try {
                        o.writeObject(true, person);
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                }
            },
            i -> {
                for (final Person expected : people) {
                    try {
                        final Person actual = i.readObject(true, Person.class);
                        assertEquals(actual, expected);
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                }
            });
    }

}

