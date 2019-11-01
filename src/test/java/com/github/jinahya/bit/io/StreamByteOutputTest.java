package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.OutputStream;

/**
 * A class for unit-testing {@link StreamByteOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StreamByteInputTest
 */
@ExtendWith({MockitoExtension.class, WeldJunit5Extension.class})
class StreamByteOutputTest extends AbstractByteOutputTest<StreamByteOutput, OutputStream> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    StreamByteOutputTest() {
        super(StreamByteOutput.class, OutputStream.class);
    }
}
