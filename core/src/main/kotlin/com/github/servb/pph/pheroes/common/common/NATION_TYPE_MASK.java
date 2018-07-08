package com.github.servb.pph.pheroes.common.common;

import com.github.servb.pph.util.helpertype.EnumC;

/**
 *
 * @author SerVB
 */
public enum NATION_TYPE_MASK implements EnumC {

    NTM_HIGHMEN(0x1),
    NTM_BARBARIANS(0x2),
    NTM_WIZARDS(0x4),
    NTM_BEASTMEN(0x8),
    NTM_ELVES(0x10),
    NTM_UNDEADS(0x20);

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /**
     * The value of the element.
     */
    private int value;

    /**
     * Constructs a new element with the next value.
     */
    private NATION_TYPE_MASK() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private NATION_TYPE_MASK(final int value) {
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
