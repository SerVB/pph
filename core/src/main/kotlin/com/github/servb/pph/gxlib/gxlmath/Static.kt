package com.github.servb.pph.gxlib.gxlmath

const val PI = 3.1415926535897932384626433832795f

fun int_sqrt(n: Int): Int {
    var n = n
    var root = 0
    var tval: Int

    for (i in 15 downTo 0) {
        tval = root + (1 shl i)

        if (n >= tval shl i) {
            n -= tval shl i
            root = root or (2 shl i)
        }
    }

    return root shr 1
}
