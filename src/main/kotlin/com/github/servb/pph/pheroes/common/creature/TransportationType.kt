package com.github.servb.pph.pheroes.common.creature

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class TransportationType(override val v: Int) : UniqueValueEnum {
    WALK(0),
    FLY(1);
}