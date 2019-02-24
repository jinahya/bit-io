package com.github.jinahya.bit.io;

/**
 * A class of {@link DefaultBitOutput} whose {@link DefaultBitOutput#delegate} is an instance of {@link
 * BlackByteOutput}.
 */
class BlackBitOutput extends DefaultBitOutput<BlackByteOutput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    BlackBitOutput() {
        super(new BlackByteOutput());
    }
}
