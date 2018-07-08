package com.github.servb.pph.gxlib.gxlmetrics

interface XYHolder {
    val x: Int
    val y: Int

    /** TODO: Provide tests, provide documentation. */
    fun GetSqDelta(other: XYHolder) = Math.max(Math.abs(other.x - x), Math.abs(other.y - y))

    /** TODO: Provide tests, provide documentation. */
    fun GetDelta(other: XYHolder): Int {
        val dx = other.x - x
        val dy = other.y - y

        return Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }
}

data class Point(override val x: Int, override val y: Int) : XYHolder {
    constructor() : this(0, 0)

    constructor(other: XYHolder) : this(other.x, other.y)

    operator fun plus(other: XYHolder) = Point(x + other.x, y + other.y)
    operator fun minus(other: XYHolder) = Point(x - other.x, y - other.y)
    operator fun plus(offs: Int) = Point(x + offs, y + offs)
    operator fun minus(offs: Int) = Point(x - offs, y - offs)
}

data class MutablePoint(override var x: Int, override var y: Int) : XYHolder {
    constructor() : this(0, 0)

    constructor(other: XYHolder) : this(other.x, other.y)

    operator fun plus(other: XYHolder) = MutablePoint(x + other.x, y + other.y)
    operator fun minus(other: XYHolder) = MutablePoint(x - other.x, y - other.y)
    operator fun plus(offs: Int) = MutablePoint(x + offs, y + offs)
    operator fun minus(offs: Int) = MutablePoint(x - offs, y - offs)

    operator fun plusAssign(other: XYHolder) {
        x += other.x
        y += other.y
    }

    operator fun minusAssign(other: XYHolder) {
        x -= other.x
        y -= other.y
    }

    /** TODO: Provide tests. */
    operator fun plusAssign(siz: IConstSize) {
        x += siz.w.v
        y += siz.h.v
    }

    /** TODO: Provide tests. */
    operator fun minusAssign(siz: IConstSize) {
        x -= siz.w.v
        y -= siz.h.v
    }

    operator fun plusAssign(offs: Int) {
        x += offs
        y += offs
    }

    operator fun minusAssign(offs: Int) {
        x -= offs
        y -= offs
    }

    fun MoveX(offset_x: Int) {
        x += offset_x
    }

    fun MoveY(offset_y: Int) {
        y += offset_y
    }

    fun Move(offset_x: Int, offset_y: Int) {
        x += offset_x
        y += offset_y
    }
}
