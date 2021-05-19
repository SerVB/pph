package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.gxlib.IDibPixel
import com.github.servb.pph.gxlib.cColor
import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA

enum class PlayerId(
    override val v: Int,
    val color: IDibPixel = cColor.Gray64.pixel,
    val textColor: RGBA = Colors.VIOLET,  // todo: change it to match sources
) : UniqueValueEnum, UndefinedCountValueEnum {
    NEUTRAL(-1),

    RED(
        0,
        (0x1Fu shl 11).toUShort(),
        Colors["#FFF3CE"],
    ),

    GREEN(
        1,
        (0x30u shl 5).toUShort(),
        Colors["#FF1C42"],
    ),

    BLUE(
        2,
        0x1Fu,
        Colors["#FF31FF"],
    ),

    CYAN(
        3,
        ((0x30u shl 5) or 0x1Fu).toUShort(),
        Colors["#F7FF73"],
    ),

    MAGENTA(
        4,
        ((0x1Du shl 11) or 0x1Fu).toUShort(),
        Colors["#FFCEF7"],
    ),

    YELLOW(
        5,
        ((0x1Du shl 11) or (0x38u shl 5)).toUShort(),
        Colors["#FFDF3A"],
    ),

    COUNT(6);
}
