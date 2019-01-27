package com.github.jinahya.bit.io;

import java.io.DataInputStream;

class WhiteDataByteInput extends DataByteInput<DataInputStream> {

    // -----------------------------------------------------------------------------------------------------------------
    WhiteDataByteInput() {
        super(new DataInputStream(new WhiteInputStream()));
    }
}
