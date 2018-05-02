/*
 * Copyright 2018 SerVB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.servb.pph.gxlib.gxlmetrics

interface IConstPoint {
    val x: Int
    val y: Int

    operator fun plus(pos: IConstPoint): IPoint = Point(x + pos.x, y + pos.y)
    operator fun minus(pos: IConstPoint): IPoint = Point(x - pos.x, y - pos.y)
    operator fun plus(offs: Int): IPoint = Point(x + offs, y + offs)
    operator fun minus(offs: Int): IPoint = Point(x - offs, y - offs)

    fun GetSqDelta(pnt: IConstPoint): Int = Math.max(Math.abs(pnt.x - x), Math.abs(pnt.y - y))

    fun GetDelta(pnt: IConstPoint): Int {
        val dx = pnt.x - x
        val dy = pnt.y - y

        return Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }

    fun equals(pos: IConstPoint): Boolean = x == pos.x && y == pos.x
}

interface IPoint : IConstPoint {
    override var x: Int
    override var y: Int

    operator fun plusAssign(pos: IConstPoint) {
        x += pos.x
        y += pos.y
    }

    operator fun minusAssign(pos: IConstPoint) {
        x -= pos.x
        y -= pos.y
    }

    operator fun plusAssign(siz: IConstSize) {
        x += siz.w.v
        y += siz.h.v
    }

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

class ConstPoint : IConstPoint {
    override val x: Int
    override val y: Int

    constructor() {
        this.x = 0
        this.y = 0
    }

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    constructor(other: IConstPoint) {
        this.x = other.x
        this.y = other.y
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    override fun hashCode(): Int {
        var hash = 7
        hash = 47 * hash + this.x
        hash = 47 * hash + this.y
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val other = other as ConstPoint?
        return if (this.x != other!!.x) {
            false
        } else this.y == other.y
    }
    //</editor-fold>

    override fun toString(): String = "ConstPoint{$x, $y}"
}

class Point : IPoint {
    override var x: Int
    override var y: Int

    constructor() {
        this.x = 0
        this.y = 0
    }

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    constructor(other: IConstPoint) {
        this.x = other.x
        this.y = other.y
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    override fun hashCode(): Int {
        var hash = 7
        hash = 47 * hash + this.x
        hash = 47 * hash + this.y
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val other = other as ConstPoint?
        return if (this.x != other!!.x) {
            false
        } else this.y == other.y
    }
    //</editor-fold>

    override fun toString(): String = "Point{$x, $y}"
}
