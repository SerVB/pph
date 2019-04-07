package com.github.servb.pph.pheroes.common

import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.gxlmetrics.Pointc
import com.github.servb.pph.gxlib.gxlmetrics.Size

// cf - Cell factor of isometric cell (cf = CellWidth/ATOM_WIDTH)

@ExperimentalUnsignedTypes
const val ATOM_WIDTH = 8u
@ExperimentalUnsignedTypes
const val ATOM_HEIGHT = 3u

@ExperimentalUnsignedTypes
data class IsoMetric(private val cellFactor: UInt) {
    val cellWidth: UInt get() = getCellWidth(cellFactor)
    val cellHeight: UInt get() = getCellHeight(cellFactor)

    val cellSize: Size get() = Size(cellWidth, cellHeight)

    val cellStepX: UInt get() = cellWidth / 2u
    val cellStepY: UInt get() = (cellHeight + 1u) / 2u

    fun getScreenOffset(cellOffset: Pointc): Point = Point(
            (cellOffset.x - cellOffset.y) * cellStepX.toInt(),
            (cellOffset.x + cellOffset.y) * cellStepY.toInt()
    )

    fun map2Screen(pos: Pointc): Point {
        val n = cellFactor.toInt() * 2

        return Point(
                2 * n * (pos.x - pos.y),
                n * (pos.x + pos.y)
        )
    }

    fun screen2Map(pos: Pointc): Point {
        val n = 2 * cellFactor.toInt()
        val coef = 4 * n

        val n2 = n * 2
        val n4 = n * 4

        val px = coef * pos.x
        val py = coef * (pos.y - cellHeight.toInt() / 2)

        val px2 = px + if (px / 2 <= py) n4 else 0

        var nx = px / n4 + py / n2
        var ny = -px2 / n4 + py / n2

        nx += if (nx > 0) n2 else -n2
        ny += if (ny > 0) n2 else -n2

        return Point(nx / coef, ny / coef)
    }

    companion object {
        fun getCellWidth(cellFactor: UInt) = cellFactor * ATOM_WIDTH
        fun getCellHeight(cellFactor: UInt) = (getCellWidth(cellFactor) - 1u) / 2u
    }
}
