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


package com.github.jinahya.io.bit;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author Jin Kwon
 */
public class ByteTargetProvider {


    public Iterator<Object[]> provide() {

        final List<Object[]> targets = new ArrayList<>();

        targets.add(new Object[]{
            new BufferOutput(ByteBuffer.allocate(1024))
        });

        targets.add(new Object[]{new BufferOutput(null) {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (target == null) {
                    target = ByteBuffer.allocate(1024);
                }

                super.writeUnsignedByte(value);
            }


        }});

        return targets.iterator();

    }


}

