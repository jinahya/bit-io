package com.github.jinahya.bit.io;

import java.io.IOException;

public interface BitWritable<T extends BitWritable<T>> {

    // -----------------------------------------------------------------------------------------------------------------
    T write(BitOutput bitOutput) throws IOException;
}
