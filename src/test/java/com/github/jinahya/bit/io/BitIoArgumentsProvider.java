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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.function.Supplier;
import java.util.stream.Stream;

class BitIoArgumentsProvider implements ArgumentsProvider {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    private static final int BYTE_LENGTH = 1048576;

    // -----------------------------------------------------------------------------------------------------------------
    static Arguments array() {
        final byte[] array = new byte[BYTE_LENGTH];
        final ByteOutput target = new ArrayByteOutput(array);
        final BitOutput output = new DefaultBitOutput<>(target);
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteInput source = new ArrayByteInput(array);
            return new DefaultBitInput<>(source);
        };
        return Arguments.of(output, inputSupplier);
    }

    static Arguments buffer() {
        final ByteBuffer buffer = ByteBuffer.allocate(BYTE_LENGTH);
        final ByteOutput delegate = new BufferByteOutput<>(buffer);
        final BitOutput output = new DefaultBitOutput<>(delegate);
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteInput source = new BufferByteInput<>((ByteBuffer) buffer.flip());
            return new DefaultBitInput<>(source);
        };
        return Arguments.of(output, inputSupplier);
    }

    static Arguments data() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(BYTE_LENGTH);
        final DataOutput target = new DataOutputStream(baos);
        final BitOutput output = new DefaultBitOutput<>(new DataByteOutput<DataOutput>(target));
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final DataInput source = new DataInputStream(bais);
            return new DefaultBitInput<ByteInput>(new DataByteInput<>(source));
        };
        return Arguments.of(output, inputSupplier);
    }

    static Arguments stream() {
        final ByteArrayOutputStream target = new ByteArrayOutputStream(BYTE_LENGTH);
        final BitOutput output = new DefaultBitOutput<ByteOutput>(new StreamByteOutput<>(target));
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteArrayInputStream source = new ByteArrayInputStream(target.toByteArray());
            return new DefaultBitInput<ByteInput>(new StreamByteInput<>(source));
        };
        return Arguments.of(output, inputSupplier);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Object[] source() {
        return new Object[] {
                array(),
                buffer(),
                data(),
                stream()
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) {
        return Stream.<Arguments>builder()
                .add(array())
                .add(buffer())
                .add(data())
                .add(stream())
                .build();
    }
}
