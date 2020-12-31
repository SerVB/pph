package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class ObjectType(override val v: Int) : UniqueValueEnum {
    UNKNOWN(0),
    HERO(1),
    VIS_CNST(2),
    OWN_CNST(3),
    MAPOBJECT(4),
    DECOR(5);
}
