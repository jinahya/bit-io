package com.github.jinahya.bit.io;

import java.io.DataOutputStream;

/**
 * A class for testing {@link BlackDataByteOutput}.
 */
class BlackDataByteOutputTest extends DataByteOutputTest<BlackDataByteOutput, DataOutputStream> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    BlackDataByteOutputTest() {
        super(BlackDataByteOutput.class, DataOutputStream.class);
    }
}
