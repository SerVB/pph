package com.github.servb.pph.iolib.xe

import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korma.geom.PointInt
import com.soywiz.korma.geom.RectangleInt

fun FindSolidArea(dib: Bitmap32): RectangleInt {
    val ltpt = PointInt(dib.width - 1, dib.height - 1)
    val rbpt = PointInt(0, 0)

    repeat(dib.height) { yy ->
        repeat(dib.width) { xx ->
            if (dib[xx, yy].a != 0) {
                if (xx < ltpt.x) {
                    ltpt.x = xx
                }
                if (yy < ltpt.y) {
                    ltpt.y = yy
                }
                if (xx > rbpt.x) {
                    rbpt.x = xx
                }
                if (yy > rbpt.y) {
                    rbpt.y = yy
                }
            }
        }
    }

    return RectangleInt(ltpt.x, ltpt.y, rbpt.x - ltpt.x + 1, rbpt.y - ltpt.y + 1)
}
