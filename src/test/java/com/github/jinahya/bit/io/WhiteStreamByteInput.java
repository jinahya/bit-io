package com.github.jinahya.bit.io;

class WhiteStreamByteInput extends StreamByteInput<WhiteInputStream> {

    // -----------------------------------------------------------------------------------------------------------------
    WhiteStreamByteInput() {
        super(new WhiteInputStream());
    }
}
