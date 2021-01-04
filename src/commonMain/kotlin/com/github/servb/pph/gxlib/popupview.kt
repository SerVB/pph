package com.github.servb.pph.gxlib

import com.github.servb.pph.util.helpertype.and
import com.github.servb.pph.util.invoke
import com.github.servb.pph.util.setTo
import com.soywiz.korma.geom.*

abstract class iPopupView : iView {

    constructor(viewMgr: iViewMgr) : super(
        viewMgr,
        IRectangleInt(0, 0, 0, 0),
        VIEWCLSID.GENERIC_VIEWPORT,
        0u,
        ViewState.Enabled.v
    )

    final override fun `$destruct`() {
        // empty. seems like overriding behavior of the iView (of removing children).
        // seems logical because popup view shouldn't have children
    }

    fun TrackPopup(pos: IPointInt, bound: IRectangleInt, alignment: Alignment) {
        val nsiz = PopupViewSize()
        val msiz = PopupViewMinSize()
        nsiz.width = maxOf(nsiz.width, msiz.width)
        nsiz.height = maxOf(nsiz.height, msiz.height)

        val orc = RectangleInt(bound)

        if ((alignment and Alignment.AlignRight) != 0) {
            orc.width = pos.x - bound.x
        } else if ((alignment and Alignment.AlignLeft) != 0) {
            orc.x = pos.x
        }

        if ((alignment and Alignment.AlignBottom) != 0) {
            orc.height = pos.y - bound.y
        } else if ((alignment and Alignment.AlignTop) != 0) {
            orc.y = pos.y
        }

        m_Rect.setTo(AlignRect(nsiz, orc, alignment))

        if (m_Rect.x < bound.x) {
            m_Rect.x = bound.x
        } else if (m_Rect.right > bound.right) {
            m_Rect.x = bound.x + (bound.width - m_Rect.width)
        }

        if (m_Rect.y < bound.y) {
            m_Rect.y = bound.y
        } else if (m_Rect.bottom > bound.bottom) {
            m_Rect.y = bound.y + (bound.height - m_Rect.height)
        }

        OnTrackPopup(m_Rect)
        SetVisible(true)
    }

    fun HidePopup() {
        OnHidePopup()
        SetVisible(false)
    }

    abstract fun PopupViewSize(): SizeInt
    abstract fun PopupViewMinSize(): SizeInt
    open fun OnTrackPopup(clRect: IRectangleInt) {}
    open fun OnHidePopup() {}
}
