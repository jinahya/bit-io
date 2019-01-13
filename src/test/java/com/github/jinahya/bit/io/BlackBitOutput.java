package com.github.jinahya.bit.io;

public class BlackBitOutput extends DefaultBitOutput<BlackByteOutput> {

    public BlackBitOutput() {
        super(new BlackByteOutput());
    }
}
