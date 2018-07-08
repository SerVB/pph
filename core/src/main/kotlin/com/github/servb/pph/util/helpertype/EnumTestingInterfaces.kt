package com.github.servb.pph.util.helpertype

/** [v] has to be unique. */
interface UniqueValueEnum {
    val v: Int
}

/**
 * Need to have an element containing "COUNT" and its value has to be a number of previous members except "UNDEFINED"
 * member (if there is).
 */
interface CountValueEnum {
    val v: Int
}
