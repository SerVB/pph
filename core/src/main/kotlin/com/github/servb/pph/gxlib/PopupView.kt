package com.github.servb.pph.gxlib

import com.github.servb.pph.gxlib.gxlmetrics.*
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr

@ExperimentalUnsignedTypes
abstract class iPopupView(viewMgr: iViewMgr)
    : iView(viewMgr, Rect(0, 0, 0u, 0u), ViewClassId.GENERIC_VIEWPORT, 0u, setOf(ViewState.Enabled)) {

    fun trackPopup(pos: Pointc, bound: Rectc, alignment: Alignment) {
        val nsiz = popupViewSize()
        val msiz = popupViewMinSize()
        nsiz.w = maxOf(nsiz.w, msiz.w)
        nsiz.h = maxOf(nsiz.h, msiz.h)

        val orc = Rect(bound)

        if ((alignment.v and Alignment.AlignRight.v) != 0) {
            orc.w = (pos.x - bound.x).toUInt()
        } else if ((alignment.v and Alignment.AlignLeft.v) != 0) {
            orc.x = pos.x
        }

        if ((alignment.v and Alignment.AlignBottom.v) != 0) {
            orc.h = (pos.y - bound.y).toUInt()
        } else if ((alignment.v and Alignment.AlignTop.v) != 0) {
            orc.y = pos.y
        }

        relativeRect = AlignRect(nsiz, orc, alignment)

        if (relativeRect.x < bound.x) {
            relativeRect.x = bound.x
        } else if (relativeRect.x.toUInt() + relativeRect.w > bound.x.toUInt() + bound.w) {
            relativeRect.x = bound.x + (bound.w - relativeRect.w).toInt()
        }

        if (relativeRect.y < bound.y) {
            relativeRect.y = bound.y
        } else if (relativeRect.y.toUInt() + relativeRect.h > bound.y.toUInt() + bound.h) {
            relativeRect.y = bound.y + (bound.h - relativeRect.h).toInt()
        }

        onTrackPopup(relativeRect)
        isVisible = true
    }

    fun hidePopup() {
        onHidePopup()
        isVisible = false
    }

    abstract fun popupViewSize(): Size
    abstract fun popupViewMinSize(): Size
    open fun onTrackPopup(clRect: Rectc) {}
    open fun onHidePopup() {}
}
