package com.github.jinahya.bit.io;

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
