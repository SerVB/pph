package com.github.servb.pph.gxlib.gxlmetrics

/** Immutable (constant) Size view. */
interface Sizec {
    val w: Int
    val h: Int

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
    fun IsZero() = w == 0 && h == 0

    operator fun plus(other: Sizec): Sizec = Size(w + other.w, h + other.h)
    operator fun minus(other: Sizec): Sizec = Size(w - other.w, h - other.h)
    operator fun plus(offs: Int): Sizec = Size(w + offs, h + offs)
    operator fun minus(offs: Int): Sizec = Size(w - offs, h - offs)

    fun toSizec(): Sizec = Size(this)
    fun toSize() = Size(this)
}

data class Size(override var w: Int, override var h: Int) : Sizec {
    constructor() : this(0, 0)

    constructor(other: Sizec) : this(other.w, other.h)

    operator fun plus(other: Size) = Size(w + other.w, h + other.h)
    operator fun minus(other: Size) = Size(w - other.w, h - other.h)

    /**
     * Inflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #InflateSize instead.", ReplaceWith("InflateSize(value)"))
    operator fun plusAssign(value: Int) = InflateSize(value)

    /**
     * Deflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #DeflateSize instead.", ReplaceWith("DeflateSize(value)"))
    operator fun minusAssign(value: Int) = DeflateSize(value)

    /**
     * Inflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    fun InflateSize(w_offs: Int, h_offs: Int) {
        w += w_offs
        h += h_offs
    }

    /**
     * Inflates the size by the value.
     *
     * @param offs The value.
     */
    fun InflateSize(offs: Int) = InflateSize(offs, offs)

    /**
     * Deflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    fun DeflateSize(w_offs: Int, h_offs: Int) {
        w -= w_offs
        h -= h_offs
    }

    /**
     * Deflates the size by the value.
     *
     * @param offs The value.
     */
    fun DeflateSize(offs: Int) = DeflateSize(offs, offs)

    /** Sets width and height to zero.  */
    fun Zero() {
        h = 0
        w = 0
    }
}
