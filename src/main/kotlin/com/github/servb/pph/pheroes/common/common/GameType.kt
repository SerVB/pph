package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Game types. TODO: Provide documentation, provide tests. */
enum class GameType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    SPLAYER(0),
    HOTSEAT(1),
    NETWORK(2),
    COUNT(3);
}
