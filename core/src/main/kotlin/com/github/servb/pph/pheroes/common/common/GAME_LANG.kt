package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Game languages.  TODO: provide tests, add all langs. */
enum class GAME_LANG(override val v: Int) : UniqueValueEnum, CountValueEnum {
    GLNG_ENGLISH(0),
    GLNG_RUSSIAN(1),
    /*GLNG_POLISH,
    GLNG_SLOVAK,
    GLNG_GERMAN,
    GLNG_CZECH,
    GLNG_ITALIAN,
    GLNG_FRENCH,
    GLNG_SPANISH,*/
    GLNG_COUNT(2);
}
