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

operator fun RectangleInt.Companion.invoke(other: IRectangleInt) =
    RectangleInt(other.x, other.y, other.width, other.height)

// new:
//operator fun IRectangleInt.contains(that: Point) = contains(that.x, that.y)  // this is not needed, remove from Rectangle too
operator fun IRectangleInt.contains(that: IPoint) = contains(that.x, that.y)
operator fun IRectangleInt.contains(that: IPointInt) = contains(that.x, that.y)
fun IRectangleInt.contains(x: Double, y: Double) = (x >= left && x < right) && (y >= top && y < bottom)
fun IRectangleInt.contains(x: Float, y: Float) = contains(x.toDouble(), y.toDouble())
fun IRectangleInt.contains(x: Int, y: Int) = contains(x.toDouble(), y.toDouble())

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

// replace `(that: RectangleInt)` with this
fun RectangleInt.setTo(that: IRectangleInt) = setTo(that.x, that.y, that.width, that.height)

// replace `(that: SizeInt)` with this
fun SizeInt.setTo(that: ISizeInt) = setTo(that.width, that.height)

// as ISizeInt.Companion.invoke()
fun ISizeInt(width: Int, height: Int): ISizeInt = SizeInt(width, height)

// as PointInt.Companion.invoke()
fun PointInt(other: IPointInt): PointInt = PointInt(other.x, other.y)

// as SizeInt.Companion.invoke()
fun SizeInt(that: ISizeInt): SizeInt = SizeInt(that.width, that.height)

fun Rectangle.deflate(left: SizeT, top: SizeT, right: SizeT, bottom: SizeT) {
    x += left
    y += top
    width -= left + right
    height -= top + bottom
}
