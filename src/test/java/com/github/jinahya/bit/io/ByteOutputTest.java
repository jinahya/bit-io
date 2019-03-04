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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static java.util.concurrent.ThreadLocalRandom.current;

@ExtendWith({WeldJunit5Extension.class})
public abstract class ByteOutputTest<T extends ByteOutput> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    public ByteOutputTest(final Class<T> byteOutputClass) {
        super();
        this.byteOutputClass = Objects.requireNonNull(byteOutputClass, "byteOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void selectByteInput() {
        byteOutput = byteInputInstance.select(byteOutputClass).get();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link ByteOutput#write(int)} with a random value.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWrite() throws IOException {
        byteOutput.write(current().nextInt(0, 256));
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> byteOutputClass;

    @Typed
    @Inject
    private Instance<ByteOutput> byteInputInstance;

    T byteOutput;
}
