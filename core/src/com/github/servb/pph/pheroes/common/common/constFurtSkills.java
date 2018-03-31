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

/**
 *
 * @author SerVB
 */
public class constFurtSkills {

    public constFurtSkills() {
        values = new int[FURTHER_SKILLS.FSK_COUNT.getValue()];
    }

    public constFurtSkills(final constFurtSkills other) {
        values = other.values.clone();
    }

    public final int Value(final FURTHER_SKILLS type) {
        Tracer.check(type.getValue() >= FURTHER_SKILLS.FSK_INVALID.getValue() &&
                type.getValue() < FURTHER_SKILLS.FSK_COUNT.getValue());

        return values[type.getValue()];
    }

    public final boolean Empty() {
        for (int xx = 0; xx < FURTHER_SKILLS.FSK_COUNT.getValue(); ++xx) {
            if (values[xx] != 0) {
                return false;
            }
        }
        return true;
    }

    public final iFurtSkills operatorP(final constFurtSkills other) {
        return new iFurtSkills(this).operatorPe(other);
    }

    public final iFurtSkills operatorM(final constFurtSkills other) {
        return new iFurtSkills(this).operatorMe(other);
    }

    protected int[] values;

}
