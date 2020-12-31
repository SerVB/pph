package com.github.servb.pph.gxlib.gxlmetrics

/** Immutable (constant) Size view. */
@ExperimentalUnsignedTypes
interface Sizec {
    val w: UInt
    val h: UInt

    /**
     * Returns the aspect ratio width/height.
     *
     * @return The aspect ratio.
     */
    // TODO: Remove toLong
    fun GetAspectRatio() = w.toLong().toFloat() / h.toLong().toFloat()

    /**
     * Checks if the size is equal to zero.
     *
     * @return The result of the check.
     */
    fun IsZero() = w == 0u && h == 0u

    operator fun plus(other: Sizec): Sizec = Size(w + other.w, h + other.h)
    operator fun minus(other: Sizec): Sizec = Size(w - other.w, h - other.h)
    operator fun plus(offs: UInt): Sizec = Size(w + offs, h + offs)
    operator fun minus(offs: UInt): Sizec = Size(w - offs, h - offs)

    fun toSizec(): Sizec = Size(this)
    fun toSize() = Size(this)
}

@ExperimentalUnsignedTypes
data class Size(override var w: UInt, override var h: UInt) : Sizec {
    constructor() : this(0u, 0u)

    constructor(other: Sizec) : this(other.w, other.h)

    operator fun plus(other: Size) = Size(w + other.w, h + other.h)
    operator fun minus(other: Size) = Size(w - other.w, h - other.h)

    /**
     * Inflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #InflateSize instead.", ReplaceWith("InflateSize(value)"))
    operator fun plusAssign(value: UInt) = InflateSize(value)

    /**
     * Deflates the size by the value.
     *
     * @param value The value.
     */
    @Deprecated("Use #DeflateSize instead.", ReplaceWith("DeflateSize(value)"))
    operator fun minusAssign(value: UInt) = DeflateSize(value)

    /**
     * Inflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    fun InflateSize(w_offs: UInt, h_offs: UInt) {
        w += w_offs
        h += h_offs
    }

    /**
     * Inflates the size by the value.
     *
     * @param offs The value.
     */
    fun InflateSize(offs: UInt) = InflateSize(offs, offs)

    /**
     * Deflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    fun DeflateSize(w_offs: UInt, h_offs: UInt) {
        w -= w_offs
        h -= h_offs
    }

    /**
     * Deflates the size by the value.
     *
     * @param offs The value.
     */
    fun DeflateSize(offs: UInt) = DeflateSize(offs, offs)

    /** Sets width and height to zero.  */
    fun Zero() {
        h = 0u
        w = 0u
    }
}
