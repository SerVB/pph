package com.github.servb.pph.gxlib.gxlcommontpl

import com.github.servb.pph.util.staticfunction.Tracer

/**
 * "Clamps" [value] between [min] and [max].
 * If [min] `<=` [value] `<=` [max] then [value] is returned.
 * If [value] `<` [min] then [min] is returned.
 * Otherwise [max] is returned.
 *
 * @param min   The minimum limit.
 * @param max   The maximum limit, should be not less than the minimum limit.
 * @param value The value to be clamped.
 * @return      The clamped value.
 */
inline fun <reified T : Comparable<T>> iCLAMP(min: T, max: T, value: T): T {
    Tracer.check(min <= max)

    if (value in min..max) {
        return value
    }

    return if (value < min) {
        min
    } else {
        max
    }
}

//<editor-fold defaultstate="collapsed" desc="iDIF">
/**
 * Returns the positive difference between two values.
 *
 * @param x The first value.
 * @param y The second value.
 * @return  The positive difference.
 */
fun iDIF(x: Byte, y: Byte): Byte {
    return (if (x > y) x - y else y - x).toByte()
}

/**
 * Returns the positive difference between two values.
 *
 * @param x The first value.
 * @param y The second value.
 * @return  The positive difference.
 */
fun iDIF(x: Short, y: Short): Short {
    return (if (x > y) x - y else y - x).toShort()
}

/**
 * Returns the positive difference between two values.
 *
 * @param x The first value.
 * @param y The second value.
 * @return  The positive difference.
 */
fun iDIF(x: Int, y: Int): Int {
    return (if (x > y) x - y else y - x).toInt()
}

/**
 * Returns the positive difference between two values.
 *
 * @param x The first value.
 * @param y The second value.
 * @return  The positive difference.
 */
fun iDIF(x: Long, y: Long): Long {
    return (if (x > y) x - y else y - x).toLong()
}

/**
 * Returns the positive difference between two values.
 *
 * @param x The first value.
 * @param y The second value.
 * @return  The positive difference.
 */
fun iDIF(x: Float, y: Float): Float {
    return (if (x > y) x - y else y - x).toFloat()
}

/**
 * Returns the positive difference between two values.
 *
 * @param x The first value.
 * @param y The second value.
 * @return  The positive difference.
 */
fun iDIF(x: Double, y: Double): Double {
    return (if (x > y) x - y else y - x).toDouble()
}
//</editor-fold>

/**
 * TODO: Provide documentation.
 *
 * @param value The value to be aligned. Should be not negative.
 * @param al    Should be positive.
 * @return      Aligned value.
 */
fun iALIGN(value: Int, al: Int): Int {
    return (value + al - 1) / al * al
}

/**
 * TODO: Provide documentation.
 *
 * @param value The value to be wrapped.
 * @param minv  Should be less than [wrap].
 * @param wrap
 * @return      Wrapped value.
 */
fun iWRAP(value: Short, minv: Short, wrap: Short): Short {
    Tracer.check(minv < wrap)

    if (value < minv) {
        return (value + wrap - minv).toShort()
    }
    return if (value >= wrap) {
        (value - wrap + minv).toShort()
    } else {
        value
    }
}
