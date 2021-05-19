package com.github.servb.pph.gxlib

import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.contains

private typealias iDialogStack = MutableList<iDialog>
private typealias iTimerList = MutableList<iTimerHandler>

private class iTimerHandler(val timer: UInt, val tid: UInt, val pHandler: iView)

class iViewMgr {

    interface IDragGlyph {
        fun ComposeDragGlyph()
    }

    private var m_Surf: iDib
    private var m_pApp: iGXApp
    private var m_pCurView: iTopmostView?
    private var m_pCapView: iView?
    private var m_pDragGlyph: IDragGlyph? = null
    private var m_pPopupView: iPopupView?

    private val m_dlgStack: iDialogStack = mutableListOf()

    private var m_timers: iTimerList = mutableListOf()
    private var m_timerCounter: UInt

    constructor(pApp: iGXApp) {
        m_pApp = pApp
        m_Surf = pApp.Display().GetSurface()
        m_pCurView = null
        m_pCapView = null
        m_pPopupView = null
        m_timerCounter = 0u
    }

    suspend fun ProcessMessage(msg: iInput.iEntry): Boolean {
        val popupView = m_pPopupView
        val capView = m_pCapView
        val curView = m_pCurView

        if (popupView != null) {
            // In case of popup view
            when (msg) {
                is iInput.iEntry.MouseMove -> if (capView != null) {
                    capView.MouseTrack(IPointInt(msg.px, msg.py))
                } else {
                    popupView.MouseTrack(IPointInt(msg.px, msg.py))
                }
                is iInput.iEntry.MouseDown -> if (capView != null) {
                    capView.MouseDown(IPointInt(msg.px, msg.py))
                } else {
                    if (!popupView.GetRect().contains(msg.px, msg.py)) {
                        popupView.HidePopup()
                        curView?.Invalidate()
                    } else {
                        popupView.MouseDown(IPointInt(msg.px, msg.py))
                    }
                }
                is iInput.iEntry.MouseUp ->
                    if (capView != null) {
                        capView.MouseUp(IPointInt(msg.px, msg.py))
                    } else {
                        if (popupView.IsVisible()) {
                            popupView.MouseUp(IPointInt(msg.px, msg.py))
                        } else {
                            HidePopup()
                        }
                    }
                is iInput.iEntry.KeyDown -> m_pApp.KeyDown(msg.key)
                is iInput.iEntry.KeyUp -> m_pApp.KeyUp(msg.key)
            }
        } else if (m_dlgStack.isNotEmpty()) {
            // In case of dialog stack
            val pDlg = m_dlgStack.last()
            when (msg) {
                is iInput.iEntry.MouseMove -> if (capView != null) {
                    capView.MouseTrack(IPointInt(msg.px, msg.py))
                } else {
                    pDlg.MouseTrack(IPointInt(msg.px, msg.py))
                }
                is iInput.iEntry.MouseDown -> if (capView != null) {
                    capView.MouseDown(IPointInt(msg.px, msg.py))
                } else {
                    pDlg.MouseDown(IPointInt(msg.px, msg.py))
                }
                is iInput.iEntry.MouseUp -> if (capView != null) {
                    capView.MouseUp(IPointInt(msg.px, msg.py))
                } else {
                    pDlg.MouseUp(IPointInt(msg.px, msg.py))
                }
                is iInput.iEntry.KeyDown -> if (!pDlg.KeyDown(msg.key)) {
                    m_pApp.KeyDown(msg.key)
                }
                is iInput.iEntry.KeyUp -> if (!pDlg.KeyUp(msg.key)) {
                    m_pApp.KeyUp(msg.key)
                }
            }
        } else if (curView != null) {
            // In general case
            when (msg) {
                is iInput.iEntry.MouseMove -> if (capView != null) {
                    capView.MouseTrack(IPointInt(msg.px, msg.py))
                } else {
                    curView.MouseTrack(IPointInt(msg.px, msg.py))
                }
                is iInput.iEntry.MouseDown -> if (capView != null) {
                    capView.MouseDown(IPointInt(msg.px, msg.py))
                } else {
                    curView.MouseDown(IPointInt(msg.px, msg.py))
                }
                is iInput.iEntry.MouseUp -> if (capView != null) {
                    capView.MouseUp(IPointInt(msg.px, msg.py))
                } else {
                    curView.MouseUp(IPointInt(msg.px, msg.py))
                }
                is iInput.iEntry.KeyDown -> if (!curView.KeyDown(msg.key)) {
                    m_pApp.KeyDown(msg.key)
                }
                is iInput.iEntry.KeyUp -> if (!curView.KeyUp(msg.key)) {
                    m_pApp.KeyUp(msg.key)
                }
            }
        } else {
            when (msg) {
                is iInput.iEntry.KeyDown -> m_pApp.KeyDown(msg.key)
                is iInput.iEntry.KeyUp -> m_pApp.KeyUp(msg.key)
            }
        }

        return true
    }

    fun Compose(rect: RectangleInt) {
        // compose current topmost view
        m_pCurView.let {
            if (it != null && it.NeedRedraw()) {
                it.Compose(rect)
            }
        }

        // compose dialog stack
        m_dlgStack.forEach { it.Compose(rect) }

        // compose Popup view
        m_pPopupView?.Compose(rect)

        // compose drag'n'drop glyph
        m_pDragGlyph?.ComposeDragGlyph()
    }

    fun App() = m_pApp
    fun Surface() = m_Surf
    fun Metrics() = m_Surf.GetSize()

    fun SetCurView(pCurView: iTopmostView?) {
        m_pCurView = pCurView
    }

    fun CurView(): iView? = m_pCurView

    // Capture view
    fun SetViewCapture(pCapView: iView) {
        check(m_pCapView == null)
        m_pCapView = pCapView
    }

    fun ReleaseViewCapture(): iView {
        val nView = checkNotNull(m_pCapView)
        m_pCapView = null
        return nView
    }

    fun CapturedView(): iView? = m_pCapView

    // Modal stack
    fun HasModalDlg(): Boolean = m_dlgStack.isNotEmpty()  // const methods, but seems const vievmgr is used nowhere

    fun pushModalDlg(pDlg: iDialog) {
        m_dlgStack.add(pDlg)
    }

    fun popModalDialog(): iDialog {
        m_pCurView?.Invalidate()
        return m_dlgStack.removeLast()
    }

    // Popup windows
    fun TrackPopup(pPopupView: iPopupView, pos: IPointInt, bound: IRectangleInt, al: Alignment) {
        check(m_pPopupView == null)
        m_pPopupView = pPopupView.also {
            it.TrackPopup(pos, bound, al)
        }
    }

    fun HidePopup() {
        checkNotNull(m_pPopupView)
        m_pPopupView = null
        m_pCurView?.Invalidate()
    }

    // Drag'n'Drop glyph
    fun SetDragGlyph(pDragGlyph: IDragGlyph?) {
        m_pDragGlyph = pDragGlyph
    }

    fun DragGlyph(): IDragGlyph? = m_pDragGlyph

    // Timer processing
    fun Process(interval: UInt) {
        if (m_timers.isNotEmpty()) {
            m_timerCounter += interval
            while (m_timers.isNotEmpty() && m_timers.last().timer <= m_timerCounter) {
                val item = m_timers.removeLast()
                item.pHandler.OnTimer(item.tid)
            }
        }
    }

    fun SetTimer(period: UInt, tid: UInt, pHandler: iView) {
        if (m_timers.isEmpty()) {
            // reset timer counter
            m_timerCounter = 0u
            m_timers.add(iTimerHandler(period, tid, pHandler))
        } else if (m_timers.last().timer >= (period + m_timerCounter)) {
            m_timers.add(iTimerHandler(period + m_timerCounter, tid, pHandler))
        } else {
            val ntimer = period + m_timerCounter
            for (xx in 0 until m_timers.size) {
                if (ntimer >= m_timers[xx].timer) {
                    m_timers.add(xx, iTimerHandler(ntimer, tid, pHandler))
                    break
                }
            }
        }
    }
    fun CleanupTimers(pHandler: iView? = null) {
        if (pHandler == null) {
            m_timers.clear()
        } else {
            var xx = 0
            while (xx < m_timers.size) {
                if (m_timers[xx].pHandler == pHandler) {
                    m_timers.removeAt(xx)
                } else {
                    ++xx
                }
            }
        }
    }
}
