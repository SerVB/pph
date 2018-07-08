package com.github.servb.pph.pheroes.common.common;

import com.github.servb.pph.util.helpertype.EnumC;

/**
 * Object types.
 *
 * @author SerVB
 */
public enum OBJ_TYPE implements EnumC {

    OBJT_UNKNOWN(0),
    OBJT_HERO,
    OBJT_VIS_CNST,
    OBJT_OWN_CNST,
    OBJT_MAPOBJECT,
    OBJT_DECOR;

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /**
     * The value of the element.
     */
    private int value;

    /**
     * Constructs a new element with the next value.
     */
    private OBJ_TYPE() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private OBJ_TYPE(final int value) {
        this.value = value;                     // Set the specified value to this value
        NextValueHolder.nextValue = value + 1;  // Increment the next value for a next element
    }

    /**
     * Returns the value of the element.
     *
     * @return The value of the element.
     */
    @Override
    public final int getValue() {
        return value;
    }

    /**
     * Object that holds the next value.
     */
    private final static class NextValueHolder {

        /**
         * The next value.
         */
        private static int nextValue = 0;

    }
    //</editor-fold>

}
