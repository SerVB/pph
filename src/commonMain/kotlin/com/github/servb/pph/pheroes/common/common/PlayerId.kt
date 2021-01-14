package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA

enum class PlayerId(
    override val v: Int,
    val color: RGBA = Colors.VIOLET,
    val textColor: RGBA = Colors.VIOLET,
) : UniqueValueEnum, UndefinedCountValueEnum {
    NEUTRAL(-1),

    RED(
        0,
        Colors["#FF0000"],
        Colors["#FFF3CE"],
    ),

    GREEN(
        1,
        Colors["#00C200"],
        Colors["#FF1C42"],
    ),

    BLUE(
        2,
        Colors["#0000FF"],
        Colors["#FF31FF"],
    ),

    CYAN(
        3,
        Colors["#00C2FF"],
        Colors["#F7FF73"],
    ),

    MAGENTA(
        4,
        Colors["#EF00FF"],
        Colors["#FFCEF7"],
    ),

    YELLOW(
        5,
        Colors["#EFE300"],
        Colors["#FFDF3A"],
    ),

    COUNT(6);
}
