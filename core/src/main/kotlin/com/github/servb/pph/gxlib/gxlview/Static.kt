package com.github.servb.pph.gxlib.gxlview

import com.github.servb.pph.gxlib.gxlmetrics.MutableRect
import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxlmetrics.Size
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr
import unsigned.Uint
import unsigned.ui

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

abstract class iView(pViewMgr: iViewMgr, rect: Rect, clsId: VIEWCLSID, uid: Uint, state: Uint) {
    enum class ViewState(val v: Uint) {
        Visible(0x1.ui),
        Enabled(0x2.ui)
    }

    fun AddChild(pChild: iView): Boolean {
        pChild.m_pParent = this
        m_Childs.add(pChild)
        return true
    }
    fun RemoveChild(pChild: iView?): Boolean {
        if (pChild != null && m_Childs.Remove(pChild)){
            pChild.m_pParent = null
            return true
        }
        return false;
    }
    fun GetChildByPos(pos: IConstPoint): iView? {
        // TODO: (from sources) reverse find (in future remake to z-order)
        if (m_Childs.empty) {
            return null
        }
        for (xx in m_Childs.GetSize()-1 downTo 0) {
            if (m_Childs[xx].m_bVisible && m_Childs[xx].m_bEnabled && m_Childs[xx].m_Rect.PtInRect(pos)) {
                return m_Childs[xx]
            }
        }
        return null
    }
    fun GetChildById(uid: Uint): iView? {
        for (child in m_Childs) {
            if (child.m_UID == uid) {
                return child
            }
        }
        return null
    }

    // Message process
    fun Compose(rect: IRect) {
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

    fun MouseDown(pos: IConstPoint): Boolean {
        if (!m_bEnabled) {
            // only topmost window can receive messages in disabled state
            check(m_pParent == null)
            m_pMgr.SetViewCapture(this)
            m_bTracking = true
            return true
        }

        val vp = GetChildByPos(pos - GetScrRect().point())
        if (vp != null && !vp.MouseDown(pos)) {  // TODO: original line: `!vp || !vp->MouseDown(pos)`
            OnMouseDown(pos)
            m_pMgr.SetViewCapture(this)
            m_bTracking = true
        }
        return true
    }
    fun MouseUp(pos: IConstPoint): Boolean {
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
    fun MouseTrack(pos: IConstPoint): Boolean {
        if (m_bTracking && m_bEnabled) {
            OnMouseTrack(pos)
        }
        return true
    }

    // Overrides
    abstract fun OnTimer(tid: Uint)

    abstract fun OnCompose()
    abstract fun OnRectChanged(rc: IConstRect)
    abstract fun OnMouseDown(pos: IConstPoint)
    abstract fun OnMouseUp(pos: IConstPoint)
    abstract fun OnMouseClick(pos: IConstPoint)
    abstract fun OnMouseTrack(pos: IConstPoint)

    // View metrics
    fun SetSize(nsiz: IConstSize) {
        m_Rect.w = nsiz.w
        m_Rect.h = nsiz.h
        OnRectChanged(m_Rect)
    }
    fun SetPos(npos: IConstPoint) {
        m_Rect.x = npos.x
        m_Rect.y = npos.y
        OnRectChanged(m_Rect)
    }
    fun SetRect(rc: IConstRect) {
        m_Rect = Rect(rc)
        OnRectChanged(m_Rect)
    }

    // inlines
    fun GetSize(): ISize = Size(m_Rect.size())

    fun GetPos(): IPoint = Point(m_Rect.point())
    fun GetRect(): IRect = Rect(m_Rect)
    fun GetScrPos(): IPoint {
        val res = Point(m_Rect.point())
        if (m_pParent) {
            res += m_pParent.GetScrPos()
        }
        return res
    }

    fun GetScrRect(): IRect {
        val res = Rect(m_Rect)
        if (m_pParent) {
            res += m_pParent.GetScrPos()
        }
        return res
    }

    fun GetParent() = m_pParents
    fun IsEnabled(): Boolean = m_bEnabled && (!m_pParent || (m_pParent && m_pParent.IsEnabled()))
    fun SetEnabled(bEnabled: Boolean = true) {
        m_bEnabled = bEnabled
        Invalidate()
    }

    fun IsVisible() = m_bVisible
    fun SetVisible(bVisible: Boolean = true) {
        m_bVisible = bVisible
        Invalidate()
    }

    fun GetClassId() = Uint(m_clsId)
    fun GetUID() = Uint(m_UID)
    fun NeedRedraw() = m_bNeedRedraw
    open fun Invalidate() {
        m_bNeedRedraw = true
        if (m_pParent) {
            m_pParent.Invalidate()
        }
    }

    private fun CreateView(pViewMgr: iViewMgr?, rect: Rect, clsId: VIEWCLSID, uid: Uint, state: Uint): Boolean {
        if (pViewMgr == null) {
            return false
        }
        m_pMgr = pViewMgr
        m_bVisible = state and ViewState.Visible.v != 0.ui
        m_bEnabled = state and ViewState.Enabled.v != 0.ui
        m_Rect = Rect(rect)
        m_clsId = clsId
        m_UID = uid
        return true
    }


    protected var m_UID: Uint
    protected var m_clsId: VIEWCLSID
    protected var m_Rect: MutableRect

    protected var m_bEnabled: Boolean
    protected var m_bVisible: Boolean
    protected var m_bTracking: Boolean
    protected var m_bNeedRedraw: Boolean

    protected var m_pMgr: iViewMgr
    protected var m_pParent: iView?
    protected var m_Childs: MutableList<iView>

    init {
        m_bNeedRedraw = true
        m_bTracking = false
        m_pParent = null
        CreateView(pViewMgr, rect, clsId, uid, state)
    }
}
