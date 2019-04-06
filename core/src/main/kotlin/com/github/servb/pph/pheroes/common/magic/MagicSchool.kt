package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.pheroes.common.common.skill.SecondarySkillType
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.or

enum class MagicSchoolType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    AIR(0),
    EARTH(1),
    FIRE(2),
    WATER(3),
    COUNT(4),
}

/** Magic school secondary skills (Air, Earth, Fire, Water). Size = MST_COUNT. */
val magicSchoolSecondarySkills: List<SecondarySkillType> = listOf(
        SecondarySkillType.AIRMAGIC,
        SecondarySkillType.EARTHMAGIC,
        SecondarySkillType.FIREMAGIC,
        SecondarySkillType.WATERMAGIC
)

enum class MagicSchoolMask(override val v: Int) : UniqueValueEnum {
    AIR(0b0001),
    EARTH(0b0010),
    FIRE(0b0100),
    WATER(0b1000),
    GOOD(AIR or EARTH or WATER),
    EVIL(AIR or EARTH or FIRE),
    ALL(AIR or EARTH or WATER or FIRE),
}
