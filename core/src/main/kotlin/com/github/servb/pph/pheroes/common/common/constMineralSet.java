package com.github.servb.pph.pheroes.common.common;

/**
 * Immutable.
 *
 * @author SerVB
 */
public class constMineralSet {

    protected int[] quant;

    public constMineralSet() {
        this.quant = new int[MINERAL_TYPE.MINERAL_COUNT.getValue()];
    }

    public constMineralSet(final int[] quant) {
        this.quant = new int[MINERAL_TYPE.MINERAL_COUNT.getValue()];
        System.arraycopy(quant, 0, this.quant, 0, quant.length);
    }

    public final int Has(final constMineralSet ms) {
        int cnt = 0;
        for (int xx=0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            if (quant[xx] < ms.quant[xx]) {
                return 0;
            }
            if (ms.quant[xx] != 0) {
                cnt = (cnt != 0) ? Math.min(cnt, quant[xx] / ms.quant[xx]) : quant[xx] / ms.quant[xx];
            }
        }
        return cnt;
    }

    public final iMineralSet operatorA(final int val) {
        final iMineralSet result = new iMineralSet();
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            result.quant[xx] = quant[xx] * val;
        }
        return result;
    }

    public final boolean Empty() {
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            if (quant[xx] != 0) {
                return false;
            }
        }
        return true;
    }

    public final iMineralSet DeficientAmount(final constMineralSet other) {
        final iMineralSet result = new iMineralSet();
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            if (other.quant[xx] > quant[xx]) {
                result.quant[xx] = other.quant[xx] - quant[xx];
            }
        }
        return result;
    }

    public final iMineralSet Intersect(final constMineralSet other) {
        final iMineralSet result = new iMineralSet();
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            result.quant[xx] = Math.min(quant[xx], other.quant[xx]);
        }
        return result;
    }

    public final boolean operatorEe(final constMineralSet ms) {
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            if (quant[xx] != ms.quant[xx]) {
                return false;
            }
        }
        return true;
    }

    public final boolean operatorNee(final constMineralSet ms) {
        return !operatorEe(ms);
    }

}
