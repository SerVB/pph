package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.gxlib.IDibPixel
import com.github.servb.pph.gxlib.cColor
import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class PlayerId(
    override val v: Int,
    val color: IDibPixel = cColor.Gray64.pixel,
    val textColor: String = "#FE8E",
) : UniqueValueEnum, UndefinedCountValueEnum {
    NEUTRAL(-1),

    RED(
        0,
        (0x1Fu shl 11).toUShort(),
        "#FF99",
    ),

    GREEN(
        1,
        (0x30u shl 5).toUShort(),
        "#F8E8",
    ),

    BLUE(
        2,
        0x1Fu,
        "#F99F",
    ),

    CYAN(
        3,
        ((0x30u shl 5) or 0x1Fu).toUShort(),
        "#F7EE",
    ),

    MAGENTA(
        4,
        ((0x1Du shl 11) or 0x1Fu).toUShort(),
        "#FE7E",
    ),

    YELLOW(
        5,
        ((0x1Du shl 11) or (0x38u shl 5)).toUShort(),
        "#FEE7",
    ),

    COUNT(6);
}
