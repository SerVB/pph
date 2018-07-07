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
