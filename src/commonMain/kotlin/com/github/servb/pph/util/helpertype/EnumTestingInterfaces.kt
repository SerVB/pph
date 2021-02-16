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

infix fun EnumWithValue.and(other: EnumWithValue): Int = this.v and other.v
infix fun Int.and(other: EnumWithValue): Int = this and other.v

infix fun EnumWithValue.xor(other: EnumWithValue): Int = this.v xor other.v
infix fun Int.xor(other: EnumWithValue): Int = this xor other.v

inline fun <reified T> getByValue(v: Int): T where T : EnumWithValue, T : Enum<T> {
    return enumValues<T>().single { it.v == v }
}
