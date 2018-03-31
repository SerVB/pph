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

import com.github.servb.pph.util.staticFunction.Tracer;
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
        Arrays.fill(values, 0);
    }

    public final void SetValue(final FURTHER_SKILLS type, final int newValue) {
        Tracer.check(type.getValue() >= FURTHER_SKILLS.FSK_INVALID.getValue() &&
                type.getValue() < FURTHER_SKILLS.FSK_COUNT.getValue());

        values[type.getValue()] = newValue;
    }

    public final iFurtSkills operatorPe(final constFurtSkills fs) {
        for (int xx = 0; xx < FURTHER_SKILLS.FSK_COUNT.getValue(); ++xx) {
            values[xx] += fs.values[xx];
        }
        return this;
    }

    public final iFurtSkills operatorMe(final constFurtSkills fs) {
        for (int xx = 0; xx < FURTHER_SKILLS.FSK_COUNT.getValue(); ++xx) {
            values[xx] -= fs.values[xx];
        }
        return this;
    }

    public final iFurtSkills operatorPe(final constPrSkills ps) {
        for (int xx = 0; xx < PRSKILL_TYPE.PRSKILL_COUNT.getValue(); ++xx) {
            values[xx + FURTHER_SKILLS.FSK_ATTACK.getValue()] += ps.val[xx];
        }
        return this;
    }

    public final iFurtSkills operatorMe(final constPrSkills ps) {
        for (int xx = 0; xx < PRSKILL_TYPE.PRSKILL_COUNT.getValue(); ++xx) {
            values[xx + FURTHER_SKILLS.FSK_ATTACK.getValue()] -= ps.val[xx];
        }
        return this;
    }

}
