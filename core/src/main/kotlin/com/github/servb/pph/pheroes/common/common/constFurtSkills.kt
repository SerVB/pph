package com.github.servb.pph.pheroes.common.common

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
        check(type.getValue() >= FURTHER_SKILLS.FSK_INVALID.getValue() && type.getValue() < FURTHER_SKILLS.FSK_COUNT.getValue())

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
