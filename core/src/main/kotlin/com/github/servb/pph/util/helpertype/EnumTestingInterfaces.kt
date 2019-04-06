package com.github.servb.pph.util.helpertype

interface EnumWithValue {
    val v: Int
}

/** [v] has to be unique. */
interface UniqueValueEnum : EnumWithValue

/** Need to have an element containing "COUNT" and its value has to be a number of previous members. */
interface CountValueEnum : EnumWithValue

/**
 * Need to have an element containing "COUNT" and its value has to be a number of previous members except "UNDEFINED"
 * member (if there is).
 */
interface UndefinedCountValueEnum : EnumWithValue

infix fun EnumWithValue.or(other: EnumWithValue): Int = this.v or other.v
infix fun Int.or(other: EnumWithValue): Int = this or other.v
