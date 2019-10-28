package com.github.jinahya.bit.io;

import java.io.IOException;

class UserBitWriter implements BitWriter<User> {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final BitOutput output, final User value) throws IOException {
        value.write(output);
    }
}
