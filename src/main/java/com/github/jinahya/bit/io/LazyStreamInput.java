/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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
import java.io.InputStream;
import java.util.function.Supplier;


/**
 * A {@link ByteInput} implementation for {@link InputStream}s.
 */
public class LazyStreamInput extends StreamInput {


    /**
     * Creates a new instance built on top of the specified input stream.
     *
     * @param supplier {@inheritDoc}
     */
    public LazyStreamInput(final Supplier<? extends InputStream> supplier) {

        super(null);

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        this.supplier = supplier;
    }


    @Override
    public int readUnsignedByte() throws IOException {

        if (source == null) {
            source = supplier.get();
        }

        return super.readUnsignedByte();
    }


    private final Supplier<? extends InputStream> supplier;


}

