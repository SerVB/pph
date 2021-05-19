package com.github.servb.pph.util

import com.soywiz.korma.geom.*

typealias SizeT = Int  // in C++ sources, these types are mostly unsigned
typealias SizeTArray = IntArray

fun IRectangleInt.asSize(): ISizeInt = object : ISizeInt {

    override val width: Int get() = this@asSize.width
    override val height: Int get() = this@asSize.height

    override fun equals(other: Any?): Boolean {
        return ISizeInt(width, height) == other
    }
}

fun IRectangleInt.asPoint(): IPointInt = object : IPointInt {

    override val x: Int get() = this@asPoint.x
    override val y: Int get() = this@asPoint.y
}

// submit after https://github.com/korlibs/korma/issues/44
fun ISizeInt.asRectangle(): IRectangleInt = object : IRectangleInt {

    override val x: Int get() = 0
    override val y: Int get() = 0
    override val width: Int get() = this@asRectangle.width
    override val height: Int get() = this@asRectangle.height
}

val IRectangleInt.isEmpty get() = width == 0 || height == 0

fun RectangleInt.setToUnion(that: IRectangleInt) {
    if (isEmpty) {
        setTo(that)
        return
    }

    val minX = minOf(that.x, x)
    val minY = minOf(that.y, y)

    val maxX = maxOf(that.x2, x2)
    val maxY = maxOf(that.y2, y2)

    x = minX
    y = minY
    width = maxX - minX + 1
    height = maxY - minY + 1
}

fun Rectangle.deflate(left: SizeT, top: SizeT, right: SizeT, bottom: SizeT) {
    x += left
    y += top
    width -= left + right
    height -= top + bottom
}
