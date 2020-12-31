package com.github.servb.pph.pheroes.common

data class SpannedMap(val shape: Shape = Shape.SQUARE, val radius: Int = 1) {
    enum class Shape {
        CIRCLE,
        SQUARE,
    }

    data class Span(val ypos: Short, val begin: Short, val end: Short) {
        constructor(ypos: Int, begin: Int, end: Int) : this(
                ypos = ypos.toShort(),
                begin = begin.toShort(),
                end = end.toShort()
        )
    }

    init {
        check(radius >= 0)
    }

    private val spanList: List<Span> = when (shape) {
        Shape.CIRCLE -> makeCircleSpan(radius)
        Shape.SQUARE -> makeSquareSpan(radius)
    }

    private fun makeCircleSpan(radius: Int): List<Span> {
        val spans = mutableMapOf<Int, Span>()

        var cx = 0
        var cy = radius
        var df = 1 - radius
        var d_e = 3
        var d_se = -2 * radius + 5

        do {    // Filled circle
            spans[radius - cx] = Span(-cx, -cy, cy)
            if (cx != 0) {
                spans[radius + cx] = Span(cx, -cy, cy)
            }
            if (df < 0) {
                df += d_e
                d_e += 2
                d_se += 2
            } else {
                if (cx != cy) {
                    spans[radius - cy] = Span(-cy, -cx, cx)
                    if (cy != 0) {
                        spans[radius + cy] = Span(cy, -cx, cx)
                    }
                }
                df += d_se
                d_e += 2
                d_se += 4
                cy--
            }
            cx++
        } while (cx <= cy)

        val count = radius * 2 + 1
        return (0 until count).map { spans[it] ?: Span(0, 0, 0) }
    }

    private fun makeSquareSpan(radius: Int): List<Span> {
        val count = radius * 2 + 1
        return (0 until count).map {
            Span(
                    ypos = -radius + it,
                    begin = -radius,
                    end = radius
            )
        }
    }

    val spanLinesCount: Int get() = spanList.size

    operator fun get(idx: Int): Span {
        check(idx in spanList.indices)

        return spanList[idx]
    }
}
