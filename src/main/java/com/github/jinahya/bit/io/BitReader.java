package com.github.jinahya.bit.io;

import java.io.IOException;

interface BitReader<T> {

    void read(T value, BitInput input) throws IOException;
}
