package com.github.servb.pph.gxlib.gxlmetrics

import com.github.servb.pph.gxlib.gxlcommontpl.iCLAMP
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class Alignment(override val v: Int) : UniqueValueEnum {
    AlignCenter(0),
    AlignTop(0b0001),
    AlignRight(0b0010),
    AlignBottom(0b0100),
    AlignLeft(0b1000),
    AlignTopRight(AlignTop.v or AlignRight.v),
    AlignBottomRight(AlignBottom.v or AlignRight.v),
    AlignTopLeft(AlignTop.v or AlignLeft.v),
    AlignBottomLeft(AlignBottom.v or AlignLeft.v);
}

/** Returns rectangle with specified size aligned in specified dst rect. */
@ExperimentalUnsignedTypes
fun AlignRect(ss: Sizec, dr: Rectc, al: Alignment): Rect {
    val sw = ss.w
    val sh = ss.h
    val dw = dr.w
    val dh = dr.h

    // Vertical alignment
    val y = when {
        al.v and Alignment.AlignTop.v != 0 -> dr.y
        al.v and Alignment.AlignBottom.v != 0 -> dr.y + (dh - sh).toInt()
        else -> dr.y + ((dh shr 1) - (sh shr 1)).toInt()
    }

    // Horizontal alignment
    val x = when {
        al.v and Alignment.AlignLeft.v != 0 -> dr.x
        al.v and Alignment.AlignRight.v != 0 -> dr.x + (dw - sw).toInt()
        else -> dr.x + ((dw shr 1) - (sw shr 1)).toInt()
    }

    return Rect(x, y, ss.w, ss.h)
}

@ExperimentalUnsignedTypes
val cInvalidPoint: Pointc = Point(0x7fff, 0x7fff)
@ExperimentalUnsignedTypes
val cInvalidSize: Sizec = Size(0xffffu, 0xffffu)
@ExperimentalUnsignedTypes
val cInvalidRect: Rectc = Rect(0x7fff, 0x7fff, 0xffffu, 0xffffu)

fun pow2(v: Int) = v * v

@ExperimentalUnsignedTypes
fun IsLineIntersectCircle(cp: Pointc, cr: Int, p1: Pointc, p2: Pointc): Boolean {
    val x01 = p1.x - cp.x
    val y01 = p1.y - cp.y
    val x02 = p2.x - cp.x
    val y02 = p2.y - cp.y

    val dx = x02 - x01
    val dy = y02 - y01

    val a = pow2(dx) + pow2(dy)
    val b = 2 * (x01 * dx + y01 * dy)
    val c = pow2(x01) + pow2(y01) - pow2(cr)

    return when {
        -b < 0 -> c < 0
        -b < 2 * a -> 4 * a * c - pow2(b) < 0
        else -> a + b + c < 0
    }

    // Commented in sources:
    /*
    if ( !iRect(p1,p2).PtInRect(cp) ) return false;

    float dist = (( (p1.y - p2.y) * cp.x + (p2.x - p1.x) * cp.y + (p1.x * p2.y - p2.x * p1.y)) / sqrt( (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y) ));
    if (dist < 0 ) dist = -dist;

    bool res = cr > dist;
    OutputDebugString(iFormat(_T("Circle (%d, %d, %d), Line (%d, %d -> %d, %d) : %f\n"), cp.x, cp.y, cr, p1.x, p1.y, p2.x, p2.y, dist).CStr());
    return res;
    */
}

@ExperimentalUnsignedTypes
fun ClipPoint(pnt: Point, rect: Rectc): Boolean {
    pnt.x = iCLAMP(rect.x, rect.x2, pnt.x)
    pnt.y = iCLAMP(rect.x, rect.y2, pnt.y)
    return true
}

/** x2 -- int[1] */
@ExperimentalUnsignedTypes
fun ClipHLine(pnt1: Point, x2: Array<Int>, rect: Rectc): Boolean {
    if (pnt1.y < rect.y || pnt1.y > rect.y2) {
        return false
    }
    pnt1.x = iCLAMP(rect.x, rect.x2, pnt1.x)
    x2[0] = iCLAMP(rect.x, rect.x2, x2[0])
    return pnt1.x != x2[0]
}

/** y2 -- int[1] */
@ExperimentalUnsignedTypes
fun ClipVLine(pnt1: Point, y2: Array<Int>, rect: Rectc): Boolean {
    if (pnt1.x < rect.x || pnt1.x > rect.x2) {
        return false
    }
    pnt1.y = iCLAMP(rect.y, rect.y2, pnt1.y)
    y2[0] = iCLAMP(rect.y, rect.y2, y2[0])
    return pnt1.y != y2[0]
}

@ExperimentalUnsignedTypes
fun ClipLine(pnt1: Point, pnt2: Point, rect: Rectc): Boolean {
    val d = Point()

    /* CLIPCODE( pnt1, c1 ) */
    var c1 = 0u
    if (pnt1.x < rect.x1) c1 = 8u or c1
    if (pnt1.x > rect.x2) c1 = 4u or c1
    if (pnt1.y < rect.y1) c1 = 2u or c1
    if (pnt1.y > rect.y2) c1 = 1u or c1
    /* CLIPCODE( pnt2, c2 ) */
    var c2 = 0u
    if (pnt2.x < rect.x1) c2 = 8u or c2
    if (pnt2.x > rect.x2) c2 = 4u or c2
    if (pnt2.y < rect.y1) c2 = 2u or c2
    if (pnt2.y > rect.y2) c2 = 1u or c2

    while (c1 or c2 != 0u) {
        if (c1 and c2 == 0u) {
            return false
        }
        d.x = pnt2.x - pnt1.x
        d.y = pnt2.y - pnt1.y
        if (c1 != 0u) {
            if (pnt1.x < rect.x1) {
                pnt1.y += d.y * (rect.x1 - pnt1.x) / d.x
                pnt1.x = rect.x1
            } else if (pnt1.x > rect.x2) {
                pnt1.y += d.y * (rect.x2 - pnt1.x) / d.x
                pnt1.x = rect.x2
            } else if (pnt1.y < rect.y1) {
                pnt1.x += d.x * (rect.y1 - pnt1.y) / d.y
                pnt1.y = rect.y1
            } else if (pnt1.y > rect.y2) {
                pnt1.x += d.x * (rect.y2 - pnt1.y) / d.y
                pnt1.y = rect.y2
            }
            /* CLIPCODE( pnt1, c1 ) */
            c1 = 0u
            if (pnt1.x < rect.x1) c1 = 8u or c1
            if (pnt1.x > rect.x2) c1 = 4u or c1
            if (pnt1.y < rect.y1) c1 = 2u or c1
            if (pnt1.y > rect.y2) c1 = 1u or c1
        } else {
            if (pnt2.x < rect.x1) {
                pnt2.y += d.y * (rect.x1 - pnt2.x) / d.x
                pnt2.x = rect.x1
            } else if (pnt2.x > rect.x2) {
                pnt2.y += d.y * (rect.x2 - pnt2.x) / d.x
                pnt2.x = rect.x2
            } else if (pnt2.y < rect.y1) {
                pnt2.x += d.x * (rect.y1 - pnt2.y) / d.y
                pnt2.y = rect.y1
            } else if (pnt2.y > rect.y2) {
                pnt2.x += d.x * (rect.y2 - pnt2.y) / d.y
                pnt2.y = rect.y2
            }
            /* CLIPCODE( pnt2, c2 ) */
            c2 = 0u
            if (pnt2.x < rect.x1) c2 = 8u or c2
            if (pnt2.x > rect.x2) c2 = 4u or c2
            if (pnt2.y < rect.y1) c2 = 2u or c2
            if (pnt2.y > rect.y2) c2 = 1u or c2
        }
    }
    return true
}

@ExperimentalUnsignedTypes
fun IntersectRect(dst_rect: Rect, src_rect1: Rectc, src_rect2: Rectc): Boolean {
    val x1 = maxOf(src_rect1.x, src_rect2.x)
    val y1 = maxOf(src_rect1.y, src_rect2.y)
    val x2 = minOf(src_rect1.x2, src_rect2.x2) + 1
    val y2 = minOf(src_rect1.y2, src_rect2.y2) + 1

    return if (x2 - x1 > 0 && y2 - y1 > 0) {
        dst_rect.x = x1
        dst_rect.y = y1
        dst_rect.w = (x2 - x1).toUInt()
        dst_rect.h = (y2 - y1).toUInt()
        true
    } else {
        false
    }
}

@ExperimentalUnsignedTypes
fun IsIntersectRect(src_rect1: Rectc, src_rect2: Rectc): Boolean {
    val x1 = maxOf(src_rect1.x, src_rect2.x)
    val y1 = maxOf(src_rect1.y, src_rect2.y)
    val x2 = minOf(src_rect1.x2, src_rect2.x2) + 1
    val y2 = minOf(src_rect1.y2, src_rect2.y2) + 1
    return x2 - x1 > 0 && y2 - y1 > 0
}

@ExperimentalUnsignedTypes
fun ClipRect(rc: Rect, rect: Rectc): Boolean {
    if (rc.x < rect.x) {
        val v = (rect.x - rc.x).toUInt()
        if (v > rc.w) return false
        rc.w -= v
        rc.x = rect.x
    }

    if (rc.y < rect.y) {
        val v = (rect.y - rc.y).toUInt()
        if (v > rc.h) return false
        rc.h -= v
        rc.y = rect.y
    }

    if (rect.x2 < rc.x2) {
        val v = (rc.x2 - rect.x2).toUInt()
        if (v > rc.w) return false
        rc.w -= v
    }

    if (rect.y2 < rc.y2) {
        val v = (rc.y2 - rect.y2).toUInt()
        if (v > rc.h) return false
        rc.h -= v
    }
    return rc.w > 0u && rc.h > 0u
}

@ExperimentalUnsignedTypes
fun iClipRectRect(dst_rc: Rect, dst_rect: Rectc, src_rc: Rect, src_rect: Rectc): Boolean {
    //iSize cl = src_rc
    var clw = src_rc.w
    var clh = src_rc.h

    // check ridiculous cases
    if (!IsIntersectRect(dst_rc, dst_rect) || !IsIntersectRect(src_rc, src_rect)) return false

    // clip src left
    if (src_rc.x < 0) {
        clw += src_rc.x.toUInt()
        dst_rc.x -= src_rc.x
        src_rc.x = 0
    }
    // clip src top
    if (src_rc.y < 0) {
        clh += src_rc.y.toUInt()
        dst_rc.y -= src_rc.y
        src_rc.y = 0
    }
    // clip src right
    if (src_rc.x.toUInt() + clw > src_rect.w) {
        clw = src_rect.w - src_rc.x.toUInt()
    }
    // clip src bottom
    if (src_rc.y.toUInt() + clh > src_rect.h) {
        clh = src_rect.h - src_rc.y.toUInt()
    }
    // clip dest left
    if (dst_rc.x < dst_rect.x) {
        dst_rc.x -= dst_rect.x
        clw += dst_rc.x.toUInt()
        src_rc.x -= dst_rc.x
        dst_rc.x = dst_rect.x
    }
    // clip dest top
    if (dst_rc.y < dst_rect.y) {
        dst_rc.y -= dst_rect.y
        clh += dst_rc.y.toUInt()
        src_rc.y -= dst_rc.y
        dst_rc.y = dst_rect.y
    }
    // clip dest right
    if (dst_rc.x + clw.toInt() >= dst_rect.x2 + 1) {
        clw = (dst_rect.x2 - dst_rc.x + 1).toUInt()
    }
    // clip dest bottom
    if (dst_rc.y + clh.toInt() >= dst_rect.y2 + 1) {
        clh = (dst_rect.y2 - dst_rc.y + 1).toUInt()
    }
    // bail out on zero size
    if (clw <= 0u || clh <= 0u) {
        return false
    }

    dst_rc.w = clw; dst_rc.h = clh
    src_rc.w = clw; src_rc.h = clh

    return true
}
