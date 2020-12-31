package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.or

enum class SpellLevel(override val v: Int) : UniqueValueEnum, CountValueEnum {
    FIRST(0),
    SECOND(1),
    THIRD(2),
    FOURTH(3),
    FIFTH(4),
    COUNT(5),
}

/** Size = SPL_COUNT. */
@ExperimentalUnsignedTypes
val spellLevelMasks: List<UInt> = listOf(
        SpellLevelMask.FIRST.v.toUInt(),
        SpellLevelMask.SECOND.v.toUInt(),
        SpellLevelMask.THIRD.v.toUInt(),
        SpellLevelMask.FOURTH.v.toUInt(),
        SpellLevelMask.FIFTH.v.toUInt()
)

enum class SpellLevelMask(override val v: Int) : UniqueValueEnum {
    NONE(0),
    FIRST(0b00001),
    SECOND(0b00010),
    THIRD(0b00100),
    FOURTH(0b01000),
    FIFTH(0b10000),
    ALL(FIRST or SECOND or THIRD or FOURTH or FIFTH),
}
