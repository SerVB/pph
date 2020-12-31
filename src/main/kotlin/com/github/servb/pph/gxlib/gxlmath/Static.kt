package com.github.servb.pph.gxlib.gxlmath

const val PI = 3.1415926535897932384626433832795f

@ExperimentalUnsignedTypes
fun int_sqrt(n: UInt): UInt {
    @Suppress("NAME_SHADOWING") var n = n
    var root = 0u
    var tval: UInt

    for (i in 15 downTo 0) {
        tval = root + (1u shl i)

        if (n >= tval shl i) {
            n -= tval shl i
            root = root or (2u shl i)
        }
    }

    return root shr 1
}
