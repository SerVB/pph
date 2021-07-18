package com.github.servb.pph.pheroes.common.common.skill

/** TODO: Remove the class. */
interface FurtherSkillsC {
    val values: List<Int>

    fun Value(type: FurtherSkill): Int {
        check(type.v in FurtherSkill.INVALID.v until FurtherSkill.COUNT.v)

        return values[type.v]
    }

    fun Empty(): Boolean {
        for (xx in 0 until FurtherSkill.COUNT.v) {
            if (values[xx] != 0) {
                return false
            }
        }
        return true
    }

    operator fun plus(other: FurtherSkillsC): FurtherSkillsC {
        val result = FurtherSkills(this)

        result += other

        return result
    }

    operator fun minus(other: FurtherSkillsC): FurtherSkillsC {
        val result = FurtherSkills(this)

        result -= other

        return result
    }
}

class FurtherSkills : FurtherSkillsC {
    override val values: MutableList<Int>

    constructor() {
        values = MutableList(FurtherSkill.COUNT.v) { 0 }
    }

    constructor(other: FurtherSkillsC) {
        values = other.values.toMutableList()
    }

    fun Reset() {
        for (i in values.indices) {
            values[i] = 0
        }
    }

    fun SetValue(type: FurtherSkill, newValue: Int) {
        check(type.v in FurtherSkill.INVALID.v until FurtherSkill.COUNT.v)

        values[type.v] = newValue
    }

    operator fun plusAssign(other: FurtherSkillsC) {
        for (xx in 0 until FurtherSkill.COUNT.v) {
            values[xx] += other.values[xx]
        }
    }

    operator fun minusAssign(other: FurtherSkillsC) {
        for (xx in 0 until FurtherSkill.COUNT.v) {
            values[xx] -= other.values[xx]
        }
    }

    operator fun plusAssign(other: PrimarySkillsC) {
        for (xx in 0 until PrimarySkillType.COUNT.v) {
            values[xx + FurtherSkill.ATTACK.v] += other.values[xx]
        }
    }

    operator fun minusAssign(other: PrimarySkillsC) {
        for (xx in 0 until PrimarySkillType.COUNT.v) {
            values[xx + FurtherSkill.ATTACK.v] -= other.values[xx]
        }
    }

    fun setTo(other: FurtherSkillsC) {
        values.clear()
        other.values.mapTo(values) { it }
    }
}
