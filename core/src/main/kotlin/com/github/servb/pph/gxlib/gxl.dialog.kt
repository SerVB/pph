package com.github.servb.pph.gxlib

import com.github.servb.pph.gxlib.gxlmetrics.AlignRect
import com.github.servb.pph.gxlib.gxlmetrics.Alignment
import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxlmetrics.Size
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr

enum class DialogReturnCode {
    UNDEFINED,
    OK,
    CANCEL,
    YES,
    NO,
}

@ExperimentalUnsignedTypes
abstract class iDialog(viewMgr: iViewMgr)
    : iView(viewMgr, Rect(0, 0, 0u, 0u), ViewClassId.MODAL_DIALOG, 0u, setOf(ViewState.Enabled)) {

    private var returnCode = DialogReturnCode.UNDEFINED

    fun doModal(): DialogReturnCode {
        center()
        onCreateDlg()
        isVisible = true
        mgr.pushModalDlg(this)
        while (mgr.App().Cycle() && returnCode == DialogReturnCode.UNDEFINED) {
        }
        val pDlg = mgr.popModalDialog()
        check(pDlg == this)

        return returnCode
    }

    abstract fun getDialogMetrics(): Size
    abstract fun onCreateDlg()
    open fun onPlace(rect: Rect) {}

    open fun keyDown(key: Int) = false
    open fun keyUp(key: Int) = false

    protected val isValidDialog: Boolean get() = returnCode == DialogReturnCode.UNDEFINED

    protected fun center() {
        val rect = Rect(AlignRect(getDialogMetrics(), Rect(mgr.Metrics()), Alignment.AlignCenter))
        onPlace(rect)
        this.relativeRect = rect
    }

    protected fun endDialog(retCode: DialogReturnCode): Boolean {
        if (returnCode != DialogReturnCode.UNDEFINED) {
            return false
        }
        returnCode = retCode
        return true
    }

    override fun invalidate() {
        needRedraw = true
        mgr.curView()?.invalidate()
    }
}
