package com.github.jinahya.bit.io;

import java.io.IOException;

interface BitWriter<T> {

    void write(T value, BitOutput output) throws IOException;
}
