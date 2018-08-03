package com.github.servb.pph.pheroes.common.common

interface PrimarySkillsC {
    val values: List<Int>
}

class PrimarySkills : PrimarySkillsC {
    override val values: MutableList<Int>

    constructor() {
        values = MutableList(PRSKILL_TYPE.PRSKILL_COUNT.value) { 0 }
    }

    constructor(attack: Int, defence: Int, power: Int, knowledge: Int) {
        values = mutableListOf(attack, defence, power, knowledge)
    }

    constructor(other: PrimarySkillsC) {
        values = other.values.toMutableList()
    }

    operator fun plusAssign(other: PrimarySkillsC) {
        for (i in values.indices) {
            values[i] += other.values[i]
        }
    }

    operator fun minusAssign(ps: PrimarySkillsC) {
        for (i in values.indices) {
            values[i] -= ps.values[i]
        }
    }
}
