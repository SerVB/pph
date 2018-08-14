package com.github.servb.pph.pheroes.common.common

import com.badlogic.gdx.graphics.Color
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class PlayerId(
        override val v: Int,
        val color: Int = -42,
        val textColor: Int = -42
) : UniqueValueEnum, CountValueEnum {
    NEUTRAL(-1),

    RED(
            0,
            Color.valueOf("#FF0000").toIntBits(),
            Color.valueOf("#FFF3CE").toIntBits()
    ),

    GREEN(
            1,
            Color.valueOf("#00C200").toIntBits(),
            Color.valueOf("#FF1C42").toIntBits()
    ),

    BLUE(
            2,
            Color.valueOf("#0000FF").toIntBits(),
            Color.valueOf("#FF31FF").toIntBits()
    ),

    CYAN(
            3,
            Color.valueOf("#00C2FF").toIntBits(),
            Color.valueOf("#F7FF73").toIntBits()
    ),

    MAGENTA(
            4,
            Color.valueOf("#EF00FF").toIntBits(),
            Color.valueOf("#FFCEF7").toIntBits()
    ),

    YELLOW(
            5,
            Color.valueOf("#EFE300").toIntBits(),
            Color.valueOf("#FFDF3A").toIntBits()
    ),

    COUNT(6);
}
