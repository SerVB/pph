package com.github.servb.pph.pheroes.common.common;

/**
 * Immutable.
 *
 * @author SerVB
 */
public class constPrSkills {

    public constPrSkills() {
        val = new int[PRSKILL_TYPE.PRSKILL_COUNT.getValue()];
    }

    public constPrSkills(final int attack, final int defence, final int power, final int knowledge) {
        val = new int[PRSKILL_TYPE.PRSKILL_COUNT.getValue()];

        val[0] = attack;
        val[1] = defence;
        val[2] = power;
        val[3] = knowledge;
    }

    public constPrSkills(final constPrSkills other) {
        val = other.val.clone();
    }

    public final int getVal(final int idx) {
        return val[idx];
    }

    protected int[] val;

}
