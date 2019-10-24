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

import lombok.extern.slf4j.Slf4j;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * An abstract class for unit-testing subclasses of {@link ByteInput} interface.
 *
 * @param <T> subclass type paramter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@ExtendWith({WeldJunit5Extension.class})
@Slf4j
abstract class ByteInputTest<T extends ByteInput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Create s new instance with specified byte input class.
     *
     * @param byteInputClass the byte input class to test.
     */
    ByteInputTest(final Class<T> byteInputClass) {
        super();
        this.byteInputClass = requireNonNull(byteInputClass, "byteOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void selectByteInput() {
        byteInput = byteInputInstance.select(byteInputClass).get();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link ByteInput#read()} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testRead() throws IOException {
        final int octet = byteInput.read();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The type of {@link ByteInput} to test.
     */
    final Class<T> byteInputClass;

    @Typed
    @Inject
    private Instance<ByteInput> byteInputInstance;

    /**
     * An instance of {@link #byteInputClass} to test with.
     */
    transient T byteInput;
}
