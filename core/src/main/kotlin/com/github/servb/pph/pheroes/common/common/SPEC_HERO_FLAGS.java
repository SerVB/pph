package com.github.servb.pph.pheroes.common.common;

import com.github.servb.pph.util.helpertype.EnumC;

/**
 * SPEC_HERO_FLAGS. TODO: Provide documentation.
 *
 * @author SerVB
 */
public enum SPEC_HERO_FLAGS implements EnumC {

    SHF_INVALID(-1),

    /** No range attack penalty */
    SHF_NORANGEPENALTY,

    /** 50% bonus for resurrection and summon spells */
    SHF_SUMRESBOUNS,

    /** Mana restores each day */
    SHF_MANARESTORE,

    /** Effect from all damage spells increased by 50% */
    SHF_DMGSPBONUS,

    /** Necromancy skill restores mummies instead of skeletons */
    SHF_NECRBONUS;

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /** The value of the element. */
    private int value;

    /** Constructs a new element with the next value. */
    private SPEC_HERO_FLAGS() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private SPEC_HERO_FLAGS(final int value) {
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
