package com.github.servb.pph.pheroes.common

import com.github.servb.pph.util.SizeT
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.PointInt
import com.soywiz.korma.geom.SizeInt

// cf - Cell factor of isometric cell (cf = CellWidth/ATOM_WIDTH)

const val ATOM_WIDTH: SizeT = 8
const val ATOM_HEIGHT: SizeT = 3

data class IsoMetric(private val cellFactor: SizeT) {

    val cellWidth: SizeT get() = getCellWidth(cellFactor)
    val cellHeight: SizeT get() = getCellHeight(cellFactor)

    val cellSize: SizeInt get() = SizeInt(cellWidth, cellHeight)

    val cellStepX: SizeT get() = cellWidth / 2
    val cellStepY: SizeT get() = (cellHeight + 1) / 2

    fun getScreenOffset(cellOffset: IPointInt): PointInt = PointInt(
        (cellOffset.x - cellOffset.y) * cellStepX,
        (cellOffset.x + cellOffset.y) * cellStepY,
    )

    fun map2Screen(pos: IPointInt): PointInt {
        val n = cellFactor * 2

        return PointInt(
            2 * n * (pos.x - pos.y),
            n * (pos.x + pos.y)
        )
    }

    fun screen2Map(pos: IPointInt): PointInt {
        val n = 2 * cellFactor
        val coef = 4 * n

        val n2 = n * 2
        val n4 = n * 4

        val px = coef * pos.x
        val py = coef * (pos.y - cellHeight / 2)

        val px2 = px + if (px / 2 <= py) n4 else 0

        var nx = px / n4 + py / n2
        var ny = -px2 / n4 + py / n2

        nx += if (nx > 0) n2 else -n2
        ny += if (ny > 0) n2 else -n2

        return PointInt(nx / coef, ny / coef)
    }

    companion object {

        fun getCellWidth(cellFactor: SizeT) = cellFactor * ATOM_WIDTH
        fun getCellHeight(cellFactor: SizeT) = (getCellWidth(cellFactor) - 1) / 2
    }
}
