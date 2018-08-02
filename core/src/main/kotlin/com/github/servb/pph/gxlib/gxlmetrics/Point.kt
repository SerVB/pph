package com.github.servb.pph.gxlib.gxlmetrics

/** Immutable (constant) Point view. */
interface Pointc {
    val x: Int
    val y: Int

    /** TODO: Provide tests, provide documentation. */
    fun GetSqDelta(other: Pointc) = maxOf(Math.abs(other.x - x), Math.abs(other.y - y))

    /** TODO: Provide tests, provide documentation. */
    fun GetDelta(other: Pointc): Int {
        val dx = other.x - x
        val dy = other.y - y

        return Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }

    operator fun plus(other: Pointc): Pointc = Point(x + other.x, y + other.y)
    operator fun minus(other: Pointc): Pointc = Point(x - other.x, y - other.y)
    operator fun plus(offs: Int): Pointc = Point(x + offs, y + offs)
    operator fun minus(offs: Int): Pointc = Point(x - offs, y - offs)

    fun toPointc(): Pointc = Point(this)
    fun toPoint() = Point(this)
}

data class Point(override var x: Int, override var y: Int) : Pointc {
    constructor() : this(0, 0)

    constructor(other: Pointc) : this(other.x, other.y)

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    operator fun plusAssign(other: Pointc) {
        x += other.x
        y += other.y
    }

    operator fun minusAssign(other: Pointc) {
        x -= other.x
        y -= other.y
    }

    operator fun plusAssign(siz: Sizec) {
        x += siz.w
        y += siz.h
    }

    operator fun minusAssign(siz: Sizec) {
        x -= siz.w
        y -= siz.h
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
