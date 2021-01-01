package com.github.servb.pph.util

import com.soywiz.korma.geom.*
import com.soywiz.korma.geom.shape.Shape2d

typealias SizeT = Int  // in C++ sources, these types are mostly unsigned

fun IRectangleInt.asSize(): ISizeInt = object : ISizeInt {

    override val width: Int get() = this@asSize.width
    override val height: Int get() = this@asSize.height
}

fun IRectangleInt.asPoint(): IPointInt = object : IPointInt {

    override val x: Int get() = this@asPoint.x
    override val y: Int get() = this@asPoint.y
}

fun ISizeInt.asRectangle(): IRectangleInt = object : IRectangleInt {

    override val x: Int get() = 0
    override val y: Int get() = 0
    override val width: Int get() = this@asRectangle.width
    override val height: Int get() = this@asRectangle.height
}

fun Rectangle.inflate(offsets: Double) = inflate(offsets, offsets)
fun Rectangle.inflate(offsets: Float) = inflate(offsets.toDouble())
fun Rectangle.inflate(offsets: Int) = inflate(offsets.toDouble())

// as IRectangle.toShape()
fun IRectangleInt.toShape() = Shape2d.Rectangle(x, y, width, height)

// remove from Rectangle, create the following:
val IRectangle.topLeft get() = Point(left, top)
val IRectangle.topRight get() = Point(right, top)
val IRectangle.bottomLeft get() = Point(left, bottom)
val IRectangle.bottomRight get() = Point(right, bottom)
val IRectangleInt.topLeft get() = PointInt(left, top)
val IRectangleInt.topRight get() = PointInt(right, top)
val IRectangleInt.bottomLeft get() = PointInt(left, bottom)
val IRectangleInt.bottomRight get() = PointInt(right, bottom)

// new:
val IRectangle.x2: Double get() = x + width - 1
val IRectangle.y2: Double get() = y + height - 1
val IRectangleInt.x2: Int get() = x + width - 1
val IRectangleInt.y2: Int get() = y + height - 1

val IRectangle.topLeft2 get() = Point(x, y)
val IRectangle.topRight2 get() = Point(x2, y)
val IRectangle.bottomLeft2 get() = Point(x, y2)
val IRectangle.bottomRight2 get() = Point(x2, y2)
val IRectangleInt.topLeft2 get() = PointInt(x, y)
val IRectangleInt.topRight2 get() = PointInt(x2, y)
val IRectangleInt.bottomLeft2 get() = PointInt(x, y2)
val IRectangleInt.bottomRight2 get() = PointInt(x2, y2)

operator fun RectangleInt.Companion.invoke(other: IRectangleInt) =
    RectangleInt(other.x, other.y, other.width, other.height)

// as IPointInt.Companion.invoke()
fun ISizeInt(width: Int, height: Int): ISizeInt = SizeInt(width, height)

// change Iterable<Rectangle> to Iterable<IRectangle>
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
