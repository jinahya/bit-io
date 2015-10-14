/*
 * Copyright 2014 Jin Kwon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.bit.io;


import java.io.IOException;
import javax.inject.Inject;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon
 * @param <T> byte input type parameter
 */
public abstract class ByteInputTest<T extends ByteInput> {


    @Test
    public void readUnsignedByte_() throws IOException {

        final int actual = input.readUnsignedByte();

        if (actual == -1) {
            return;
        }

        Assert.assertTrue(actual >= 0);
        Assert.assertTrue(actual < 256);
    }


    @Inject
    protected T input;


}

