package com.github.jinahya.bit.io;

/**
 * A class of {@link StreamByteOutput} whose {@link StreamByteOutput#target} is an instance of {@link
 * BlackOutputStream}.
 *
 * @see WhiteStreamByteInput
 */
class BlackStreamByteOutput extends StreamByteOutput<BlackOutputStream> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    BlackStreamByteOutput() {
        super(new BlackOutputStream());
    }
}
