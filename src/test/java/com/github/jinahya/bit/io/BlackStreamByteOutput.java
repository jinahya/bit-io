package com.github.jinahya.bit.io;

class BlackStreamByteOutput extends StreamByteOutput<BlackOutputStream> {

    // -----------------------------------------------------------------------------------------------------------------
    BlackStreamByteOutput() {
        super(new BlackOutputStream());
    }
}
