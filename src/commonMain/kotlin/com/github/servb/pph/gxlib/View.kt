package com.github.servb.pph.gxlib

import com.github.servb.pph.util.contains
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.and
import com.github.servb.pph.util.invoke
import com.github.servb.pph.util.setTo
import com.github.servb.pph.util.setToUnion
import com.soywiz.korma.geom.*

enum class VIEWCLSID {
    GENERIC_VIEWPORT,
    MODAL_DIALOG,
    PUSH_BUTTON,
    TABBED_SWITCH,
    SCROLL_BAR,
    LIST_BOX,
}

enum class CTRL_EVT_ID {
    BUTTONDOWN,
    BUTTONUP,
    TRACKING,
    CLICK,
}

typealias iViewList = MutableList<iView>

interface IiView {

    fun GetChildByPos(pos: IPointInt): IiView?
    fun GetChildById(uid: UInt): IiView?

    fun GetSize(): SizeInt
    fun GetPos(): PointInt
    fun GetRect(): RectangleInt
    fun GetSrcPos(): PointInt
    fun GetScrRect(): RectangleInt
    fun GetParent(): IiView?
    fun IsEnabled(): Boolean
    fun IsVisible(): Boolean
    fun GetClassId(): VIEWCLSID
    fun GetUID(): UInt
    fun NeedRedraw(): Boolean
}

abstract class iView : IiView {

    enum class ViewState(override val v: Int) : UniqueValueEnum {
        Visible(0x1),
        Enabled(0x2)
    }

    protected val m_UID: UInt
    protected val m_clsId: VIEWCLSID
    protected val m_Rect: RectangleInt

    protected var m_bEnabled: Boolean
        private set

    protected var m_bVisible: Boolean
        private set

    protected var m_bTracking: Boolean
        private set

    protected var m_bNeedRedraw: Boolean

    protected val m_pMgr: iViewMgr

    protected var m_pParent: iView?
        private set

    protected val m_Childs: iViewList = mutableListOf()

    constructor(pViewMgr: iViewMgr, rect: IRectangleInt, clsId: VIEWCLSID, uid: UInt, state: Int) {
        m_bNeedRedraw = true
        m_bTracking = false
        m_pParent = null

        // inlined CreateView:
        m_pMgr = pViewMgr
        m_bVisible = (state and ViewState.Visible) > 0
        m_bEnabled = (state and ViewState.Enabled) > 0
        m_Rect = RectangleInt(rect)
        m_clsId = clsId
        m_UID = uid
    }

    open fun `$destruct`() {
        Cleanup()
    }

    fun AddChild(pChild: iView): Boolean {
        pChild.m_pParent = this
        m_Childs.add(pChild)
        return true
    }

    fun RemoveChild(pChild: iView): Boolean {
        // need support for nullable argument? check the sources!
        if (m_Childs.remove(pChild)) {
            pChild.m_pParent = null
            return true
        }
        return false
    }

    final override fun GetChildByPos(pos: IPointInt): iView? {
        // reverse find (in future remake to z-order)
        return m_Childs.lastOrNull { it.m_bVisible && it.m_bEnabled && pos in it.m_Rect }
    }

    final override fun GetChildById(uid: UInt): iView? {
        return m_Childs.firstOrNull { it.m_UID == uid }
    }

    fun Compose(rect: RectangleInt) {
        if (m_bVisible) {
            OnCompose()
            m_Childs.forEach { it.Compose(rect) }
            if (m_bNeedRedraw) {
                rect.setToUnion(GetScrRect())
                m_bNeedRedraw = false
            }
        }
    }

    suspend fun MouseDown(pos: IPointInt): Boolean {
        if (!m_bEnabled) {
            check(m_pParent == null) { "only topmost window can receive messages in disabled state" }
            m_pMgr.SetViewCapture(this)
            m_bTracking = true
            return true
        }

        val vp = GetChildByPos(pos - GetScrRect().getPosition())
        if (vp == null || !vp.MouseDown(pos)) {
            OnMouseDown(pos)
            m_pMgr.SetViewCapture(this)
            m_bTracking = true
        }
        return true
    }

    suspend fun MouseUp(pos: IPointInt): Boolean {
        check(m_bTracking)
        m_bTracking = false
        m_pMgr.ReleaseViewCapture()
        OnMouseUp(pos)
        if (pos in GetScrRect()) {
            OnMouseClick(pos)
        }
        return true
    }

    suspend fun MouseTrack(pos: IPointInt): Boolean {
        if (m_bTracking && m_bEnabled) {
            OnMouseTrack(pos)
        }
        return true
    }

    open fun OnTimer(tid: UInt) {}
    open fun OnCompose() {}
    open fun OnRectChanged(rc: IRectangleInt) {}
    open suspend fun OnMouseDown(pos: IPointInt) {}
    open suspend fun OnMouseUp(pos: IPointInt) {}
    open suspend fun OnMouseClick(pos: IPointInt) {}
    open suspend fun OnMouseTrack(pos: IPointInt) {}

    fun SetSize(nsiz: ISizeInt) {
        m_Rect.width = nsiz.width
        m_Rect.height = nsiz.height
        OnRectChanged(m_Rect)
    }

    fun SetPos(npos: IPointInt) {
        m_Rect.x = npos.x
        m_Rect.y = npos.y
        OnRectChanged(m_Rect)
    }

    fun SetRect(rc: IRectangleInt) {
        m_Rect.setTo(rc)
        OnRectChanged(m_Rect)
    }

    final override fun GetSize(): SizeInt = m_Rect.getSize()
    final override fun GetPos(): PointInt = m_Rect.getPosition()
    final override fun GetRect(): RectangleInt = RectangleInt(m_Rect)

    final override fun GetSrcPos(): PointInt {
        val res = m_Rect.getPosition()
        m_pParent?.let {
            val parentRes = it.GetSrcPos()
            res.x += parentRes.x
            res.y += parentRes.y
        }
        return res
    }

    final override fun GetScrRect(): RectangleInt {
        val res = RectangleInt(m_Rect)
        m_pParent?.let {
            val parentRes = it.GetSrcPos()
            res.x += parentRes.x
            res.y += parentRes.y
        }
        return res
    }

    final override fun GetParent(): iView? = m_pParent

    final override fun IsEnabled(): Boolean {
        return m_bEnabled && m_pParent.let { it == null || it.IsEnabled() }
    }

    fun SetEnabled(bEnabled: Boolean = true) {
        m_bEnabled = bEnabled
        Invalidate()
    }

    final override fun IsVisible(): Boolean = m_bVisible

    fun SetVisible(bVisible: Boolean = true) {
        m_bVisible = bVisible
        Invalidate()
    }

    final override fun GetClassId(): VIEWCLSID = m_clsId

    final override fun GetUID(): UInt = m_UID

    final override fun NeedRedraw(): Boolean = m_bNeedRedraw

    open fun Invalidate() {
        m_bNeedRedraw = true
        m_pParent?.Invalidate()
    }

    private fun Cleanup() {
        m_Childs.forEach { it.`$destruct`() }
        m_Childs.clear()
    }
}
