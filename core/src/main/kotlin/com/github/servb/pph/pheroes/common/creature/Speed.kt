package com.github.servb.pph.pheroes.common.creature

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class Speed(override val v: Int, private val description: String? = null) : UniqueValueEnum {
    SPEED_SUPERSLOW(1),
    SPEED_ULTRASLOW(2),
    SPEED_VERYSLOW(3),
    SPEED_EXTRASLOW(4),
    SPEED_SLOW(5),
    SPEED_SWIFT(6),
    SPEED_EXTRASWIFT(7),
    SPEED_VERYSWIFT(8),
    SPEED_ULTRASWIFT(9, "2 to 2"),
    SPEED_SUPERSWIFT(10, "1 to 2 or 2 to 1"),
    SPEED_QUICK(11, "1 to 1"),
    SPEED_EXTRAQUICK(12),
    SPEED_VERYQUICK(13),
    SPEED_ULTRAQUICK(14),
    SPEED_SUPERQUICK(15),
    SPEED_FAST(16),
    SPEED_EXTRAFAST(17),
    SPEED_VERYFAST(18),
    SPEED_ULTRAFAST(19),
    SPEED_SUPERFAST(20),
    SPEED_MAX(21);
}