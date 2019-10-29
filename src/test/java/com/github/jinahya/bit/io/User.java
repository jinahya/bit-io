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

import lombok.Data;

import java.io.IOException;

import static com.github.jinahya.bit.io.ExtendedBitInput.readString;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeString;
import static java.nio.charset.StandardCharsets.UTF_8;

@Data
class User implements BitReadable, BitWritable {

    // -----------------------------------------------------------------------------------------------------------------
    private static final int SIZE_AGE = 7;

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void read(BitInput input) throws IOException {
        name = readString(true, input, UTF_8.name());
        age = input.readInt(true, SIZE_AGE);
    }

    @Override
    public void write(BitOutput output) throws IOException {
        writeString(true, output, name, UTF_8.name());
        output.writeInt(true, SIZE_AGE, age);
    }

    // -----------------------------------------------------------------------------------------------------------------
    String name;

    int age;
}
