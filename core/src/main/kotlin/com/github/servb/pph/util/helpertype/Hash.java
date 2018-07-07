/*
 * Copyright 2018 SerVB.
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
package com.github.servb.pph.util.helpertype;

/**
 * Hash Calculator. From "Effective Java".
 *
 * @author SerVB
 */
public final class Hash {

    /** Default initial value of the hash. */
    private final static int INITIAL_VALUE = 17;

    /** Default multiplication value for calculating the hash. */
    private final static int MUL_VALUE = 31;

    /** The result of calculations. */
    private int result;

    /** The multiplication value for calculating the hash. */
    private final int mul;

    /**
     * Initializes the Hash with the given values.
     *
     * @param initial   The initial value of the hash.
     * @param mul       The multiplication value for calculating the hash.
     */
    private Hash(final int initial, final int mul) {
        result = initial;
        this.mul = mul;
    }

    /**
     * Constructs the Hash object: Initializes the Hash with the standard values.
     *
     * @return The new Hash object.
     */
    public static Hash std() {
        return from(INITIAL_VALUE);
    }

    /**
     * Constructs the Hash object:
     * Initializes the Hash with the given initial value and the standard multiplication value.
     *
     * @param initial   The initial value of the Hash.
     * @return          The new Hash object.
     */
    public static Hash from(final int initial) {
        return fromAndMul(initial, MUL_VALUE);
    }

    /**
     * Constructs the Hash object: Initializes the Hash with the given values.
     *
     * @param initial   The initial value of the hash.
     * @param mul       The multiplication value for calculating the hash.
     * @return          The new Hash object.
     */
    public static Hash fromAndMul(final int initial, final int mul) {
        return new Hash(initial, mul);
    }

    /**
     * Returns the result of Hash calculations.
     *
     * @return The result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Inserts int value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final int f) {
        result = mul * result + f;
    }

    /**
     * Inserts boolean value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final boolean f) {
        insert(f ? 1 : 0);
    }

    /**
     * Inserts byte value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final byte f) {
        insert((int) f);
    }

    /**
     * Inserts char value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final char f) {
        insert((int) f);
    }

    /**
     * Inserts short value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final short f) {
        insert((int) f);
    }

    /**
     * Inserts long value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final long f) {
        insert((int)(f ^ (f >>> 32)));
    }

    /**
     * Inserts float value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final float f) {
        insert(Float.floatToIntBits(f));
    }

    /**
     * Inserts double value to the Hash.
     *
     * @param f Value to be inserted.
     */
    public void insert(final double f) {
        insert(Double.doubleToLongBits(f));
    }

    /**
     * Inserts Object to the Hash.
     *
     * @param f Object to be inserted.
     */
    public void insert(final Object f) {
        if (f == null) {
            insert(0);
        } else {
            insert(f.hashCode());
        }
    }
}