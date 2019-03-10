package com.github.jinahya.bit.io;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExtendedBitIoTests {

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R applyRandomAscii(final int lengthSize, final Function<String, R> function) {
        BitIoConstraints.requireValidSizeInt(true, lengthSize);
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final int length = current().nextInt(0, ((1 << lengthSize)));
        final byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) current().nextInt(0, 128);
        }
        return function.apply(new String(bytes, UTF_8));
    }

    static void acceptRandomAscii(final int lengthSize, final Consumer<String> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(applyRandomAscii(lengthSize, v -> {
            consumer.accept(v);
            return null;
        }));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ExtendedBitIoTests() {
        super();
    }
}
