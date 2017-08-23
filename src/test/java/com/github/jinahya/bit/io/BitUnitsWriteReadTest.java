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
import java.util.ArrayList;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

abstract class BitUnitsWriteReadTest {

    @Test
    public void test() throws IOException {
        final BitUnit[] units = BitUnit.values();
        final List<Integer> ordinals = new ArrayList<>();
        final List<Object> params = new ArrayList<>();
        final List<Object> values = new ArrayList<>();
        final int count = 1048576 / 8;
        for (int i = 0; i < count; i++) {
            final BitUnit unit = units[current().nextInt(units.length)];
            ordinals.add(unit.ordinal());
            final Object value = unit.write(params, output);
            values.add(value);
        }
        output.align(1);
        for (int i = 0; i < count; i++) {
            final BitUnit unit = units[ordinals.get(i)];
            final Object value = unit.read(params, input);
            assertEquals(value, values.get(i));
        }
    }

    @Inject
    private BitOutput output;

    @Inject
    private BitInput input;
}
