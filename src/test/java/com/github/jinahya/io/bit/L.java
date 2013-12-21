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


package com.github.jinahya.io.bit;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class L {


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.lIntUnsigned;
        hash = 13 * hash + this.lInt;
        hash = 13 * hash + this.lLongUnsigned;
        hash = 13 * hash + this.lLong;
        hash = 13 * hash + this.sBytes;
        hash = 13 * hash + this.rByhtes;
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
        final L other = (L) obj;
        if (this.lIntUnsigned != other.lIntUnsigned) {
            return false;
        }
        if (this.lInt != other.lInt) {
            return false;
        }
        if (this.lLongUnsigned != other.lLongUnsigned) {
            return false;
        }
        if (this.lLong != other.lLong) {
            return false;
        }
        if (this.sBytes != other.sBytes) {
            return false;
        }
        if (this.rByhtes != other.rByhtes) {
            return false;
        }
        return true;
    }


    final int lIntUnsigned = BitIoTests.lengthIntUnsigned();


    final int lInt = BitIoTests.lengthInt();


    final int lLongUnsigned = BitIoTests.lengthLongUnsigned();


    final int lLong = BitIoTests.lengthLong();


    final int sBytes = BitIoTests.scaleBytes();


    final int rByhtes = BitIoTests.rangeBytes();


}

