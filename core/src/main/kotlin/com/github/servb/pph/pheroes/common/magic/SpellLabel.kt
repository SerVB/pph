package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class SpellLabel(override val v: Int) : UniqueValueEnum, UndefinedCountValueEnum {
    NONE(0xFF),
    PROTEARTH(0),
    HASTE(1),
    SHIELD(2),
    PRECISION(3),
    AIRSHIELD(4),
    COUNTERSTRIKE(5),
    PROTAIR(6),
    SLOW(7),
    STONESKIN(8),
    SORROW(9),
    BLOODLUST(10),
    PROTWATER(11),
    CURSE(12),
    BLIND(13),
    WEAKNESS(14),
    MISFORTUNE(15),
    BLESS(16),
    PROTFIRE(17),
    FORTUNE(18),
    MIRTH(19),
    ANTIMAGIC(20),
    PRAYER(21),
    DISRAY(22),
    COUNT(23),
}
