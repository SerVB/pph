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
package com.github.servb.pph.gxlib.gxlfixpoint;

/**
 * Fixed point class. Uses 32 bits to storage the value.
 * All the methods of the class will throw {@code NullPointerException} if they receive null argument.
 *
 * TODO: Correct division code ( now can lead to overflow / zero division in some cases )
 * TODO: Add diagnostics
 * TODO: Provide non-templated fixed32 class ( using optimized math from GPP )
 *
 * TODO: It seems that comparison operators aren't work right
 *       (i.e. {@code operatorG(final int a, final fix32 b)}).
 * TODO: Calculate the precision (it seems to be around 1e-4 per operation).
 *
 * @author SerVB
 */
public final class fix32 {

    /** The storage. */
    private int val;

    /** The precision per operation. */
    public static final float PRECISION = 1e-4f;

    /** The number of bits for fraction. */
    private static final int FRAC_BITS_NUMBER = 16;

    /** The mask of fraction bits. */
    private static final int FRAC_BITS_MASK;

    static {
        int lowBitsResult = 0;
        for (int i = 0; i < FRAC_BITS_NUMBER; ++i) {
            lowBitsResult |= 1 << i;
        }
        FRAC_BITS_MASK = lowBitsResult;
    }

    /**
     * Compares two values with the precision.
     *
     * @param a The first value.
     * @param b The second value.
     * @return  {@code True} if two values are almost equal, {@code false} otherwise.
     */
    public static final boolean closeValues(final fix32 a, final fix32 b) {
        return Math.abs(a.to_float() - b.to_float()) < PRECISION;
    }

    /**
     * Contains the immutable raw value of the floating point.
     */
    public static final class fp {
        /** Raw value container. */
        public final int toset_;

        /**
         * Constructs the object by the raw {@code fixed32} representation.
         *
         * @param val The raw fixed32 representation.
         */
        public fp(final int val) {
            toset_ = val;
        }
    }

    public static final fix32 zero() {
        return new fix32();
    }

    /**
     * Constructs the zero object.
     */
    public fix32() {
        this.val = 0;
    }

    /**
     * Copies the other object. Doesn't change the other object.
     *
     * @param other The other object to copy.
     */
    public fix32(final fix32 other) {
        this.val = other.val;
    }

    /**
     * Constructs the object by the integer value.
     *
     * @param i The integer value.
     */
    public fix32(final int i) {
        this.val = i << FRAC_BITS_NUMBER;
    }

    /**
     * Constructs the object by the float value.
     *
     * @param f The float value.
     */
    public fix32(final float f) {
        set(f);
    }

    /**
     * Constructs the object by the raw value.
     *
     * @param v The raw value.
     */
    public fix32(final fp v) {
        val = v.toset_;
    }

    /**
     * Sets this object to the other's state, doesn't modify the other object (operator=).
     *
     * @param rhs   The other object.
     * @return      This object.
     */
    public final fix32 operatorE(final fix32 rhs) {
        val = rhs.val;
        return  this;
    }

    /**
     * Sets this object to the integer value (operator=).
     *
     * @param rhs   The integer value.
     * @return      This object.
     */
    public final fix32 operatorE(final int rhs) {
        val = rhs << FRAC_BITS_NUMBER;
        return this;
    }

    /**
     * Sets this object to the float value (operator=).
     *
     * @param f The float value.
     * @return  This object.
     */
    public final fix32 operatorE(final float f) {
        set(f);
        return this;
    }

    /**
     * Sets this object to the other value (operator=).
     *
     * @param v The other object.
     * @return  This object.
     */
    public final fix32 operatorE(final fp v) {
        val = v.toset_;
        return this;
    }

    /**
     * Sets this object to the integer value.
     *
     * @param i The integer value.
     */
    public final void set(final int i) {
        val = i << FRAC_BITS_NUMBER;
    }

    /**
     * Sets this object to the float value.
     *
     * @param f The float value.
     */
    public final void set(final float f) {
        val = (int) (f * (1 << FRAC_BITS_NUMBER) + 0.5f);
    }

    /**
     * Converts the value to the floating point value. Doesn't modify the object.
     *
     * @return The floating point value.
     */
    public final float to_float() {
        return ((float) (val)) / (1 << FRAC_BITS_NUMBER);
    }

    /**
     * Unary minus operator. Returns the value with the changed sign, doesn't modify the object.
     *
     * @return The value with the changed sign.
     */
    public final fix32 operatorM() {
        fix32 tmp = new fix32(this);
        tmp.val = -tmp.val;
        return tmp;
    }

    /**
     * Adds the other value to this. Doesn't modify the other value (operator+=).
     *
     * @param rhs   The other value.
     * @return      This object.
     */
    public final fix32 operatorPe(final fix32 rhs) {
        val += rhs.val;
        return this;
    }

    /**
     * Subtracts the other value from this. Doesn't modify the other value (operator-=).
     *
     * @param rhs   The other value.
     * @return      This object.
     */
    public final fix32 operatorMe(final fix32 rhs) {
        val -= rhs.val;
        return this;
    }

    /**
     * Multiplies this value by the other. Doesn't modify the other value (operator*=).
     *
     * @param rhs   The other value.
     * @return      This object.
     */
    public final fix32 operatorAe(final fix32 rhs) {
        val = mul_shift(val, rhs.val);
        return this;
    }

    /**
     * Divides this value by the other. Doesn't modify the other value (operator/=).
     *
     * @param rhs   The other value.
     * @return      This object.
     */
    public final fix32 operatorSe(final fix32 rhs) {
        val = shift_div(val, rhs.val);
        return this;
    }

    /**
     * Adds the integer value to this value (operator+=).
     *
     * @param rhs   The integer value.
     * @return      This object.
     */
    public final fix32 operatorPe(final int rhs) {
        return operatorPe(new fix32(rhs));
    }

    /**
     * Subtracts the integer value from this value (operator-=).
     *
     * @param rhs   The integer value.
     * @return      This object.
     */
    public final fix32 operatorMe(final int rhs) {
        return operatorMe(new fix32(rhs));
    }

    /**
     * Multiplies this value by the integer value (operator*=).
     *
     * @param rhs   The integer value.
     * @return      This object.
     */
    public final fix32 operatorAe(final int rhs) {
        val *= rhs;
        return this;
    }

    /**
     * Divides this value by the integer value (operator/=).
     *
     * @param rhs   The integer value.
     * @return      This object.
     */
    public final fix32 operatorSe(final int rhs) {
        val /= rhs;
        return this;
    }

    /**
     * Binary plus operator. Sums two values, doesn't modify them.
     *
     * @param lhs   The first value.
     * @param rhs   The second value.
     * @return      The sum of two values.
     */
    public static final fix32 operatorP(final fix32 lhs, final fix32 rhs) {
        final fix32 tmp = new fix32(lhs);
        return tmp.operatorPe(rhs);
    }

    /**
     * Binary minus operator. Subtracts the second value from the first, doesn't modify the values.
     *
     * @param lhs   The first value.
     * @param rhs   The second value.
     * @return      The difference of two values.
     */
    public static final fix32 operatorM(final fix32 lhs, final fix32 rhs) {
        final fix32 tmp = new fix32(lhs);
        return tmp.operatorMe(rhs);
    }

    /**
     * Binary asterisk operator. Multiplies two values, doesn't modify them.
     *
     * @param lhs   The first value.
     * @param rhs   The second value.
     * @return      The product of two values.
     */
    public static final fix32 operatorA(final fix32 lhs, final fix32 rhs) {
        final fix32 tmp = new fix32(lhs);
        return tmp.operatorAe(rhs);
    }

    /**
     * Binary slash operator. Divides the first value by the second, doesn't modify the values.
     *
     * @param lhs   The first value.
     * @param rhs   The second value.
     * @return      The division result.
     */
    public static final fix32 operatorS(final fix32 lhs, final fix32 rhs) {
        final fix32 tmp = new fix32(lhs);
        return tmp.operatorSe(rhs);
    }

    /**
     * Binary asterisk operator. Multiplies two values, doesn't modify them.
     *
     * @param lhs   The fixed point value.
     * @param rhs   The integer value.
     * @return      The product of two values.
     */
    public static final fix32 operatorA(final fix32 lhs, final int rhs) {
        final fix32 tmp = new fix32(lhs);
        tmp.val *= rhs;
        return tmp;
    }

    /**
     * Binary asterisk operator. Multiplies two values, doesn't modify them.
     *
     * @param lhs   The integer value.
     * @param rhs   The fixed point value.
     * @return      The product of two values.
     */
    public static final fix32 operatorA(final int lhs, final fix32 rhs) {
        final fix32 tmp = new fix32(rhs);
        tmp.val *= lhs;
        return tmp;
    }

    /**
     * Binary slash operator. Divides the first value by the second, doesn't modify the values.
     *
     * @param lhs   The first value.
     * @param rhs   The second value.
     * @return      The division result.
     */
    public static final fix32 operatorS(final fix32 lhs, final int rhs) {
        final fix32 tmp = new fix32(lhs);
        tmp.val /= rhs;
        return tmp;
    }

    /**
     * Binary slash operator. Divides the first value by the second, doesn't modify the values.
     *
     * @param lhs   The first value.
     * @param rhs   The second value.
     * @return      The division result.
     */
    public static final fix32 operatorS(final int lhs, final fix32 rhs) {
        final fix32 tmp = new fix32(lhs);
        return tmp.operatorSe(rhs);
    }

    /**
     * Returns the floored value of the object. Doesn't modify the object.
     *
     * @return The floored value of the object.
     */
    public final int floor() {
        return val >> FRAC_BITS_NUMBER;
    }

    /**
     * Returns the ceiled value of the object. Doesn't modify the object.
     *
     * @return The ceiled value of the object.
     */
    public final int ceil() {
        int result = val >> FRAC_BITS_NUMBER;
        if ((val & FRAC_BITS_MASK) != 0) {
            ++result;
        }
        return result;
    }

    /** Multiplies then shifts. */
    private static int mul_shift(final int a, final int b) {
        final long v = (((long) a) * b) >> FRAC_BITS_NUMBER;
        return (int) v;
    }

    /** Shifts then divides. */
    private static int shift_div(final int a, final int b) {
        final long v = (((long) a) << FRAC_BITS_NUMBER) / b;
        return (int) v;
    }

    /**
     * Compares two values ({@code operator==}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorEe(final fix32 a, final fix32 b) {
        return a.val == b.val;
    }

    /**
     * Compares two values ({@code operator!=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorNee(final fix32 a, final fix32 b) {
        return !operatorEe(a, b);
    }

    /**
     * Compares two values ({@code operator>}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorG(final fix32 a, final fix32 b) {
        return a.val > b.val;
    }

    /**
     * Compares two values ({@code operator<}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorL(final fix32 a, final fix32 b) {
        return a.val < b.val;
    }

    /**
     * Compares two values ({@code operator>=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorGe(final fix32 a, final fix32 b) {
        return !operatorL(a, b);
    }

    /**
     * Compares two values ({@code operator<=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorLe(final fix32 a, final fix32 b) {
        return !operatorG(a, b);
    }

    /**
     * Compares two values ({@code operator==}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorEe(final fix32 a, final int b) {
        return a.ceil() == b;
    }

    /**
     * Compares two values ({@code operator!=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorNee(final fix32 a, final int b) {
        return a.ceil() != b;
    }

    /**
     * Compares two values ({@code operator>}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorG(final fix32 a, final int b) {
        return a.ceil() > b;
    }

    /**
     * Compares two values ({@code operator<}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorL(final fix32 a, final int b) {
        return a.ceil() < b;
    }

    /**
     * Compares two values ({@code operator>=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorGe(final fix32 a, final int b) {
        return !operatorL(a, b);
    }

    /**
     * Compares two values ({@code operator<=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorLe(final fix32 a, final int b) {
        return !operatorG(a, b);
    }

    /**
     * Compares two values ({@code operator==}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorEe(final int a, final fix32 b) {
        return a == b.ceil();
    }

    /**
     * Compares two values ({@code operator!=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorNee(final int a, final fix32 b) {
        return a != b.ceil();
    }

    /**
     * Compares two values ({@code operator>}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorG(final int a, final fix32 b) {
        return a > b.ceil();
    }

    /**
     * Compares two values ({@code operator<}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorL(final int a, final fix32 b) {
        return a < b.ceil();
    }

    /**
     * Compares two values ({@code operator>=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorGe(final int a, final fix32 b) {
        return !operatorL(a, b);
    }

    /**
     * Compares two values ({@code operator<=}).
     *
     * @param a The first value.
     * @param b The second value.
     * @return  The result of the comparison.
     */
    public static final boolean operatorLe(final int a, final fix32 b) {
        return !operatorG(a, b);
    }

    //<editor-fold defaultstate="collapsed" desc="FIXED_UNSAFE (commented in sources)">
////#define FIXED_UNSAFE
//#ifdef FIXED_UNSAFE
//    inline explicit fixed( uint_type i ) : val( value_type(i) << frac_bits ) {}
//    inline this_type& operator=( const uint_type rhs ) { val = value_type(rhs) << frac_bits; return *this; }
//    inline void set( uint_type i ) { val = (value_type(i) << frac_bits); }
//
//    inline this_type& operator+=( const uint_type rhs )
//    { return operator+=( this_type(rhs)); }
//    inline this_type& operator-=( const uint_type rhs )
//    { return operator-=( this_type(rhs)); }
//
//    inline this_type& operator*=( const uint_type rhs )
//    { val *= rhs; return *this; }
//    inline this_type& operator/=( const uint_type rhs )
//    { val /= rhs; return *this; }
//
//    friend inline this_type operator*( const this_type lhs, const uint_type rhs )
//    { this_type tmp(lhs); tmp.val *= rhs; return tmp; }
//    friend inline this_type operator*( const uint_type lhs, const this_type rhs )
//    { this_type tmp(rhs); tmp.val *= lhs; return tmp; }
//    friend inline this_type operator/( const this_type lhs, const uint_type rhs )
//    { this_type tmp(lhs); tmp.val /= rhs; return tmp; }
//    friend inline this_type operator/( const uint_type lhs, const this_type rhs )
//    { this_type tmp(lhs); tmp.val /= rhs; return tmp; }
//#endif //FIXED_UNSAFE
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    @Override
    public int hashCode() {
        return this.val;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final fix32 other = (fix32) obj;
        return this.val == other.val;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return String.valueOf(to_float());
    }
    //</editor-fold>

}
