package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Game types. TODO: Provide documentation, provide tests. */
enum class GAME_TYPE(override val v: Int) : UniqueValueEnum, CountValueEnum {
    GMT_SPLAYER(0),
    GMT_HOTSEAT(1),
    GMT_NETWORK(2),
    GMT_COUNT(3);
}
