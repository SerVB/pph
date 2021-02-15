package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.gxlib.iRandTable
import com.soywiz.korma.geom.IPointInt

const val EMAP_FILE_HDR_KEY: UInt = 0x76235278u
const val EMAP_FILE_VERSION: UInt = 0x19u

val GMAP_FILE_HDR_KEY: UInt =
    ('G'.toInt() or ('M'.toInt() shl 8) or ('A'.toInt() shl 16) or ('P'.toInt() shl 24)).toUInt()
const val GMAP_FILE_VERSION: UShort = 0x39u

val GOBJ_FILE_HDR_KEY: UInt =
    ('G'.toInt() or ('O'.toInt() shl 8) or ('B'.toInt() shl 16) or ('J'.toInt() shl 24)).toUInt()
const val GOBJ_FILE_VERSION: UShort = 0x07u

val GFNT_FILE_HDR_KEY: UInt =
    ('G'.toInt() or ('F'.toInt() shl 8) or ('N'.toInt() shl 16) or ('T'.toInt() shl 24)).toUInt()
const val GFNT_FILE_VERSION: UShort = 0x01u

const val RANDOM_QUANTITY = 0
const val RAND_VAL = -1

fun CalcCellSeqGame(pnt: IPointInt, maxv: Int): Int {
    var result = pnt.x
    result += (pnt.y shl 16).inv()
    result = result xor (pnt.x shr 5)
    result += pnt.y shl 3
    result = result xor (pnt.x shr 13)
    result += (pnt.y shl 9).inv()
    result = result xor (result shr 17)

    val idx = result xor (result shr 8) xor (result shr 16) and 255
    result = iRandTable[idx].toInt()

    return result % maxv
}

fun CalcCellSeqGameInEditor(pnt: IPointInt, maxv: Int): Int {
    TODO("Uncomment when editor will be coded")
//    int result = pnt.x;
//    result += ~(pnt.y << 16);
//    result ^= (pnt.x >> 5);
//    result += (pnt.y << 3);
//    result ^= (pnt.x >> 13);
//    result += ~(pnt.y << 9);
//    result ^= (result >> 17);
//
//    final int idx = (result ^ (result >> 8) ^ (result >> 16)) & 255;
//    result = iTables.crc32[idx];  // <==
//
//    return result % maxv;
}

const val DEF_HERO_SCOUTING = 4
const val DEF_HERO_MYSTICISM = 1
const val DEF_HERO_MOVES = 60

private val MINERAL_EXCH_RATE = listOf(1, 250, 250, 500, 500, 500, 500)

fun MineralExchRate(from: MineralType, to: MineralType, mlvl: Int): FractionCoeff {
    val fromIdx = from.v
    val toIdx = to.v

    return FractionCoeff(
            MINERAL_EXCH_RATE[fromIdx] * (mlvl + 1),
            10 * 2 * MINERAL_EXCH_RATE[toIdx]
    )
}

val MINERALS_DIVIDER = listOf(1000, 2, 2, 1, 1, 1, 1)

private infix fun Int.x(other: Int): IPointInt = IPointInt(this, other)

val HERO_FLAG_ANCHOR = listOf(
    4 x 7, 4 x 5, 4 x 4, 4 x 5, 4 x 7, 4 x 6, 4 x 4, 4 x 5, 4 x 6,
    8 x 7, 9 x 7, 8 x 7, 9 x 8, 10 x 8, 10 x 7, 9 x 7, 8 x 7, 7 x 7,
    11 x 8, 12 x 8, 11 x 8, 10 x 8, 10 x 9, 9 x 9, 10 x 8, 11 x 8, 10 x 8,
    13 x 7, 14 x 7, 13 x 8, 11 x 9, 13 x 9, 14 x 10, 14 x 9, 14 x 8, 12 x 7,
    32 x 8, 32 x 9, 32 x 10, 32 x 8, 32 x 9, 32 x 10, 32 x 11, 32 x 8, 32 x 9,
    20 x 7, 19 x 7, 20 x 8, 19 x 9, 20 x 9, 19 x 10, 19 x 9, 19 x 8, 22 x 7,
    22 x 8, 21 x 8, 22 x 8, 23 x 8, 23 x 9, 24 x 9, 23 x 8, 22 x 8, 23 x 8,
    25 x 7, 24 x 7, 25 x 7, 24 x 8, 23 x 8, 23 x 7, 24 x 7, 25 x 7, 26 x 7,
)
