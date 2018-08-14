package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** TODO: Provide documentation, provide tests.  */
enum class DifficultyLevel(
        override val v: Int,
        val human: MineralSetC? = null,
        val computer: MineralSetC? = null
) : UniqueValueEnum, CountValueEnum {
    UNDEFINED(-1),

    EASY(
            0,
            MineralSet(15000, 30, 30, 15, 15, 15, 15),
            MineralSet(2500, 5, 5, 2, 2, 2, 2)
    ),

    NORMAL(
            1,
            MineralSet(10000, 20, 20, 10, 10, 10, 10),
            MineralSet(5000, 10, 10, 5, 5, 5, 5)
    ),

    HARD(
            2,
            MineralSet(5000, 10, 10, 5, 5, 5, 5),
            MineralSet(15000, 30, 30, 15, 15, 15, 15)
    ),

    EXPERT(
            3,
            MineralSet(2500, 5, 5, 2, 2, 2, 2),
            MineralSet(25000, 50, 50, 25, 25, 25, 25)
    ),

    IMPOSSIBLE(
            4,
            MineralSet(0, 0, 0, 0, 0, 0, 0),
            MineralSet(50000, 80, 80, 40, 40, 40, 40)
    ),

    COUNT(5);
}
