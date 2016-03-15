/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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

import java.io.InputStream;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayByteInputTest extends ByteInputTest<ArrayByteInput> {

    @Test
    public static void newInstance() {
        assertThrows(NullPointerException.class,
                     () -> ArrayByteInput.newInstance(null, 1));
        assertThrows(IllegalArgumentException.class,
                     () -> ArrayByteInput.newInstance(
                             mock(InputStream.class), 0));
        assertThrows(IllegalArgumentException.class,
                     () -> ArrayByteInput.newInstance(
                             mock(InputStream.class), -1));
    }
}
