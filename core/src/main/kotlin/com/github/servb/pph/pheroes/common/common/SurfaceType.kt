package com.github.servb.pph.pheroes.common.common

import com.badlogic.gdx.graphics.Color
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class SurfaceType(
        override val v: Int,
        val mask: Int = -42,
        val moveCost: Int = -42,
        val color: Int = -42
) : UniqueValueEnum, CountValueEnum {
    WATER(0, 1 shl 0, 255, Color.toIntBits(8, 28, 128, 255)),
    SAND(1, 1 shl 1, 12, Color.toIntBits(214, 182, 148, 255)),
    DIRT(2, 1 shl 2, 6, Color.toIntBits(99, 48, 8, 255)),
    GRASS(3, 1 shl 3, 6, Color.toIntBits(24, 97, 16, 255)),
    SWAMP(4, 1 shl 4, 14, Color.toIntBits(0, 44, 0, 255)),
    LAVA(5, 1 shl 5, 10, Color.toIntBits(48, 48, 48, 255)),
    WASTELAND(6, 1 shl 6, 8, Color.toIntBits(165, 85, 16, 255)),
    DESERT(7, 1 shl 7, 12, Color.toIntBits(181, 138, 24, 255)),
    SNOW(8, 1 shl 8, 10, Color.toIntBits(220, 220, 220, 255)),
    NEW_DESERT(9, 1 shl 9, 12, Color.toIntBits(192, 160, 0, 255)),
    PAVEMENT(10, 1 shl 10, 4, Color.toIntBits(160, 160, 160, 255)),
    NEW_WASTELAND(10, 1 shl 11, 9, Color.toIntBits(192, 192, 160, 255)),
    COUNT(11);
}
