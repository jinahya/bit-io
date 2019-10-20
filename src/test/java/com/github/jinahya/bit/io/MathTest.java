package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.StrictMath.ulp;

@Slf4j
class MathTest {

    @Test
    void log2() {
        MathTest.log.debug("log(0.0d) / log(2): {}", log(0.0d) / log(2.0) + 1e-10);
        MathTest.log.debug("log(0.0d) / log(2): {}", ceil(log(0.0d) / log(2.0) + 1e-10));
        MathTest.log.debug("log(1.0d) / log(2): {}", ceil(log(1.0d) / log(2.0)));
        MathTest.log.debug("log(1.0d) / log(2): {}", ceil(log(1.0d) / log(2.0) + ulp(1.0d)));
        MathTest.log.debug("log(Integer.MAX_VALUE) / log(2): {}", log(Integer.MAX_VALUE) / log(2.0) + ulp(1.0d));
        MathTest.log.debug("log(Integer.MAX_VALUE) / log(2): {}", ceil(log(Integer.MAX_VALUE) / log(2.0) + ulp(1.0d)));
    }
}
