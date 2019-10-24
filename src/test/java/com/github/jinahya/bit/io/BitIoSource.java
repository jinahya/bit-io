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
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * A class for sourcing {@link BitInput} and {@link BitOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
final class BitIoSource {

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceBitIoArray() {
        final Stream<Arguments> byteIo = ByteIoSource.sourceByteIoArray();
        return byteIo.map(a -> Arguments.of(new DefaultBitOutput((ByteOutput) a.get()[0]),
                                            new DefaultBitInput((ByteInput) a.get()[1])));
    }

    static Stream<Arguments> sourceBitIoBuffer() {
        final Stream<Arguments> byteIo = ByteIoSource.sourceByteIoBuffer();
        return byteIo.map(a -> Arguments.of(new DefaultBitOutput((ByteOutput) a.get()[0]),
                                            new DefaultBitInput((ByteInput) a.get()[1])));
    }

    static Stream<Arguments> sourceBitIoData() {
        final Stream<Arguments> byteIo = ByteIoSource.sourceByteIoData();
        return byteIo.map(a -> Arguments.of(new DefaultBitOutput((ByteOutput) a.get()[0]),
                                            new DefaultBitInput((ByteInput) a.get()[1])));
    }

    static Stream<Arguments> sourceBitIoStream() {
        final Stream<Arguments> byteIo = ByteIoSource.sourceByteIoStream();
        return byteIo.map(a -> Arguments.of(new DefaultBitOutput((ByteOutput) a.get()[0]),
                                            new DefaultBitInput((ByteInput) a.get()[1])));
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
