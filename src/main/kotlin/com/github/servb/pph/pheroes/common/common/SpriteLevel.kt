package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class SpriteLevel(override val v: Int) : UniqueValueEnum {
    GROUND(0),
    PLANT(1),
    OBJECT(2);
}
