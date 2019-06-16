package com.github.servb.pph.gxlib.gxlpopupview

import com.github.servb.pph.gxlib.ViewClassId
import com.github.servb.pph.gxlib.gxlmetrics.*
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr
import com.github.servb.pph.gxlib.iView

abstract class iPopupView(pViewMgr: iViewMgr) : iView(
        pViewMgr, Rect(0, 0, 0, 0), ViewClassId.GENERIC_VIEWPORT, 0, ViewState.Enabled) {

    // operations
    fun TrackPopup(pos: Pointc, bound: Rectc, al: Alignment) {
        val nsiz = PopupViewSize()
        val msiz = PopupViewMinSize()
        nsiz.w = Math.max(nsiz.w, msiz.w)
        nsiz.h = Math.max(nsiz.h, msiz.h)

        val orc = Rect(bound)

        if (al.v and Alignment.AlignRight.v != 0) {
            orc.w = pos.x - bound.x
        } else if (al.v and Alignment.AlignLeft.v != 0) {
            orc.x = pos.x
        }

        if (al.v and Alignment.AlignBottom.v != 0) {
            orc.h = pos.y - bound.y
        } else if (al.v and Alignment.AlignTop.v != 0) {
            orc.y = pos.y
        }

        relativeRect = AlignRect(nsiz, orc, al)


        if (relativeRect.x < bound.x) {
            relativeRect.x = bound.x
        } else if (relativeRect.x + relativeRect.w > bound.x + bound.w) {
            relativeRect.x = bound.x + bound.w - relativeRect.w
        }

        if (relativeRect.y < bound.y) {
            relativeRect.y = bound.y
        } else if (relativeRect.y + relativeRect.h > bound.y + bound.h) {
            relativeRect.y = bound.y + bound.h - relativeRect.h
        }

        OnTrackPopup(relativeRect)
        SetVisible(true)
    }
    fun HidePopup() {
        OnHidePopup()
        SetVisible(false)
    }

    // virtuals
    abstract fun PopupViewSize(): Size

    abstract fun PopupViewMinSize(): Size
    fun OnTrackPopup(clRect: Rectc) {}
    fun OnHidePopup() {}
}
