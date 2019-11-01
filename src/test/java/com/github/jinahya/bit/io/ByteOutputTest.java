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
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * An abstract class for unit-testing subclasses of {@link ByteOutput} interface.
 *
 * @param <T> subclass type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteInputTest
 */
@Slf4j
abstract class ByteOutputTest<T extends ByteOutput> {

    // -----------------------------------------------------------------------------------------------------------------
    ByteOutputTest(final Class<T> byteOutputClass) {
        super();
        this.byteOutputClass = requireNonNull(byteOutputClass, "byteOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link ByteOutput#write(int)} with a random value.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testWrite() throws IOException {
        byteOutput().write(current().nextInt(0, 256));
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected T byteOutput() {
        return byteInputInstance.select(byteOutputClass).get();
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> byteOutputClass;

    @Inject
    private Instance<ByteOutput> byteInputInstance;
}
