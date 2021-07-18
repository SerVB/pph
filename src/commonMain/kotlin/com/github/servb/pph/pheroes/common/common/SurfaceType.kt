package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.gxlib.IDibPixel
import com.github.servb.pph.gxlib.RGB16
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class SurfaceType(
    override val v: Int,
    val mask: Int = -42,
    val moveCost: Int = -42,
    val color: IDibPixel = 42u,
) : UniqueValueEnum, CountValueEnum {
    WATER(0, 1 shl 0, 255, RGB16(8, 28, 128)),
    SAND(1, 1 shl 1, 12, RGB16(214, 182, 148)),
    DIRT(2, 1 shl 2, 6, RGB16(99, 48, 8)),
    GRASS(3, 1 shl 3, 6, RGB16(24, 97, 16)),
    SWAMP(4, 1 shl 4, 14, RGB16(0, 44, 0)),
    LAVA(5, 1 shl 5, 10, RGB16(48, 48, 48)),
    WASTELAND(6, 1 shl 6, 8, RGB16(165, 85, 16)),
    DESERT(7, 1 shl 7, 12, RGB16(181, 138, 24)),
    SNOW(8, 1 shl 8, 10, RGB16(220, 220, 220)),
    NEW_DESERT(9, 1 shl 9, 12, RGB16(192, 160, 0)),
    PAVEMENT(10, 1 shl 10, 4, RGB16(160, 160, 160)),
    NEW_WASTELAND(11, 1 shl 11, 9, RGB16(192, 192, 160)),
    COUNT(12);
}
