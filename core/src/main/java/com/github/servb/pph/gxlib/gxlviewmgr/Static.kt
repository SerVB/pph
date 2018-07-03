package com.github.servb.pph.gxlib.gxlviewmgr

import com.github.servb.pph.gxlib.gxlapplication.iGXApp
import com.github.servb.pph.gxlib.gxlmetrics.*
import com.github.servb.pph.gxlib.gxlview.iView
import com.github.servb.pph.util.staticFunction.Tracer

class iViewMgr {
    interface IDragGlyph {
        fun ComposeDragGlyph()
    }

    constructor(pApp: iGXApp) {
        m_pApp = pApp
        m_Surf = pApp.Display().GetSurface()
        m_pCurView = null
        m_pCapView = null
        m_pPopupView = null
        m_timerCounter = 0
    }

    fun ProcessMessage(msg: iInput.iEntry): Boolean {
        if (m_pPopupView) {
            // In case of popup view
            when (msg.taskType) {
                iInput.iEntry.MouseMove -> if (m_pCapView != null) {
                    m_pCapView.MouseTrack(Point(msg.px, msg.py))
                } else {
                    m_pPopupView.MouseTrack(Point(msg.px, msg.py))
                }
                iInput.iEntry.MouseDown ->
                if (m_pCapView != null) {
                    m_pCapView.MouseDown(Point(msg.px, msg.py))
                } else {
                    if (!m_pPopupView.GetRect().PtInRect(msg.px, msg.py)) {
                        m_pPopupView.HidePopup();
                        if (m_pCurView) {
                            m_pCurView.Invalidate()
                        }
                    } else {
                        m_pPopupView.MouseDown(Point(msg.px,msg.py))
                    }
                }
                iInput.iEntry.MouseUp ->
                if (m_pCapView != null) {
                    m_pCapView.MouseUp(Point(msg.px, msg.py))
                } else {
                    if (m_pPopupView.IsVisible()) {
                        m_pPopupView.MouseUp(Point(msg.px, msg.py))
                    } else {
                        HidePopup()
                    }
                }
                iInput.iEntry.KeyDown -> m_pApp.KeyDown(msg.key)
                iInput.iEntry.KeyUp -> m_pApp.KeyUp(msg.key)
            }
        } else if (!m_dlgStack.isEmpty()) {
            // In case of dialog stack
            val pDlg = m_dlgStack[m_dlgStack.size - 1]
            when (msg.taskType) {
                iInput.iEntry.MouseMove -> if (m_pCapView != null) {
                    m_pCapView.MouseTrack(Point(msg.px, msg.py))
                } else {
                    pDlg.MouseTrack(Point(msg.px, msg.py))
                }
                iInput.iEntry.MouseDown -> if (m_pCapView != null) {
                    m_pCapView.MouseDown(Point(msg.px, msg.py))
                } else {
                    pDlg.MouseDown(Point(msg.px, msg.py))
                }
                iInput.iEntry.MouseUp -> if (m_pCapView != null) {
                    m_pCapView.MouseUp(Point(msg.px, msg.py))
                } else {
                    pDlg.MouseUp(Point(msg.px, msg.py))
                }
                iInput.iEntry.KeyDown -> if (!pDlg.KeyDown(msg.key)) {
                    m_pApp.KeyDown(msg.key)
                }
                iInput.iEntry.KeyUp -> if (!pDlg.KeyUp(msg.key)) {
                    m_pApp.KeyUp(msg.key)
                }
            }
        } else if (m_pCurView != null) {
            // In general case
            when (msg.taskType) {
                iInput.iEntry.MouseMove ->
                if (m_pCapView != null) {
                    m_pCapView.MouseTrack(Point(msg.px,msg.py))
                } else {
                    m_pCurView.MouseTrack(Point(msg.px, msg.py))
                }
                iInput.iEntry.MouseDown ->
                if (m_pCapView != null) {
                    m_pCapView.MouseDown(Point(msg.px, msg.py))
                } else {
                    m_pCurView.MouseDown(Point(msg.px,msg.py))
                }
                iInput.iEntry.MouseUp ->
                if (m_pCapView != null) {
                    m_pCapView.MouseUp(Point(msg.px, msg.py))
                } else {
                    m_pCurView.MouseUp(Point(msg.px, msg.py))
                }
                iInput.iEntry.KeyDown ->
                if (!m_pCurView.KeyDown(msg.key)) {
                    m_pApp.KeyDown(msg.key)
                }
                iInput.iEntry.KeyUp ->
                if (!m_pCurView.KeyUp(msg.key)) {
                    m_pApp.KeyUp(msg.key)
                }
            }
        } else {
            when (msg.taskType) {
                iInput.iEntry.KeyDown -> m_pApp.KeyDown(msg.key)
                iInput.iEntry.KeyUp -> m_pApp.KeyUp(msg.key)
            }
        }

        return true;
    }
    fun Compose(rect: IRect) {
        // Compose current topmost view
        if (m_pCurView != null && m_pCurView.NeedRedraw()) {
            m_pCurView.Compose(rect)
        }

        // Compose dialog stack
        m_dlgStack.forEach { it.Compose(rect) }

        // Compose Popup view
        if (m_pPopupView != null) {
            m_pPopupView.Compose(rect)
        }

        // Compose drag'n'drop glyph
        if (m_pDragGlyph != null) {
            m_pDragGlyph.ComposeDragGlyph()
        }
    }

    fun App() = m_pApp
    fun Surface() = m_Surf
    fun Metrics() = Size(m_Surf.GetSize())

    fun SetCurView(pCurView: iTopmostView) {
        m_pCurView = pCurView
    }
    fun CurView() = m_pCurView as iView

    // Capture view
    fun SetViewCapture(pCapView: iView) {
        Tracer.check(m_pCapView == null)
        m_pCapView = pCapView
    }
    fun ReleaseViewCapture(): iView {
        Tracer.check(m_pCapView != null)
        val nView = m_pCapView!!  // TODO: difficult place
        m_pCapView = null
        return nView
    }
    val CapturedView: iView get() = m_pCapView

    // Modal stack
    val HasModalDlg get() = m_dlgStack.GetSize() > 0
    fun PushModalDlg(pDlg: iDialog) {
        m_dlgStack.add(pDlg)
    }
    fun PopModalDlg(): iDialog {
        if (m_pCurView) {
            m_pCurView.Invalidate()
        }
        return m_dlgStack.removeAt(m_dlgStack.size - 1)
    }

    // Popup windows
    fun TrackPopup(pPopupView: iPopupView, pos: IConstPoint, bound: IConstRect, al: Alignment) {
        Tracer.check(m_pPopupView == null && pPopupView != null)
        m_pPopupView = pPopupView
        m_pPopupView.TrackPopup(pos, bound, al)
    }
    fun HidePopup() {
        Tracer.check(m_pPopupView)
        m_pPopupView = null;
        if (m_pCurView != null) {
            m_pCurView.Invalidate()
        }
    }

    // Drag'n'Drop glyph
    fun SetDragGlyph(pDragGlyph: IDragGlyph) {
        m_pDragGlyph = pDragGlyph
    }
    fun DragGlyph() = m_pDragGlyph

    // Timer processing
    fun Process(interval: Uint) {
        if (!m_timers.isEmpty()) {
            m_timerCounter += interval
            while (!m_timers.isEmpty() && m_timers[m_timers.size - 1].timer <= m_timerCounter) {
                val item = m_timers.removeAt(m_timers.size - 1)
                item.pHandler.OnTimer(item.tid)
            }
        }
    }
    fun SetTimer(period: Uint, tid: Uint, pHandler: iView) {
        if (m_timers.isEmpty()) {
            // reset timer counter
            m_timerCounter = 0
            m_timers.add(iTimerHandler(period, tid, pHandler))
        } else if (m_timers[m_timers.size - 1].timer >= (period+m_timerCounter) ) {
            m_timers.add(iTimerHandler(period+m_timerCounter, tid, pHandler))
        } else {
            val ntimer = period+m_timerCounter
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


    private var m_Surf: iDib
    private var m_pApp: iGXApp
    private var m_pCurView: iTopmostView
    private var m_pCapView: iView?
    private var m_pDragGlyph: IDragGlyph?
    private var m_pPopupView: iPopupView

    private var m_dlgStack: MutableList<iDialog>

    data class iTimerHandler(var timer: Uint, var tid: Uint, var pHandler: iView)

    private var m_timers: MutableList<iTimerHandler>
    private var m_timerCounter: Uint
}
