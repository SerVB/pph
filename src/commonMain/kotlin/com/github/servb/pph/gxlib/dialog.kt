package com.github.servb.pph.gxlib

import com.github.servb.pph.util.asRectangle
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt

enum class DLG_RETCODE(override val v: Int) : UniqueValueEnum {
    UNDEFINED(-1),
    OK(0),
    CANCEL(1),
    YES(2),
    NO(3),
}

abstract class iDialog : iView {

    private var m_retCode: Int  // can't use DLG_RETCODE type because some dialogs use plain Int

    constructor(viewMgr: iViewMgr) : super(
        viewMgr,
        IRectangleInt(0, 0, 0, 0),
        VIEWCLSID.MODAL_DIALOG,
        0u,
        ViewState.Enabled.v,
    ) {
        m_retCode = DLG_RETCODE.UNDEFINED.v
    }

    suspend fun DoModal(): Int {
        Center()
        OnCreateDlg()
        SetVisible()
        m_pMgr.pushModalDlg(this)
        while (m_pMgr.App().Cycle() && m_retCode == DLG_RETCODE.UNDEFINED.v) {
            // cycling, no body needed
        }
        val pDlg = m_pMgr.popModalDialog()
        check(pDlg == this)

        return m_retCode
    }

    abstract fun GetDialogMetrics(): SizeInt
    abstract fun OnCreateDlg()
    open fun OnPlace(rect: RectangleInt) {}

    open fun KeyDown(key: iKbdKey): Boolean = false
    open fun KeyUp(key: iKbdKey): Boolean = false

    protected fun IsValidDialog(): Boolean = m_retCode == DLG_RETCODE.UNDEFINED.v

    protected fun Center() {
        val rect = AlignRect(GetDialogMetrics(), m_pMgr.Metrics().asRectangle(), Alignment.AlignCenter)
        OnPlace(rect)
        SetRect(rect)
    }

    protected fun EndDialog(retCode: Int): Boolean {
        if (m_retCode != DLG_RETCODE.UNDEFINED.v) {
            return false
        }
        m_retCode = retCode
        return true
    }

    override fun Invalidate() {
        m_bNeedRedraw = true
        m_pMgr.CurView()?.Invalidate()
    }
}
