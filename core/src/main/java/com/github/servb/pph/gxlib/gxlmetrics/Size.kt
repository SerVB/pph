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

import unsigned.Uint
import java.lang.IllegalArgumentException

interface IConstSize {
    val w: Uint
    val h: Uint

    operator fun plus(other: IConstSize): ISize = Size(w + other.w, h + other.h)
    operator fun plus(other: Uint): ISize = Size(w + other, h + other)
    operator fun minus(other: Uint): ISize = Size(w - other, h - other)

    fun equals(other: IConstSize): Boolean = w == other.w && h == other.h

    /**
     * Returns the aspect ratio width/height.
     *
     * @return The aspect ratio.
     */
    fun GetAspectRatio(): Float = w.toFloat() / h.toFloat()

    /**
     * Checks if the size is equal to zero.
     *
     * @return The result of the check.
     */
    fun IsZero(): Boolean = w.v == 0 && h.v == 0
}

interface ISize : IConstSize {
    override var w: Uint
    override var h: Uint

    /**
     * Inflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #InflateSize instead.")
    operator fun plusAssign(value: Uint) {
        w += value
        h += value
    }

    /**
     * Deflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #DeflateSize instead.")
    operator fun minusAssign(value: Uint) {
        w -= value
        h -= value
    }

    /**
     * Inflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    fun InflateSize(w_offs: Uint, h_offs: Uint) {
        w += w_offs
        h += h_offs
    }

    /**
     * Inflates the size by the value.
     *
     * @param offs The value.
     */
    fun InflateSize(offs: Uint) = InflateSize(offs, offs)

    /**
     * Deflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    fun DeflateSize(w_offs: Uint, h_offs: Uint) {
        w -= w_offs
        h -= h_offs
    }

    /**
     * Deflates the size by the value.
     *
     * @param offs The value.
     */
    fun DeflateSize(offs: Uint) = DeflateSize(offs, offs)

    /** Sets width and height to zero.  */
    fun Zero()  {
        h = Uint(0)
        w = Uint(0)
    }
}

class ConstSize : IConstSize {
    override val w: Uint
    override val h: Uint

    constructor(w: Uint, h: Uint) {
        this.w = w
        this.h = h
    }

    constructor(w: Int, h: Int) {
        if (w < 0) {
            throw IllegalArgumentException("The width can't be negative!")
        }
        if (h < 0) {
            throw IllegalArgumentException("The height can't be negative!")
        }
        this.w = Uint(w)
        this.h = Uint(h)
    }

    constructor(other: IConstSize) {
        this.w = other.w
        this.h = other.h
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    override fun hashCode(): Int {
        var hash = 7
        hash = 53 * hash + this.w.v
        hash = 53 * hash + this.h.v
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
        val other = other as ConstSize?
        return if (this.w != other!!.w) {
            false
        } else this.h == other.h
    }
    //</editor-fold>

    override fun toString(): String = "ConstSize{$w, $h}"
}

class Size : ISize {
    override var w: Uint
    override var h: Uint

    constructor(w: Uint, h: Uint) {
        this.w = w
        this.h = h
    }

    constructor(other: IConstSize) {
        this.w = other.w
        this.h = other.h
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    override fun hashCode(): Int {
        var hash = 3
        hash = 53 * hash + this.w.v
        hash = 53 * hash + this.h.v
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
        val other = other as Size?
        return if (this.w != other!!.w) {
            false
        } else this.h == other.h
    }
    //</editor-fold>

    override fun toString(): String = "Size{$w, $h}"
}
