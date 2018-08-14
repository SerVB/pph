package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class HeroTypeMask(override val v: Int) : UniqueValueEnum {
    KNIGHT(0x1),
    BARBARIAN(0x2),
    WIZARD(0x4),
    WARLOCK(0x8),
    SORCERESS(0x10),
    NECROMANCER(0x20),

    GOOD(KNIGHT.v or WIZARD.v or SORCERESS.v),
    EVIL(BARBARIAN.v or WARLOCK.v or NECROMANCER.v),

    MIGHT(KNIGHT.v or BARBARIAN.v),
    MISC(SORCERESS.v or NECROMANCER.v),
    MAGIC(WIZARD.v or WARLOCK.v),

    ALL(GOOD.v or EVIL.v)
}
