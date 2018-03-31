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
