package com.github.servb.pph.gxlib.gxlview

import com.github.servb.pph.gxlib.gxlmetrics.*
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr

enum class VIEWCLSID {
    GENERIC_VIEWPORT,
    MODAL_DIALOG,
    PUSH_BUTTON,
    TABBED_SWITCH,
    SCROLL_BAR,
    LIST_BOX
}

enum class CTRL_EVT_ID {
    CEI_BUTTONDOWN,
    CEI_BUTTONUP,
    CEI_TRACKING,
    CEI_CLICK
}

abstract class iView(pViewMgr: iViewMgr, rect: Rect, clsId: VIEWCLSID, uid: Int, state: Int) {
    enum class ViewState(val v: Int) {
        Visible(0x1),
        Enabled(0x2)
    }

    fun AddChild(pChild: iView): Boolean {
        pChild.m_pParent = this
        m_Childs.add(pChild)
        return true
    }
    fun RemoveChild(pChild: iView?): Boolean {
        if (pChild != null && m_Childs.remove(pChild)) {
            pChild.m_pParent = null
            return true
        }
        return false
    }

    fun GetChildByPos(pos: Pointc): iView? {
        // TODO: (from sources) reverse find (in future remake to z-order)
        if (m_Childs.isEmpty()) {
            return null
        }
        for (xx in m_Childs.size - 1 downTo 0) {
            if (m_Childs[xx].m_bVisible && m_Childs[xx].m_bEnabled && m_Childs[xx].m_Rect.PtInRect(pos)) {
                return m_Childs[xx]
            }
        }
        return null
    }

    fun GetChildById(uid: Int): iView? {
        for (child in m_Childs) {
            if (child.m_UID == uid) {
                return child
            }
        }
        return null
    }

    // Message process
    fun Compose(rect: Rect) {
        if (m_bVisible) {
            OnCompose()
            for (child in m_Childs) {
                child.Compose(rect)
            }
            if (m_bNeedRedraw) {
                rect += GetScrRect()
                m_bNeedRedraw = false
            }
        }
    }

    fun MouseDown(pos: Pointc): Boolean {
        if (!m_bEnabled) {
            // only topmost window can receive messages in disabled state
            check(m_pParent == null)
            m_pMgr.SetViewCapture(this)
            m_bTracking = true
            return true
        }

        val vp = GetChildByPos(pos - GetScrRect().toPoint())
        if (vp != null && !vp.MouseDown(pos)) {  // TODO: original line: `!vp || !vp->MouseDown(pos)`
            OnMouseDown(pos)
            m_pMgr.SetViewCapture(this)
            m_bTracking = true
        }
        return true
    }

    fun MouseUp(pos: Pointc): Boolean {
        if (m_bTracking) {
            m_bTracking = false
            m_pMgr.ReleaseViewCapture()
            OnMouseUp(pos)
            if (GetScrRect().PtInRect(pos)) {
                OnMouseClick(pos)
            }
        } else {
            check(false)
        }

        return true
    }

    fun MouseTrack(pos: Pointc): Boolean {
        if (m_bTracking && m_bEnabled) {
            OnMouseTrack(pos)
        }
        return true
    }

    // Overrides
    abstract fun OnTimer(tid: Int)

    abstract fun OnCompose()
    abstract fun OnRectChanged(rc: Rectc)
    abstract fun OnMouseDown(pos: Pointc)
    abstract fun OnMouseUp(pos: Pointc)
    abstract fun OnMouseClick(pos: Pointc)
    abstract fun OnMouseTrack(pos: Pointc)

    // View metrics
    fun SetSize(nsiz: Sizec) {
        m_Rect.w = nsiz.w
        m_Rect.h = nsiz.h
        OnRectChanged(m_Rect)
    }

    fun SetPos(npos: Pointc) {
        m_Rect.x = npos.x
        m_Rect.y = npos.y
        OnRectChanged(m_Rect)
    }

    fun SetRect(rc: Rectc) {
        m_Rect = Rect(rc)
        OnRectChanged(m_Rect)
    }

    // inlines
    fun GetSize(): Size = Size(m_Rect.toSize())

    fun GetPos(): Point = Point(m_Rect.toPoint())
    fun GetRect(): Rect = Rect(m_Rect)
    fun GetScrPos(): Point {
        val res = Point(m_Rect.toPoint())
        res += m_pParent?.GetScrPos() ?: return res
        return res
    }

    fun GetScrRect(): Rect {
        val res = Rect(m_Rect)
        res += Rect(m_pParent?.GetScrPos() ?: return res)
        return res
    }

    fun GetParent() = m_pParent
    fun IsEnabled(): Boolean = m_bEnabled && (m_pParent == null || (m_pParent?.IsEnabled() ?: false))
    fun SetEnabled(bEnabled: Boolean = true) {
        m_bEnabled = bEnabled
        Invalidate()
    }

    fun IsVisible() = m_bVisible
    fun SetVisible(bVisible: Boolean = true) {
        m_bVisible = bVisible
        Invalidate()
    }

    fun GetClassId() = m_clsId
    fun GetUID() = m_UID
    fun NeedRedraw() = m_bNeedRedraw
    open fun Invalidate() {
        m_bNeedRedraw = true
        m_pParent?.Invalidate()
    }


    protected var m_UID: Int
    protected var m_clsId: VIEWCLSID
    protected var m_Rect: Rect

    protected var m_bEnabled: Boolean
    protected var m_bVisible: Boolean
    protected var m_bTracking: Boolean = false
    protected var m_bNeedRedraw: Boolean = true

    protected var m_pMgr: iViewMgr
    protected var m_pParent: iView? = null
    protected var m_Childs: MutableList<iView> = mutableListOf()

    init {
        m_pMgr = pViewMgr
        m_bVisible = state and ViewState.Visible.v != 0
        m_bEnabled = state and ViewState.Enabled.v != 0
        m_Rect = Rect(rect)
        m_clsId = clsId
        m_UID = uid
    }
}
