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
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.DefaultArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.github.jinahya.bit.io.ByteIoSource.sourceByteIoArray;
import static com.github.jinahya.bit.io.ByteIoSource.sourceByteIoBuffer;
import static com.github.jinahya.bit.io.ByteIoSource.sourceByteIoData;
import static com.github.jinahya.bit.io.ByteIoSource.sourceByteIoStream;

/**
 * A class for sourcing {@link BitInput} and {@link BitOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
final class BitIoSource {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Sources arguments of {@link BitOutput} and {@link BitInput}.
     *
     * @return a stream of arguments
     * @see ByteIoSource#sourceByteIoArray()
     */
    static Stream<Arguments> sourceBitIoArray() {
        return sourceByteIoArray().map(a -> {
            final ArgumentsAccessor accessor = new DefaultArgumentsAccessor(a.get());
            return Arguments.of(new DefaultBitOutput(accessor.get(0, ByteOutput.class)),
                                new DefaultBitInput(accessor.get(1, ByteInput.class)));
        });
    }

    static Stream<Arguments> sourceBitIoBuffer() {
        return sourceByteIoBuffer().map(a -> {
            final ArgumentsAccessor accessor = new DefaultArgumentsAccessor(a.get());
            return Arguments.of(new DefaultBitOutput(accessor.get(0, ByteOutput.class)),
                                new DefaultBitInput(accessor.get(1, ByteInput.class)));
        });
    }

    static Stream<Arguments> sourceBitIoData() {
        return sourceByteIoData().map(a -> {
            final ArgumentsAccessor accessor = new DefaultArgumentsAccessor(a.get());
            return Arguments.of(new DefaultBitOutput(accessor.get(0, ByteOutput.class)),
                                new DefaultBitInput(accessor.get(1, ByteInput.class)));
        });
    }

    static Stream<Arguments> sourceBitIoStream() {
        return sourceByteIoStream().map(a -> {
            final ArgumentsAccessor accessor = new DefaultArgumentsAccessor(a.get());
            return Arguments.of(new DefaultBitOutput(accessor.get(0, ByteOutput.class)),
                                new DefaultBitInput(accessor.get(1, ByteInput.class)));
        });
    }

    static Stream<Arguments> sourceBitIo() {
        return Stream.of(sourceBitIoArray(), sourceBitIoBuffer(), sourceBitIoData(), sourceBitIoStream())
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoSource() {
        super();
    }
}
