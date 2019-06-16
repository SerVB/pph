package com.github.servb.pph.gxlib.gxltopmostview

import com.github.servb.pph.gxlib.VIEWCLSID
import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr
import com.github.servb.pph.gxlib.iView

class iTopmostView(pViewMgr: iViewMgr) : iView(
        pViewMgr,
        Rect(pViewMgr.Metrics()),
        VIEWCLSID.GENERIC_VIEWPORT,
        0,
        ViewState.Visible.v or ViewState.Enabled.v
) {

    // Message handler
    fun ProcessMessage(msg: iInput.iEntry): Boolean {
        when (msg.taskType) {
            iInput.iEntry.MouseMove -> mouseTrack(Point(msg.px, msg.py))
            iInput.iEntry.MouseDown -> mouseDown(Point(msg.px, msg.py))
            iInput.iEntry.MouseUp -> mouseUp(Point(msg.px, msg.py))
            iInput.iEntry.KeyDown -> KeyDown(msg.key)
            iInput.iEntry.KeyUp -> KeyUp(msg.key)
        }
        return true
    }
    fun KeyDown(key: Int): Boolean = OnKeyDown(key)
    fun KeyUp(key: Int): Boolean = OnKeyUp(key)

    override fun OnKeyDown(key: Int) = false
    override fun OnKeyUp(key: Int) = false
}
