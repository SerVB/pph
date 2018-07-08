package com.github.servb.pph.pheroes.common.common;

/**
 *
 * @author SerVB
 */
public final class FractionCoeff {

    public int num, denum;

    public FractionCoeff() {
        num = -1;
        denum = -1;
    }

    public FractionCoeff(final int num, final int denum) {
        this.num = num;
        this.denum = denum;
    }

    public final boolean IsValid() {
        return num != -1 && denum != -1;
    }

    public final FractionCoeff GetNormalized() {
        if (num > denum) {
            return new FractionCoeff(num / denum, 1);
        }
        if (denum > num) {
            return new FractionCoeff(1, (denum + num - 1) / num);
        }
        return new FractionCoeff(1, 1);
    }

}
