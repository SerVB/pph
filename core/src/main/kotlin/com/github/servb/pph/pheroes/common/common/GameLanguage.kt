package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Game languages.  TODO: provide tests, add all langs. */
enum class GameLanguage(override val v: Int) : UniqueValueEnum, CountValueEnum {
    ENGLISH(0),
    RUSSIAN(1),
    /*GLNG_POLISH,
    GLNG_SLOVAK,
    GLNG_GERMAN,
    GLNG_CZECH,
    GLNG_ITALIAN,
    GLNG_FRENCH,
    GLNG_SPANISH,*/
    COUNT(2);
}
