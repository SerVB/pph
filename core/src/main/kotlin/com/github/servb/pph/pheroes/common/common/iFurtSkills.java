package com.github.servb.pph.pheroes.common.common;

import java.util.Arrays;

/**
 *
 * @author SerVB
 */
public final class iFurtSkills extends constFurtSkills {

    public iFurtSkills() {
        super();
    }

    public iFurtSkills(final constFurtSkills other) {
        super(other);
    }

    public final void Reset() {
        Arrays.fill(getValues(), 0);
    }

    public final void SetValue(final FURTHER_SKILLS type, final int newValue) {
        check(type.getValue() >= FURTHER_SKILLS.FSK_INVALID.getValue() &&
                type.getValue() < FURTHER_SKILLS.FSK_COUNT.getValue());

        getValues()[type.getValue()] = newValue;
    }

    public final iFurtSkills operatorPe(final constFurtSkills fs) {
        for (int xx = 0; xx < FURTHER_SKILLS.FSK_COUNT.getValue(); ++xx) {
            getValues()[xx] += fs.getValues()[xx];
        }
        return this;
    }

    public final iFurtSkills operatorMe(final constFurtSkills fs) {
        for (int xx = 0; xx < FURTHER_SKILLS.FSK_COUNT.getValue(); ++xx) {
            getValues()[xx] -= fs.getValues()[xx];
        }
        return this;
    }

    public final iFurtSkills operatorPe(final constPrSkills ps) {
        for (int xx = 0; xx < PRSKILL_TYPE.PRSKILL_COUNT.getValue(); ++xx) {
            getValues()[xx + FURTHER_SKILLS.FSK_ATTACK.getValue()] += ps.val[xx];
        }
        return this;
    }

    public final iFurtSkills operatorMe(final constPrSkills ps) {
        for (int xx = 0; xx < PRSKILL_TYPE.PRSKILL_COUNT.getValue(); ++xx) {
            getValues()[xx + FURTHER_SKILLS.FSK_ATTACK.getValue()] -= ps.val[xx];
        }
        return this;
    }

}
