package com.github.servb.pph.gxlib.gxlmetrics

import unsigned.Uint
import unsigned.ui

interface WHHolder {
    val w: Uint
    val h: Uint

    /**
     * Returns the aspect ratio width/height.
     *
     * @return The aspect ratio.
     */
    fun GetAspectRatio() = w.toFloat() / h.toFloat()

    /**
     * Checks if the size is equal to zero.
     *
     * @return The result of the check.
     */
    fun IsZero() = w.v == 0 && h.v == 0
}

data class Size(override val w: Uint, override val h: Uint) : WHHolder {
    constructor() : this(0.ui, 0.ui)

    constructor(other: WHHolder) : this(Uint(other.w), Uint(other.h))

    operator fun plus(other: WHHolder) = Size(w + other.w, h + other.h)
    operator fun minus(other: WHHolder) = Size(w - other.w, h - other.h)
    operator fun plus(offs: Uint) = Size(w + offs, h + offs)
    operator fun minus(offs: Uint) = Size(w - offs, h - offs)
}

data class MutableSize(override var w: Uint, override var h: Uint) : WHHolder {
    constructor() : this(0.ui, 0.ui)

    constructor(other: WHHolder) : this(Uint(other.w), Uint(other.h))

    operator fun plus(other: WHHolder) = MutableSize(w + other.w, h + other.h)
    operator fun minus(other: WHHolder) = MutableSize(w - other.w, h - other.h)
    operator fun plus(offs: Uint) = MutableSize(w + offs, h + offs)
    operator fun minus(offs: Uint) = MutableSize(w - offs, h - offs)

    /**
     * Inflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #InflateSize instead.", ReplaceWith("InflateSize(value)"))
    operator fun plusAssign(value: Uint) {
        InflateSize(value)
    }

    /**
     * Deflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #DeflateSize instead.", ReplaceWith("DeflateSize(value)"))
    operator fun minusAssign(value: Uint) {
        DeflateSize(value)
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
    fun Zero() {
        h = 0.ui
        w = 0.ui
    }
}
