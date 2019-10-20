package com.github.jinahya.bit.io;

import java.io.IOException;

public interface BitWritable {

    void write(BitOutput bitOutput) throws IOException;
}
