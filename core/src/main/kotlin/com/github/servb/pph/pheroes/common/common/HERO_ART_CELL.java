package com.github.servb.pph.pheroes.common.common;

import com.github.servb.pph.util.helpertype.EnumC;

/**
 * HERO_ART_CELL. TODO: Provide documentation.
 *
 * @author SerVB
 */
public enum HERO_ART_CELL implements EnumC {

    AC_UNDEFINED(-1),
    AC_HEAD,
    AC_NECK,
    AC_TORSO,
    AC_LHAND,
    AC_RHAND,
    AC_LFINGERS,
    AC_RFINGERS,
    AC_SHOULDERS,
    AC_LEGS,
    AC_FEET,
    AC_MISC1,
    AC_MISC2,
    AC_MISC3,
    AC_MISC4,
    AC_COUNT;

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /** The value of the element. */
    private int value;

    /** Constructs a new element with the next value. */
    private HERO_ART_CELL() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private HERO_ART_CELL(final int value) {
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

    /** Object that holds the next value. */
    private final static class NextValueHolder {

        /** The next value. */
        private static int nextValue = 0;

    }
    //</editor-fold>

}
