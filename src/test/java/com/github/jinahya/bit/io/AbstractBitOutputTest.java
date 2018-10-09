/*
 * Copyright 2017 Jin Kwon &gt;onacit@gmail.com&lt;.
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

import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.mock;

public class AbstractBitOutputTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void alignWithZeroBytes() throws IOException {
        final BitOutput mock
                = mock(AbstractBitOutput.class, Mockito.CALLS_REAL_METHODS);
        mock.align(0);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void alignWithNegativeBytes() throws IOException {
        final BitOutput mock
                = mock(AbstractBitOutput.class, Mockito.CALLS_REAL_METHODS);
        mock.align(ThreadLocalRandom.current().nextInt() | Integer.MIN_VALUE);
    }
}
