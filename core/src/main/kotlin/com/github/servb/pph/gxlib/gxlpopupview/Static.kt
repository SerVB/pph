package com.github.servb.pph.gxlib.gxlpopupview

import com.github.servb.pph.gxlib.gxlmetrics.*
import com.github.servb.pph.gxlib.gxlview.VIEWCLSID
import com.github.servb.pph.gxlib.gxlview.iView
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr

abstract class iPopupView(pViewMgr: iViewMgr) : iView(
        pViewMgr, Rect(0,0,0,0), VIEWCLSID.GENERIC_VIEWPORT, 0, ViewState.Enabled) {

    // operations
    fun TrackPopup(pos: IConstPoint, bound: IConstRect, al: Alignment) {
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

        m_Rect = AlignRect(nsiz, orc, al)


        if (m_Rect.x < bound.x) {
            m_Rect.x = bound.x
        } else if (m_Rect.x + m_Rect.w > bound.x + bound.w) {
            m_Rect.x = bound.x + bound.w - m_Rect.w
        }

        if (m_Rect.y < bound.y) {
            m_Rect.y = bound.y
        } else if (m_Rect.y + m_Rect.h > bound.y + bound.h) {
            m_Rect.y = bound.y + bound.h - m_Rect.h
        }

        OnTrackPopup(m_Rect)
        SetVisible(true)
    }
    fun HidePopup() {
        OnHidePopup()
        SetVisible(false)
    }

    // virtuals
    abstract fun PopupViewSize(): ISize
    abstract fun PopupViewMinSize(): ISize
    fun OnTrackPopup(clRect: IConstRect) {}
    fun OnHidePopup() {}
}
