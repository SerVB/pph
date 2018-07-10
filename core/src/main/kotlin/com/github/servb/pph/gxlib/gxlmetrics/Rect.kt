package com.github.servb.pph.gxlib.gxlmetrics

import unsigned.Uint
import unsigned.minus
import unsigned.plus
import unsigned.ui

interface XYWHHolder : XYHolder, WHHolder {
    val x1 get() = x
    val y1 get() = y
    val x2 get() = x + w - 1
    val y2 get() = y + h - 1

    fun Center() = Point(x + w / 2, y + h / 2)
    fun TopRight() = Point(x2, y1)
    fun TopLeft() = Point(x1, y1)
    fun BottomRight() = Point(x2, y2)
    fun BottomLeft() = Point(x1, y2)

    fun Point() = Point(x, y)
    fun Size() = Size(w, h)

    fun PtInRect(_x: Int, _y: Int) = (_x in x1..x2) && (_y in y1..y2)
    fun PtInRect(pnt: XYHolder) = PtInRect(pnt.x, pnt.y)

    fun IsEmpty() = w.v == 0 || h.v == 0
}

/** TODO: Make constructor. */
fun MakeRect(p1: XYHolder, p2: XYHolder): Rect {
    val minX = minOf(p1.x, p2.x)
    val minY = minOf(p1.y, p2.y)
    val maxX = maxOf(p1.x, p2.x)
    val maxY = maxOf(p1.y, p2.y)

    val x = minX
    val y = minY
    val w = maxX - minX + 1
    val h = maxY - minY + 1

    return Rect(x, y, w.ui, h.ui)
}

data class Rect(override val x: Int, override val y: Int, override val w: Uint, override val h: Uint) : XYWHHolder {
    constructor() : this(0, 0, 0.ui, 0.ui)

    constructor(point: XYHolder, size: WHHolder) : this(point.x, point.y, Uint(size.w), Uint(size.h))

    constructor(other: XYWHHolder) : this(other.x, other.y, Uint(other.w), Uint(other.h))

    constructor(size: WHHolder) : this(0, 0, Uint(size.w), Uint(size.h))

    operator fun plus(rect: XYWHHolder): Rect {
        val rc = MutableRect(this)
        rc += rect
        return Rect(rc)
    }
}

data class MutableRect(override var x: Int, override var y: Int, override var w: Uint, override var h: Uint)
        : XYWHHolder {
    constructor() : this(0, 0, 0.ui, 0.ui)

    constructor(point: XYHolder, size: WHHolder) : this(point.x, point.y, Uint(size.w), Uint(size.h))

    constructor(other: XYWHHolder) : this(other.x, other.y, Uint(other.w), Uint(other.h))

    constructor(size: WHHolder) : this(0, 0, Uint(size.w), Uint(size.h))

    operator fun plus(rect: XYWHHolder): MutableRect {
        val rc = MutableRect(this)
        rc += rect
        return MutableRect(rc)
    }

    fun Reset() {
        x = 0
        y = 0
        w = 0.ui
        h = 0.ui
    }

    operator fun plusAssign(rect: XYWHHolder) {
        if (IsEmpty()) {
            x = rect.x
            y = rect.y
            w = rect.w
            h = rect.h
        }

        val minX = minOf(rect.x, x)
        val minY = minOf(rect.y, y)

        val maxX = maxOf(rect.x2, x2)
        val maxY = maxOf(rect.y2, y2)

        x = minX
        y = minY
        w = (maxX - minX + 1).ui
        h = (maxY - minY + 1).ui
    }

    fun InflateRect(left: Uint, top: Uint, right: Uint, bottom: Uint) {
        x -= left
        y -= top
        w += left + right
        h += top + bottom
    }

    fun InflateRect(x_offs: Uint, y_offs: Uint) = InflateRect(x_offs, y_offs, x_offs, y_offs)

    fun InflateRect(offs: Uint) = InflateRect(offs, offs, offs, offs)

    fun DeflateRect(left: Uint, top: Uint, right: Uint, bottom: Uint) {
        x += left
        y += top
        w -= left + right
        h -= top + bottom
    }

    fun DeflateRect(x_offs: Uint, y_offs: Uint) = DeflateRect(x_offs, y_offs, x_offs, y_offs)

    fun DeflateRect(offs: Uint) = DeflateRect(offs, offs, offs, offs)
}
