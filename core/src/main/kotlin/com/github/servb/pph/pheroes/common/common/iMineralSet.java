/*
 * Copyright 2018 SerVB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
