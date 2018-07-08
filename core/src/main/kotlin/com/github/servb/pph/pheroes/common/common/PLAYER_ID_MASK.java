package com.github.servb.pph.pheroes.common.common;

import com.github.servb.pph.util.helpertype.EnumC;

/**
 *
 * @author SerVB
 */
public enum PLAYER_ID_MASK implements EnumC {

    PIM_NONE(0x00),
    PIM_RED(0x01),
    PIM_GREEN(0x02),
    PIM_BLUE(0x04),
    PIM_CYAN(0x08),
    PIM_MAGENTA(0x10),
    PIM_YELLOW(0x20),
    PIM_ALL(
            PIM_RED.value | PIM_GREEN.value | PIM_BLUE.value |
            PIM_CYAN.value | PIM_MAGENTA.value | PIM_YELLOW.value
    );

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /**
     * The value of the element.
     */
    private int value;

    /**
     * Constructs a new element with the next value.
     */
    private PLAYER_ID_MASK() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private PLAYER_ID_MASK(final int value) {
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
