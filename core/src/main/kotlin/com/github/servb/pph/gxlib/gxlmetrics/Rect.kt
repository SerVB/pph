package com.github.servb.pph.gxlib.gxlmetrics

/** Immutable (constant) rectangle view. */
interface Rectc : Pointc, Sizec {
    override fun plus(offs: Int): Rectc = Rect(x + offs, y + offs, w + offs, h + offs)
    override fun minus(offs: Int): Rectc = Rect(x - offs, y - offs, w - offs, h - offs)

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
    fun PtInRect(pnt: Pointc) = PtInRect(pnt.x, pnt.y)

    fun IsEmpty() = w == 0 || h == 0

    operator fun plus(rect: Rectc): Rectc {
        val rc = Rect(this)
        rc += rect
        return Rect(rc)
    }

    fun toRectc(): Rectc = Rect(this)
    fun toRect(): Rect = Rect(this)
}

data class Rect(override var x: Int, override var y: Int, override var w: Int, override var h: Int) : Rectc {
    constructor() : this(0, 0, 0, 0)

    constructor(point: Pointc, size: Sizec) : this(point.x, point.y, size.w, size.h)

    constructor(other: Rectc) : this(other.x, other.y, other.w, other.h)

    constructor(point: Pointc) : this(point.x, point.y, 0, 0)

    constructor(size: Sizec) : this(0, 0, size.w, size.h)

    constructor(p1: Pointc, p2: Pointc) : this(42, 42, 42, 42) {
        val minX = minOf(p1.x, p2.x)
        val minY = minOf(p1.y, p2.y)

        val maxX = maxOf(p1.x, p2.x)
        val maxY = maxOf(p1.y, p2.y)

        x = minX
        y = minY

        w = maxX - minX + 1
        h = maxY - minY + 1
    }

    fun Reset() {
        x = 0
        y = 0
        w = 0
        h = 0
    }

    operator fun plusAssign(rect: Rectc) {
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

        w = maxX - minX + 1
        h = maxY - minY + 1
    }

    fun InflateRect(left: Int, top: Int, right: Int, bottom: Int) {
        x -= left
        y -= top
        w += left + right
        h += top + bottom
    }

    fun InflateRect(x_offs: Int, y_offs: Int) = InflateRect(x_offs, y_offs, x_offs, y_offs)

    fun InflateRect(offs: Int) = InflateRect(offs, offs, offs, offs)

    fun DeflateRect(left: Int, top: Int, right: Int, bottom: Int) {
        x += left
        y += top
        w -= left + right
        h -= top + bottom
    }

    fun DeflateRect(x_offs: Int, y_offs: Int) = DeflateRect(x_offs, y_offs, x_offs, y_offs)

    fun DeflateRect(offs: Int) = DeflateRect(offs, offs, offs, offs)

    operator fun plus(rect: Rect): Rect {
        val rc = Rect(this)
        rc += rect
        return Rect(rc)
    }
}
