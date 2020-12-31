package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class CastleSize(override val v: Int) : UniqueValueEnum {
    /** Village. */
    SMALL(1),

    /** Town. */
    MEDIUM(2),

    /** City. */
    LARGE(3),

    COUNT(4);
}
