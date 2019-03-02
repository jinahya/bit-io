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

@ExtendWith({WeldJunit5Extension.class})
public abstract class ByteInputTest<T extends ByteInput> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    public ByteInputTest(final Class<T> byteInputClass) {
        super();
        this.byteInputClass = Objects.requireNonNull(byteInputClass, "byteOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    //@BeforeAll // seems not work with PER_CLASS
    void selectByteInput() {
        byteInput = byteInputInstance.select(byteInputClass).get();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testRead() throws IOException {
        final int octet = byteInput.read();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The type of {@link ByteInput} to test.
     */
    protected final Class<T> byteInputClass;

    @Typed
    @Inject
    private Instance<ByteInput> byteInputInstance;

    /**
     * An instance of {@link #byteInputClass} to test with.
     */
    protected T byteInput;
}
