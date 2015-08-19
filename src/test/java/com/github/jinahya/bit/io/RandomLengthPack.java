/*
 * Copyright 2013 Jin Kwon <jinahya at gmail.com>.
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


/**
 *
 * @author <a href="mailto:jinahya@gmail.com">Jin Kwon</a>
 */
public class RandomLengthPack {


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + lengthIntUnsigned;
        hash = 13 * hash + lengthInt;
        hash = 13 * hash + lengthLongUnsigned;
        hash = 13 * hash + lengthLong;
        hash = 13 * hash + scaleBytes;
        hash = 13 * hash + rangeBytes;
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RandomLengthPack that = (RandomLengthPack) obj;
        if (lengthIntUnsigned != that.lengthIntUnsigned) {
            return false;
        }
        if (lengthInt != that.lengthInt) {
            return false;
        }
        if (lengthLongUnsigned != that.lengthLongUnsigned) {
            return false;
        }
        if (lengthLong != that.lengthLong) {
            return false;
        }
        if (scaleBytes != that.scaleBytes) {
            return false;
        }
        if (rangeBytes != that.rangeBytes) {
            return false;
        }
        return true;
    }


    final int lengthIntUnsigned = BitIoRandoms.lengthIntUnsigned();


    final int lengthInt = BitIoRandoms.lengthInt();


    final int lengthLongUnsigned = BitIoRandoms.lengthLongUnsigned();


    final int lengthLong = BitIoRandoms.lengthLong();


    final int scaleBytes = BitIoRandoms.scaleBytes();


    final int rangeBytes = BitIoRandoms.rangeBytes();


}

