package com.github.jinahya.bit.io;

import java.io.DataOutputStream;

class BlackDataByteOutput extends DataByteOutput<DataOutputStream> {

    // -----------------------------------------------------------------------------------------------------------------
    BlackDataByteOutput() {
        super(new DataOutputStream(new BlackOutputStream()));
    }
}
