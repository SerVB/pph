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
package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.staticfunction.Tracer

/** TODO: Remove the class. */
class constFurtSkills {

    var values: IntArray

    constructor() {
        values = IntArray(FURTHER_SKILLS.FSK_COUNT.getValue())
    }

    constructor(other: constFurtSkills) {
        values = other.values.clone()
    }

    fun Value(type: FURTHER_SKILLS): Int {
        Tracer.check(type.getValue() >= FURTHER_SKILLS.FSK_INVALID.getValue() && type.getValue() < FURTHER_SKILLS.FSK_COUNT.getValue())

        return values[type.getValue()]
    }

    fun Empty(): Boolean {
        for (xx in 0 until FURTHER_SKILLS.FSK_COUNT.getValue()) {
            if (values[xx] != 0) {
                return false
            }
        }
        return true
    }

    fun operatorP(other: constFurtSkills): iFurtSkills {
        return iFurtSkills(this).operatorPe(other)
    }

    fun operatorM(other: constFurtSkills): iFurtSkills {
        return iFurtSkills(this).operatorMe(other)
    }

}
