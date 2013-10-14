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


package com.github.jinahya.io;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.Assert;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class RandomEntity implements Serializable {


    public RandomEntity() {

        super();

        final Random random = ThreadLocalRandom.current();

        bv = new boolean[random.nextInt(128)];
        for (int i = 0; i < bv.length; i++) {
            bv[i] = random.nextBoolean();
        }

        uil = new int[random.nextInt(128)];
        uiv = new int[uil.length];
        for (int i = 0; i < uiv.length; i++) {
            uil[i] = RandomLengths.newLengthIntUnsigned();
            uiv[i] = BitIoTests.newValueIntUnsigned(uil[i]);
        }

        sil = new int[random.nextInt(128)];
        siv = new int[sil.length];
        for (int i = 0; i < siv.length; i++) {
            sil[i] = RandomLengths.newLengthInt();
            siv[i] = BitIoTests.newValueInt(sil[i]);
        }

        ull = new int[random.nextInt(128)];
        ulv = new long[ull.length];
        for (int i = 0; i < ulv.length; i++) {
            ull[i] = RandomLengths.newLengthLongUnsigned();
            ulv[i] = BitIoTests.newValueLongUnsigned(ull[i]);
        }

        sll = new int[random.nextInt(128)];
        slv = new long[sll.length];
        for (int i = 0; i < slv.length; i++) {
            sll[i] = RandomLengths.newLengthLong();
            slv[i] = BitIoTests.newValueLong(sll[i]);
        }

        bas = new int[random.nextInt(32)];
        bar = new int[bas.length];
        bav = new byte[bar.length][];
        for (int i = 0; i < bav.length; i++) {
            bas[i] = RandomLengths.newScaleBytes();
            bar[i] = RandomLengths.newRangeBytes();
            bav[i] = BitIoTests.newValueBytes(bas[i], bar[i]);
        }
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Arrays.hashCode(this.bv);
        hash = 53 * hash + Arrays.hashCode(this.uiv);
        hash = 53 * hash + Arrays.hashCode(this.siv);
        hash = 53 * hash + Arrays.hashCode(this.ulv);
        hash = 53 * hash + Arrays.hashCode(this.slv);
        hash = 53 * hash + Arrays.deepHashCode(this.bav);
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
        final RandomEntity other = (RandomEntity) obj;
        if (!Arrays.equals(this.bv, other.bv)) {
            return false;
        }
        if (!Arrays.equals(this.uiv, other.uiv)) {
            return false;
        }
        if (!Arrays.equals(this.siv, other.siv)) {
            return false;
        }
        if (!Arrays.equals(this.ulv, other.ulv)) {
            return false;
        }
        if (!Arrays.equals(this.slv, other.slv)) {
            return false;
        }
        if (!Arrays.deepEquals(this.bav, other.bav)) {
            return false;
        }
        return true;
    }


    public RandomEntity read(final BitInput input) throws IOException {

        bv = new boolean[input.readUnsignedInt(7)];
        for (int i = 0; i < bv.length; i++) {
            bv[i] = input.readBoolean();
        }

        uil = new int[input.readUnsignedInt(7)];
        uiv = new int[uil.length];
        for (int i = 0; i < uiv.length; i++) {
            uil[i] = input.readUnsignedInt(5);
            uiv[i] = input.readUnsignedInt(uil[i]);
        }

        sil = new int[input.readUnsignedInt(7)];
        siv = new int[sil.length];
        for (int i = 0; i < sil.length; i++) {
            sil[i] = input.readUnsignedInt(5) + 1;
            siv[i] = input.readInt(sil[i]);
        }

        ull = new int[input.readUnsignedInt(7)];
        ulv = new long[ull.length];
        for (int i = 0; i < ull.length; i++) {
            ull[i] = input.readUnsignedInt(6);
            ulv[i] = input.readUnsignedLong(ull[i]);
        }

        sll = new int[input.readUnsignedInt(7)];
        slv = new long[sll.length];
        for (int i = 0; i < sll.length; i++) {
            sll[i] = input.readUnsignedInt(6) + 1;
            Assert.assertTrue(sll[i] > 1);
            Assert.assertTrue(sll[i] <= 64);
            slv[i] = input.readLong(sll[i]);
        }

        bas = new int[input.readUnsignedInt(7)];
        bar = new int[bas.length];
        bav = new byte[bar.length][];
        for (int i = 0; i < bav.length; i++) {
            bas[i] = input.readUnsignedInt(16);
            bar[i] = input.readUnsignedInt(8);
            System.out.println("r: " + bas[i] + "/" + bar[i]);
            bav[i] = input.readBytes(bas[i], bar[i]);
        }

        return this;
    }


    public RandomEntity write(final BitOutput output) throws IOException {

        output.writeUnsignedInt(7, bv.length);
        for (int i = 0; i < bv.length; i++) {
            output.writeBoolean(bv[i]);
        }

        output.writeUnsignedInt(7, uil.length);
        for (int i = 0; i < uil.length; i++) {
            output.writeUnsignedInt(5, uil[i]);
            output.writeUnsignedInt(uil[i], uiv[i]);
        }

        output.writeUnsignedInt(7, sil.length);
        for (int i = 0; i < sil.length; i++) {
            output.writeUnsignedInt(5, sil[i] - 1);
            output.writeInt(sil[i], siv[i]);
        }

        output.writeUnsignedInt(7, ull.length);
        for (int i = 0; i < ull.length; i++) {
            output.writeUnsignedInt(6, ull[i]);
            output.writeUnsignedLong(ull[i], ulv[i]);
        }

        output.writeUnsignedInt(7, sll.length);
        for (int i = 0; i < sll.length; i++) {
            output.writeUnsignedInt(6, sll[i] - 1);
            output.writeLong(sll[i], slv[i]);
        }

        output.writeUnsignedInt(7, bas.length);
        for (int i = 0; i < bav.length; i++) {
            output.writeUnsignedInt(16, bas[i]);
            output.writeUnsignedInt(8, bar[i]);
            System.out.println("w: " + bas[i] + "/" + bar[i]);
            output.writeBytes(bas[i], bar[i], bav[i]);
        }

        return this;
    }


    public RandomEntity read(final DataInput input) throws IOException {

        bv = new boolean[input.readUnsignedByte()];
        for (int i = 0; i < bv.length; i++) {
            bv[i] = input.readBoolean();
        }

        uiv = new int[input.readUnsignedByte()];
        for (int i = 0; i < uiv.length; i++) {
            uiv[i] = input.readInt();
        }

        siv = new int[input.readUnsignedByte()];
        for (int i = 0; i < siv.length; i++) {
            siv[i] = input.readInt();
        }

        ulv = new long[input.readUnsignedByte()];
        for (int i = 0; i < ulv.length; i++) {
            ulv[i] = input.readLong();
        }

        slv = new long[input.readUnsignedByte()];
        for (int i = 0; i < slv.length; i++) {
            slv[i] = input.readLong();
        }

        return this;
    }


    public RandomEntity write(final DataOutput output) throws IOException {

        output.writeByte(bv.length);
        for (boolean v : bv) {
            output.writeBoolean(v);
        }

        output.writeByte(uiv.length);
        for (int ui : uiv) {
            output.writeInt(ui);
        }

        output.writeByte(siv.length);
        for (int si : siv) {
            output.writeInt(si);
        }

        output.writeByte(ulv.length);
        for (long ul : ulv) {
            output.writeLong(ul);
        }

        output.writeByte(slv.length);
        for (long sl : slv) {
            output.writeLong(sl);
        }

        return this;
    }


    private boolean[] bv;


    private int[] uil;


    private int[] uiv;


    private int[] sil;


    private int[] siv;


    private int[] ull;


    private long[] ulv;


    private int[] sll;


    private long[] slv;


    private int[] bas;


    private int[] bar;


    private byte[][] bav;


}
