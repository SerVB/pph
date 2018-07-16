package com.github.servb.pph.gxlib

import com.github.servb.pph.gxlib.gxlcommontpl.iCLAMP
import com.github.servb.pph.gxlib.gxlmetrics.*
import com.github.servb.pph.gxlib.gxltimer.GetTickCount
import com.github.servb.pph.gxlib.gxlview.VIEWCLSID
import com.github.servb.pph.gxlib.gxlview.iView
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import unsigned.Uint
import unsigned.minus
import unsigned.plus
import unsigned.ui

// TODO: Rename file to "standard controls"

enum class CTRL_CMD_ID {
    CCI_BTNCLICK,
    CCI_BTNDBLCLICK,
    CCI_TABCHANGED,
    CCI_CHECKED,
    CCI_SBLINEUP,
    CCI_SBLINEDOWN,
    CCI_SBPAGEUP,
    CCI_SBPAGEDOWN,
    CCI_SBTRACKING,
    CCI_LBSELCHANGED,
    CCI_LBSELDBLCLICK,
    CCI_EDITCHANGED;
}

interface IViewCmdHandler {
    fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int)
}

/** Base view-port based control. */
abstract class iBaseCtrl(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?, rect: Rect,
                         clsId: VIEWCLSID, uid: Uint, state: Uint) : iView(pViewMgr, rect, clsId, uid, state) {
    protected var m_pCmdHandler: IViewCmdHandler? = pCmdHandler

    fun SetCommandHandler(pCmdHandler: IViewCmdHandler) {
        m_pCmdHandler = pCmdHandler
    }
}

/** Generic push button. */
open class iButton(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?, rect: Rect, uid: Uint, state: Uint)
    : iBaseCtrl(pViewMgr, pCmdHandler, rect, VIEWCLSID.PUSH_BUTTON, uid, state) {
    private var m_state: Int = 0
    private var m_lastClick: Uint = 0.ui

    enum class State(override val v: Int) : UniqueValueEnum {
        Pressed(0b001),
        Selected(0b010),
        Disabled(0b100);
    }

    fun GetButtonState() = m_state or if (IsEnabled()) 0 else State.Disabled.v

    private fun OnMouseDown(pos: Point) {
        m_state = m_state or State.Pressed.v
        OnBtnDown()
        Invalidate()
    }

    private fun OnMouseUp(pos: Point) {
        if (m_state and State.Pressed.v != 0) {
            m_state = m_state xor State.Pressed.v
            OnBtnUp()
            Invalidate()
        }
    }

    private fun OnMouseClick(pos: Point) {
        if (m_pCmdHandler != null && IsEnabled()) {
            val nt = GetTickCount()
            if (m_lastClick != 0.ui && nt > m_lastClick && nt - m_lastClick < 500) {
                m_lastClick = 0.ui
                m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_BTNDBLCLICK, 0)
            } else {
                m_lastClick = nt
                m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_BTNCLICK, 0)
            }
        }
    }

    private fun OnMouseTrack(pos: Point) {
        if (GetScrRect().PtInRect(pos)) {
            if (m_state and State.Pressed.v != State.Pressed.v) {
                m_state = m_state or State.Pressed.v
                Invalidate()
            }
        } else if (m_state and State.Pressed.v != 0) {
            m_state = m_state xor State.Pressed.v
            Invalidate()
        }
    }

    protected open fun OnBtnDown() {}
    protected open fun OnBtnUp() {}
}


/** Tabbed switch control. */
abstract class iTabbedSwitch(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?,
                             rect: Rect, tabcnt: Int, uid: Uint, state: Uint = Visible or Enabled)
    : iBaseCtrl(pViewMgr, pCmdHandler, rect, VIEWCLSID.TABBED_SWITCH, uid, state) {
    protected var m_tabStates: Array<Uint> // uint32*
    protected var m_ItemWidth: Uint
    protected var m_TabsCount: Int = tabcnt
    protected var m_CurTab: Int = 0
    protected var m_CurFocTab: Int = -1
    protected var m_FocTab: Int = -1

    init {
        m_tabStates = Array(tabcnt) { 1.ui }
        m_ItemWidth = m_Rect.w / m_TabsCount
        check(m_ItemWidth != 0.ui)
        check(rect.w % m_TabsCount == 0.ui)
    }

    // Pure methods
    abstract fun ComposeTabItem(idx: Int, itemState: Int, rect: Rect)

    // inlines
    fun EnableTab(idx: Int, bEnable: Boolean = true) {
        check(idx < m_TabsCount)
        m_tabStates[idx] = if (bEnable) 1.ui else 0.ui
    }

    fun IsTabEnabled(idx: Int): Boolean {
        check(idx < m_TabsCount)
        return m_tabStates[idx] != 0.ui
    }

    fun GetTabsCount() = m_TabsCount

    fun SetCurrentTab(ntab: Int) {
        check(ntab < m_TabsCount)
        m_CurTab = ntab
    }

    fun GetCurrentTab() = m_CurTab

    fun GetFocusedTab() = m_CurFocTab

    private fun OnMouseDown(pos: Point) {
        val ntab = GetTabByPos(pos - GetScrRect().Point())
        if (ntab in 0..(m_TabsCount - 1) && m_tabStates[ntab] != 0.ui) {
            m_FocTab = ntab
            m_CurFocTab = ntab
        }
        Invalidate()
    }

    private fun OnMouseUp(pos: Point) {
        val tab = GetTabByPos(pos - GetScrRect().point())
        if (tab == m_FocTab) {
            if (m_CurTab == m_FocTab) {
                if (m_pCmdHandler != null && IsEnabled()) {
                    m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_BTNCLICK, m_CurTab)
                }
            } else {
                m_CurTab = m_FocTab
                if (m_pCmdHandler != null && IsEnabled()) {
                    m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_TABCHANGED, m_CurTab)
                }
            }
            Invalidate()
        }
        m_FocTab = -1
        m_CurFocTab = -1
    }

    private fun OnMouseTrack(pos: Point) {
        val tab = GetTabByPos(pos - GetScrRect().Point())
        if (tab != m_CurFocTab) {
            Invalidate()
        }
        m_CurFocTab = if (tab == m_FocTab) tab else -1
    }

    override fun OnCompose() {
        val rc = GetScrRect()
        rc.w = m_ItemWidth
        for (xx in 0 until m_TabsCount) {
            var state = 0
            if (xx == GetFocusedTab()) {
                state = state or iButton.State.Pressed.v
            }
            if (xx == GetCurrentTab()) {
                state = state or iButton.State.Selected.v
            }
            if (m_tabStates[xx] == 0.ui) {
                state = state or iButton.State.Disabled.v
            }
            ComposeTabItem(xx, state, Rect(rc))
            rc.x += m_ItemWidth
        }
    }

    protected fun GetTabByPos(pos: Point): Int {
        if (!Rect(m_Rect.Size()).PtInRect(pos)) {
            return -1
        }
        return pos.x / m_ItemWidth.v
    }
}

/** Scroll Bar. */
abstract class iScrollBar(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?, rect: Rect, uid: Uint, flags: Int = 0)
    : iBaseCtrl(pViewMgr, pCmdHandler, rect, VIEWCLSID.SCROLL_BAR,
        uid, ViewState.Visible.v or ViewState.Enabled.v), IViewCmdHandler {
    protected var m_flags: Int = flags
    protected var m_bThumbTrack: Boolean = false
    protected abstract var m_trackAnchor: MutablePoint
    protected abstract var m_trackPos: Int

    protected var m_thumbMetrix: ThumbMetrix = ThumbMetrix(-42, -42, -42, -42, -42)
    protected var m_totSiz: Int = 0
    protected var m_pagSiz: Int = 0
    protected var m_curPos: Int = 0
    protected var m_pUp: iTBButton?
    protected var m_pDown: iTBButton?

    init {
        if (IsHorizontal()) {
            val up = iTBButton(pViewMgr, this, Rect(0, 0, GetDefBtnSiz(), rect.h),
                    uid + 1, ViewState.Visible.v or ViewState.Enabled.v, this, Element.EL_BtnLeft)
            AddChild(up)
            m_pUp = up
            val down = iTBButton(pViewMgr, this,
                    Rect(rect.w.v - GetDefBtnSiz().v, 0, GetDefBtnSiz(), rect.h),uid + 2,
                    ViewState.Visible.v or ViewState.Enabled.v, this, Element.EL_BtnRight)
            AddChild(down)
            m_pDown = down
        } else {
            val up = iTBButton(pViewMgr, this, Rect(0, 0, rect.w, GetDefBtnSiz()), uid + 1,
                    ViewState.Visible.v or ViewState.Enabled.v, this, Element.EL_BtnUp)
            AddChild(up)
            m_pUp = up
            val down = iTBButton(pViewMgr, this,
                    Rect(0, rect.h.v - GetDefBtnSiz().v, rect.w, GetDefBtnSiz()), uid + 2,
                    ViewState.Visible.v or ViewState.Enabled.v, this, Element.EL_BtnDown)
            AddChild(down)
            m_pDown = down
        }
        CalcThumbMetrix()
    }

    protected data class ThumbMetrix(var es: Int, var h: Int, var s1: Int, var th: Int, var s2: Int)

    enum class Element {
        EL_Bkgnd,
        EL_Thumb,
        EL_BtnUp,
        EL_BtnDown,
        EL_BtnLeft,
        EL_BtnRight;
    }

    enum class HitTestRes {
        HTR_PgUp,
        HTR_PgDown,
        HTR_Thumb;
    }

    enum class Flags(val v: Int) {
        Horizontal(0x1); // default is vertical
    }

    class iTBButton(pViewMgr: iViewMgr?, pCmdHandler: IViewCmdHandler?, rect: Rect,
                    uid: Uint, state: Uint, pScrollBar: iScrollBar, el: Element)
        : iButton(pViewMgr, pCmdHandler, rect, uid, state) {
        private val m_el: Element = el
        private val m_pScrollBar: iScrollBar = pScrollBar

        override fun OnCompose() {
            m_pScrollBar.ComposeSBElement(m_el, Rect(GetScrRect()), GetButtonState())
        }
    }

    fun SetMetrics(totSiz: Int, pagSiz: Int) {
        m_totSiz = totSiz
        m_pagSiz = pagSiz
        m_curPos = 0
        CalcThumbMetrix()
    }

    fun SetCurPos(nVal: Int): Boolean {
        val nVal = iCLAMP(0, maxOf(0, m_totSiz - m_pagSiz), nVal)
        if (m_totSiz == 0 || nVal == m_curPos) {
            return false
        }
        m_curPos = nVal
        CalcThumbMetrix()
        return true
    }

    fun PageUp(): Boolean {
        if (SetCurPos(m_curPos - m_pagSiz)) {
            m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_SBPAGEUP, m_curPos)
            Invalidate()
            return true
        }
        return false
    }

    fun PageDown(): Boolean {
        if (SetCurPos(m_curPos + m_pagSiz)) {
            m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_SBPAGEDOWN, m_curPos)
            Invalidate()
            return true
        }
        return false
    }

    fun CurPos() = m_curPos
    fun MaxPos() = m_totSiz - m_pagSiz
    fun IsHorizontal() = (m_flags and Flags.Horizontal.v) != 0
    fun GetDefBtnSiz() = 19.ui

    // Pure methods
    abstract fun ComposeSBElement(el: Element, rc: Rect, flags: Int)

    private fun OnMouseDown(pos: Point) {
        val res = HitTest(pos - GetScrRect().Point());

        if (res == HitTestRes.HTR_Thumb) {
            m_bThumbTrack = true
            m_trackAnchor = MutablePoint(pos)
            m_trackPos = m_curPos
            Invalidate()
        } else if (res == HitTestRes.HTR_PgUp){
            if (PageUp()) {
                Invalidate()
            }
        } else if (res == HitTestRes.HTR_PgDown){
            if (PageDown()) {
                Invalidate()
            }
        }
    }

    private fun OnMouseUp(pos: Point) {
        m_bThumbTrack = false
        Invalidate()
    }

    private fun OnMouseTrack(pos: Point) {
        if (m_bThumbTrack) {
            val n = if (IsHorizontal()) pos.x - m_trackAnchor.x else pos.y - m_trackAnchor.y
            if (SetCurPos(m_trackPos + NItems(n))) {
                m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_SBTRACKING, m_curPos)
                Invalidate()
            }
        }
    }

    override fun OnCompose() {
        fun flags() = if (m_bEnabled) 0 else iButton.State.Disabled.v or
                if (m_bThumbTrack) iButton.State.Pressed.v else 0

        val rc = MutableRect(GetScrRect())
        if (IsHorizontal()) {
            ComposeSBElement(Element.EL_Bkgnd,
                    Rect(rc.x + m_thumbMetrix.es, rc.y, m_thumbMetrix.h.ui, rc.h), 0)  // TODO: m_thumbMetrix.w??
            ComposeSBElement(Element.EL_Thumb,
                    Rect(rc.x + m_thumbMetrix.es + m_thumbMetrix.s1, rc.y, m_thumbMetrix.th.ui, rc.h), flags())
        } else {
            ComposeSBElement(Element.EL_Bkgnd, Rect(rc.x, rc.y + m_thumbMetrix.es, rc.w, m_thumbMetrix.h.ui), 0)
            ComposeSBElement(Element.EL_Thumb,
                    Rect(rc.x, rc.y + m_thumbMetrix.es + m_thumbMetrix.s1, rc.w, m_thumbMetrix.th.ui), flags())
        }
    }


    protected fun HitTest(pos: Point): HitTestRes {
        var value = if (IsHorizontal()) { pos.x - m_thumbMetrix.es } else { pos.y - m_thumbMetrix.es }
        if (value < m_thumbMetrix.s1) {
            return HitTestRes.HTR_PgUp
        }
        value -= m_thumbMetrix.s1
        if (value < m_thumbMetrix.th) {
            return HitTestRes.HTR_Thumb
        }
        return HitTestRes.HTR_PgDown
    }

    protected fun ItemsHeight(icnt: Int) = (m_thumbMetrix.h * icnt) / m_totSiz
    protected fun NItems(n: Int) = (m_totSiz * n) / m_thumbMetrix.h

    protected fun CalcThumbMetrix() {
        if (IsHorizontal()){
            m_thumbMetrix.es = GetDefBtnSiz().v
            m_thumbMetrix.h = m_Rect.w.v - m_thumbMetrix.es * 2
        } else {
            m_thumbMetrix.es = GetDefBtnSiz().v
            m_thumbMetrix.h = m_Rect.h.v - m_thumbMetrix.es * 2
        }

        if (m_totSiz == 0) {
            m_thumbMetrix.th = m_thumbMetrix.h
            m_thumbMetrix.s2 = 0
            m_thumbMetrix.s1 = 0
        } else {
            m_thumbMetrix.th = maxOf(ItemsHeight(minOf(m_pagSiz, m_totSiz)), m_thumbMetrix.es)
            if (m_curPos == 0) {
                m_thumbMetrix.s1 = 0
            } else {
                val fpx = m_thumbMetrix.h - m_thumbMetrix.th
                m_thumbMetrix.s1 = (fpx * m_curPos) / (m_totSiz - m_pagSiz)
            }
            m_thumbMetrix.s2 = m_thumbMetrix.h - m_thumbMetrix.th - m_thumbMetrix.s1
        }
    }

    override fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID()
        if (uid == m_UID + 1) {
            // Scroll up
            if (SetCurPos(m_curPos - 1)) {
                m_pCmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_SBLINEUP, m_curPos)
            }
        } else if (uid == m_UID + 2) {
            // Scroll down
            if (SetCurPos(m_curPos + 1)) {
                m_pCmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_SBLINEDOWN, m_curPos)
            }
        }
    }
}

/** List Box (virtual). */
abstract class iListBox(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?, rect: Rect, uid: Uint)
    : iBaseCtrl(pViewMgr, pCmdHandler, rect, VIEWCLSID.LIST_BOX, uid, ViewState.Visible.v or ViewState.Enabled.v),
        IViewCmdHandler {
    protected var m_pScrollBar: iScrollBar? = null
    protected var m_scrVal: Int = 0
    protected var m_selItem: Int = -1
    protected var m_lcTime: Uint = 0.ui
    protected var m_lcIdx: Int = -1

    fun SetScrollBar(pScrollBar: iScrollBar?) {
        m_pScrollBar = pScrollBar

        m_pScrollBar?.apply {
            SetCommandHandler(this)
            SetMetrics(LBItemsCount(), PageSize().v)
            SetCurPos(m_scrVal)
        }
    }

    fun UpdateListBox() {
        m_scrVal = 0
        m_pScrollBar!!.SetMetrics(LBItemsCount(), PageSize().v)
        m_pScrollBar!!.SetCurPos(m_scrVal)
        m_selItem = if (LBItemsCount() != 0) 0 else -1
        m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_LBSELCHANGED, m_selItem)
        Invalidate()
    }

    fun SetCurPos(sval: Int) {
        m_scrVal = sval
    }

    fun SetCurSel(idx: Int, bNotify: Boolean) {
        require(idx == -1 || IsValidIdx(idx)) { "Not valid idx: $idx" }
        if (idx != m_selItem) {
            m_selItem = idx
            if (bNotify) {
                m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_LBSELCHANGED, m_selItem)
            }
            if (idx != -1) {
                if (m_selItem < m_scrVal) {
                    m_pScrollBar?.SetCurPos(m_selItem)
                    SetCurPos(m_selItem)
                } else if (m_selItem > m_scrVal + PageSize() - 1) {
                    val npos = m_selItem - PageSize() + 1
                    m_pScrollBar?.SetCurPos(npos)
                    SetCurPos(npos)
                }
            }
            Invalidate()
        }
    }

    fun SelPrev() {
        if (m_selItem > 0) {
            SetCurSel(m_selItem - 1, true)
        }
    }

    fun SelNext() {
        if (m_selItem + 1 < LBItemsCount()) {
            SetCurSel(m_selItem + 1, true)
        }
    }

    fun SelItem() = m_selItem
    fun PageSize() = m_Rect.h / LBItemHeight()

    // Pure methods
    abstract fun ComposeLBBackground(rect: Rect)
    abstract fun ComposeLBItem(iIdx: Uint, bSel: Boolean, irc: Rect)
    abstract fun LBItemHeight(): Int
    abstract fun LBItemsCount(): Int

    private fun OnMouseDown(pos: Point) {
        var nIdx = IdxByPos(pos)
        if (!IsValidIdx(nIdx)) {
            nIdx = -1
        }
        SetCurSel(nIdx, true)
    }

    private fun OnMouseClick(pos: Point) {
        m_pCmdHandler?.let {
            val nt = GetTickCount()
            if (m_lcTime != 0.ui && nt > m_lcTime && (nt-m_lcTime) < 500 && m_selItem != -1 && m_lcIdx == m_selItem) {
                m_lcTime = 0.ui
                m_lcIdx = -1
                it.iCMDH_ControlCommand(this, CTRL_CMD_ID.CCI_LBSELDBLCLICK, m_selItem)
            } else {
                m_lcTime = nt
                m_lcIdx = m_selItem
            }
        }
    }

    override fun OnCompose() {
        val rc = GetScrRect()
        ComposeLBBackground(Rect(rc))
        val ih = LBItemHeight()
        val ic = LBItemsCount()
        val ps = PageSize()
        val irc = MutableRect(rc.x, rc.y, rc.w, ih.ui)
        for (xx in m_scrVal until minOf(ic, m_scrVal + ps)){
            ComposeLBItem(xx.ui, xx == m_selItem, Rect(irc))
            irc.y += ih
        }
    }

    override fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        require(m_pScrollBar !== null && m_pScrollBar === pView)
        SetCurPos(param)
    }

    protected fun IsValidIdx(idx: Int) = idx < LBItemsCount()
    protected fun IdxByPos(pos: XYHolder): Int = m_scrVal + (pos.y - GetScrRect().y) / LBItemHeight()
}
