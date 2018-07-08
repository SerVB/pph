package com.github.servb.pph.pheroes.common.common;

/**
 *
 * @author SerVB
 */
public final class iPrSkills extends constPrSkills {

    public iPrSkills() {
        super();
    }

    public iPrSkills(final int attack, final int defence, final int power, final int knowledge) {
        super(attack, defence, power, knowledge);
    }

    public iPrSkills(final constPrSkills other) {
        super(other);
    }

    public final int[] getVal() {
        return val;
    }

    public final void operatorPe(final constPrSkills ps) {
        for (int xx = 0; xx < PRSKILL_TYPE.PRSKILL_COUNT.getValue(); ++xx) {
            val[xx] += ps.val[xx];
        }
    }

    public final void operatorMe(final constPrSkills ps) {
        for (int xx = 0; xx < PRSKILL_TYPE.PRSKILL_COUNT.getValue(); ++xx) {
            val[xx] -= ps.val[xx];
        }
    }

}
