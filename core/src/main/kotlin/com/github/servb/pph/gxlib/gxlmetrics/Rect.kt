package com.github.servb.pph.gxlib.gxlmetrics

import unsigned.Uint

interface IConstRect : IConstPoint, IConstSize {
    val x1: Int get() = x
    val y1: Int get() = y
    val x2: Int get() = x + w.v - 1
    val y2: Int get() = y + h.v - 1

    fun Center(): IPoint = Point(x + w.v / 2, y + h.v / 2)
    fun TopRight(): IPoint = Point(x2, y)
    fun TopLeft(): IPoint = Point(x, y)
    fun BottomRight(): IPoint = Point(x2, y2)
    fun BottomLeft(): IPoint = Point(x, y2)

    fun Point(): IPoint = Point(x, y)
    fun Size(): ISize = Size(w, h)

    fun PtInRect(_x: Int, _y: Int): Boolean = (_x in x until (x + w.v)) && (_y in y until (y + h.v))
    fun PtInRect(pnt: IConstPoint): Boolean = PtInRect(pnt.x, pnt.y)

    fun IsEmpty(): Boolean = w.v == 0 || h.v == 0

    fun equals(rect: IConstRect): Boolean = x == rect.x && y == rect.y && w == rect.w && h == rect.h

    operator fun plus(rect: IConstRect): IRect {
        val rc = Rect(this)
        rc += rect
        return rc
    }
}

interface IRect : IConstRect, IPoint, ISize {
    fun Reset() {
        x = 0
        y = 0
        w = Uint(0)
        h = Uint(0)
    }

    operator fun plusAssign(rect: IConstRect) {
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
        w = Uint(maxX - minX + 1)
        h = Uint(maxY - minY + 1)
    }

    fun InflateRect(left: Uint, top: Uint, right: Uint, bottom: Uint) {
        x -= left.v
        y -= top.v
        w += left + right
        h += top + bottom
    }

    fun InflateRect(x_offs: Uint, y_offs: Uint) = InflateRect(x_offs, y_offs, x_offs, y_offs)

    fun InflateRect(offs: Uint) = InflateRect(offs, offs, offs, offs)

    fun DeflateRect(left: Uint, top: Uint, right: Uint, bottom: Uint) {
        x += left.v
        y += top.v
        w -= left + right
        h -= top + bottom
    }

    fun DeflateRect(x_offs: Uint, y_offs: Uint) = DeflateRect(x_offs, y_offs, x_offs, y_offs)

    fun DeflateRect(offs: Uint) = DeflateRect(offs, offs, offs, offs)
}

class ConstRect : IConstRect {
    override val x: Int
    override val y: Int
    override val w: Uint
    override val h: Uint

    constructor() {
        x = 0
        y = 0
        w = Uint(0)
        h = Uint(0)
    }

    constructor(x: Int, y: Int, w: Uint, h: Uint) {
        this.x = x
        this.y = y
        this.w = Uint(w)
        this.h = Uint(h)
    }

    constructor(point: IConstPoint, size: IConstSize) {
        x = point.x
        y = point.y
        w = Uint(size.w)
        h = Uint(size.h)
    }

    constructor(p1: IConstPoint, p2: IConstPoint) {
        val minX = minOf(p1.x, p2.x)
        val minY = minOf(p1.y, p2.y)
        val maxX = maxOf(p1.x, p2.x)
        val maxY = maxOf(p1.y, p2.y)

        x = minX
        y = minY
        w = Uint(maxX - minX + 1)
        h = Uint(maxY - minY + 1)
    }

    constructor(other: IConstRect) {
        x = other.x
        y = other.y
        w = other.w
        h = other.h
    }

    constructor(size: IConstSize) {
        x = 0
        y = 0
        w = Uint(size.w)
        h = Uint(size.h)
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConstRect

        if (x != other.x) return false
        if (y != other.y) return false
        if (w != other.w) return false
        if (h != other.h) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 5
        result = 31 * result + x
        result = 31 * result + y
        result = 31 * result + w.v
        result = 31 * result + h.v
        return result
    }
    //</editor-fold>

    override fun toString(): String = "ConstRect{$x, $y, $w, $h}"
}

class Rect : IRect {
    override var x: Int
    override var y: Int
    override var w: Uint
    override var h: Uint

    constructor() {
        x = 0
        y = 0
        w = Uint(0)
        h = Uint(0)
    }

    constructor(x: Int, y: Int, w: Uint, h: Uint) {
        this.x = x
        this.y = y
        this.w = Uint(w)
        this.h = Uint(h)
    }

    constructor(point: IConstPoint, size: IConstSize) {
        x = point.x
        y = point.y
        w = Uint(size.w)
        h = Uint(size.h)
    }

    constructor(p1: IConstPoint, p2: IConstPoint) {
        val minX = minOf(p1.x, p2.x)
        val minY = minOf(p1.y, p2.y)
        val maxX = maxOf(p1.x, p2.x)
        val maxY = maxOf(p1.y, p2.y)

        x = minX
        y = minY
        w = Uint(maxX - minX + 1)
        h = Uint(maxY - minY + 1)
    }

    constructor(other: IConstRect) {
        x = other.x
        y = other.y
        w = other.w
        h = other.h
    }

    constructor(size: IConstSize) {
        x = 0
        y = 0
        w = Uint(size.w)
        h = Uint(size.h)
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConstRect

        if (x != other.x) return false
        if (y != other.y) return false
        if (w != other.w) return false
        if (h != other.h) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 5
        result = 31 * result + x
        result = 31 * result + y
        result = 31 * result + w.v
        result = 31 * result + h.v
        return result
    }
    //</editor-fold>

    override fun toString(): String = "Rect{$x, $y, $w, $h}"
}
