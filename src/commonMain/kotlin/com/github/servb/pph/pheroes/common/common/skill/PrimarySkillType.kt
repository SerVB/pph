package com.github.servb.pph.pheroes.common.common.skill

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class PrimarySkillType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    ATTACK(0),
    DEFENCE(1),
    POWER(2),
    KNOWLEDGE(3),
    COUNT(4);
}
