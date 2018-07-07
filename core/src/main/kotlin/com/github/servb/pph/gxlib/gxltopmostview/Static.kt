package com.github.servb.pph.gxlib.gxltopmostview

import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxlview.VIEWCLSID
import com.github.servb.pph.gxlib.gxlview.iView
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr

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
            iInput.iEntry.MouseMove -> MouseTrack(Point(msg.px, msg.py))
            iInput.iEntry.MouseDown -> MouseDown(Point(msg.px, msg.py))
            iInput.iEntry.MouseUp -> MouseUp(Point(msg.px, msg.py))
            iInput.iEntry.KeyDown -> KeyDown(msg.key)
            iInput.iEntry.KeyUp -> KeyUp(msg.key)
        }
        return true;
    }
    fun KeyDown(key: Int): Boolean = OnKeyDown(key)
    fun KeyUp(key: Int): Boolean = OnKeyUp(key)

    override fun OnKeyDown(key: Int) = false
    override fun OnKeyUp(key: Int) = false
}
