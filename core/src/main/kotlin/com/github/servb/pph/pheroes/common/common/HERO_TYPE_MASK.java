package com.github.servb.pph.pheroes.common.common;

import com.github.servb.pph.util.helpertype.EnumC;

/**
 *
 * @author SerVB
 */
public enum HERO_TYPE_MASK implements EnumC {

    HTM_KNIGHT(0x1),
    HTM_BARBARIAN(0x2),
    HTM_WIZARD(0x4),
    HTM_WARLOCK(0x8),
    HTM_SORCERESS(0x10),
    HTM_NECROMANCER(0x20),
    HTM_GOOD(HTM_KNIGHT.value | HTM_WIZARD.value | HTM_SORCERESS.value),
    HTM_EVIL(HTM_BARBARIAN.value | HTM_WARLOCK.value | HTM_NECROMANCER.value),
    HTM_MIGHT(HTM_KNIGHT.value | HTM_BARBARIAN.value),
    HTM_MISC(HTM_SORCERESS.value | HTM_NECROMANCER.value),
    HTM_MAGIC(HTM_WIZARD.value | HTM_WARLOCK.value),
    HTM_ALL(HTM_GOOD.value | HTM_EVIL.value);

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /**
     * The value of the element.
     */
    private int value;

    /**
     * Constructs a new element with the next value.
     */
    private HERO_TYPE_MASK() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private HERO_TYPE_MASK(final int value) {
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
