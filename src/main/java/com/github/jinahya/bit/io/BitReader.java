package com.github.jinahya.bit.io;

import java.io.IOException;

interface BitReader<T> {

    // -----------------------------------------------------------------------------------------------------------------
    T read(BitInput input) throws IOException;
}
