package com.github.servb.pph.gxlib

import com.github.servb.pph.util.asRectangle
import com.github.servb.pph.util.helpertype.or
import com.soywiz.korma.geom.IPointInt

abstract class iTopmostView : iView {

    constructor(pViewMgr: iViewMgr) : super(
        pViewMgr,
        pViewMgr.Metrics().asRectangle(),
        VIEWCLSID.GENERIC_VIEWPORT,
        0u,
        ViewState.Visible or ViewState.Enabled
    )

    override fun `$destruct`() {}

    fun ProcessMessage(msg: iInput.iEntry): Boolean {
        when (msg) {
            is iInput.iEntry.MouseMove -> MouseTrack(IPointInt(msg.px, msg.py))
            is iInput.iEntry.MouseDown -> MouseDown(IPointInt(msg.px, msg.py))
            is iInput.iEntry.MouseUp -> MouseUp(IPointInt(msg.px, msg.py))
            is iInput.iEntry.KeyDown -> KeyDown(msg.key)
            is iInput.iEntry.KeyUp -> KeyUp(msg.key)
        }
        return true
    }

    fun KeyDown(key: iKbdKey): Boolean {
        return OnKeyDown(key)
    }

    fun KeyUp(key: iKbdKey): Boolean {
        return OnKeyUp(key)
    }

    open fun OnKeyDown(key: iKbdKey): Boolean = false
    open fun OnKeyUp(key: iKbdKey): Boolean = false
}
