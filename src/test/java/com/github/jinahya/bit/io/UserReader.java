package com.github.jinahya.bit.io;

import java.io.IOException;

class UserReader implements BitReader<User> {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public User read(final BitInput input) throws IOException {
        final User value = new User();
        value.read(input);
        return value;
    }
}
