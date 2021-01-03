package com.github.servb.pph.util

import com.soywiz.korma.geom.*

typealias SizeT = Int  // in C++ sources, these types are mostly unsigned
typealias SizeTArray = IntArray

fun IRectangleInt.asSize(): ISizeInt = object : ISizeInt {

    override val width: Int get() = this@asSize.width
    override val height: Int get() = this@asSize.height
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

// https://github.com/korlibs/korma/pull/46
fun Rectangle.inflate(offsets: Double) = inflate(offsets, offsets)
fun Rectangle.inflate(offsets: Float) = inflate(offsets.toDouble())
fun Rectangle.inflate(offsets: Int) = inflate(offsets.toDouble())

// remove from Rectangle, create the following (after https://github.com/korlibs/korma/pull/47):
val IRectangle.topLeft get() = Point(left, top)
val IRectangle.topRight get() = Point(right, top)
val IRectangle.bottomLeft get() = Point(left, bottom)
val IRectangle.bottomRight get() = Point(right, bottom)

// https://github.com/korlibs/korma/pull/47
val IRectangleInt.topLeft get() = PointInt(left, top)
val IRectangleInt.topRight get() = PointInt(right, top)
val IRectangleInt.bottomLeft get() = PointInt(left, bottom)
val IRectangleInt.bottomRight get() = PointInt(right, bottom)
val IRectangleInt.x2: Int get() = x + width - 1
val IRectangleInt.y2: Int get() = y + height - 1
val IRectangleInt.topLeft2 get() = PointInt(x, y)
val IRectangleInt.topRight2 get() = PointInt(x2, y)
val IRectangleInt.bottomLeft2 get() = PointInt(x, y2)
val IRectangleInt.bottomRight2 get() = PointInt(x2, y2)

operator fun RectangleInt.Companion.invoke(other: IRectangleInt) =
    RectangleInt(other.x, other.y, other.width, other.height)

// new:
//operator fun IRectangleInt.contains(that: Point) = contains(that.x, that.y)  // this is not needed, remove from Rectangle too
operator fun IRectangleInt.contains(that: IPoint) = contains(that.x, that.y)
fun IRectangleInt.contains(x: Double, y: Double) = (x >= left && x < right) && (y >= top && y < bottom)
fun IRectangleInt.contains(x: Float, y: Float) = contains(x.toDouble(), y.toDouble())
fun IRectangleInt.contains(x: Int, y: Int) = contains(x.toDouble(), y.toDouble())

// as ISizeInt.Companion.invoke()
fun ISizeInt(width: Int, height: Int): ISizeInt = SizeInt(width, height)

// as PointInt.Companion.invoke()
fun PointInt(other: IPointInt): PointInt = PointInt(other.x, other.y)

// https://github.com/korlibs/korma/pull/49
fun IRectangleInt.anchor(ax: Double, ay: Double): PointInt =
    PointInt((x + width * ax).toInt(), (y + height * ay).toInt())

inline fun IRectangleInt.anchor(ax: Number, ay: Number): PointInt = anchor(ax.toDouble(), ay.toDouble())
val IRectangleInt.center get() = anchor(0.5, 0.5)

// https://github.com/korlibs/korma/pull/48
fun Iterable<IRectangle>.bounds(target: Rectangle = Rectangle()): Rectangle {
    var first = true
    var left = 0.0
    var right = 0.0
    var top = 0.0
    var bottom = 0.0
    for (r in this) {
        if (first) {
            left = r.left
            right = r.right
            top = r.top
            bottom = r.bottom
            first = false
        } else {
            left = kotlin.math.min(left, r.left)
            right = kotlin.math.max(right, r.right)
            top = kotlin.math.min(top, r.top)
            bottom = kotlin.math.max(bottom, r.bottom)
        }
    }
    return target.setBounds(left, top, right, bottom)
}
