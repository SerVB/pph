package com.github.servb.pph.pheroes.common.common;

import java.util.Arrays;

/**
 *
 * @author SerVB
 */
public final class iMineralSet extends constMineralSet {

    public iMineralSet() {
        super();
    }

    public final void Reset() {
        Arrays.fill(quant, 0);
    }

    public final void Normalize() {
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            if (quant[xx] < 0) {
                quant[xx] = 0;
            }
        }
    }

    public final constMineralSet operatorE(final constMineralSet ms) {
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            quant[xx] = ms.quant[xx];
        }
        return this;
    }

    public final constMineralSet operatorPe(final constMineralSet ms) {
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            quant[xx] += ms.quant[xx];
        }
        return this;
    }

    public final constMineralSet operatorMe(final constMineralSet ms) {
        for (int xx = 0; xx < MINERAL_TYPE.MINERAL_COUNT.getValue(); ++xx) {
            quant[xx] -= ms.quant[xx];
        }
        return this;
    }

}
