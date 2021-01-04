package com.github.servb.pph.gxlib

import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.asPoint
import com.github.servb.pph.util.contains
import com.github.servb.pph.util.helpertype.*
import com.github.servb.pph.util.invoke
import com.soywiz.korma.geom.*
import com.soywiz.korma.math.clamp

// TODO: Rename file to "standard controls"

enum class CTRL_CMD_ID {
    BUTTON_CLICK,
    BUTTON_DOUBLE_CLICK,
    TABCHANGED,
    CHECKED,
    SBLINEUP,
    SBLINEDOWN,
    SBPAGEUP,
    SBPAGEDOWN,
    SBTRACKING,
    LBSELCHANGED,
    LBSELDBLCLICK,
    EDITCHANGED,
}

interface IViewCmdHandler {

    fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int)
}

/** Base view-port based control. */
abstract class iBaseCtrl : iView {

    protected var m_pCmdHandler: IViewCmdHandler?

    constructor(
        viewMgr: iViewMgr,
        cmdHandler: IViewCmdHandler?,
        rect: IRectangleInt,
        clsId: VIEWCLSID,
        uid: UInt,
        state: Int
    ) : super(viewMgr, rect, clsId, uid, state) {
        m_pCmdHandler = cmdHandler
    }

    fun SetCommandHandler(pCmdHandler: IViewCmdHandler?) {
        m_pCmdHandler = pCmdHandler
    }

    override fun `$destruct`() {}
}

/** Generic push button. */
abstract class iButton : iBaseCtrl {

    enum class State(override val v: Int) : UniqueValueEnum {
        Pressed(0b001),
        Selected(0b010),
        Disabled(0b100);
    }

    protected var m_state: Int
    protected var m_lastClick: UInt

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler?,
        rect: IRectangleInt,
        uid: UInt,
        state: Int
    ) : super(pViewMgr, pCmdHandler, rect, VIEWCLSID.PUSH_BUTTON, uid, state) {
        m_lastClick = 0u
        m_state = 0
    }

    fun GetButtonState(): Int = m_state or (if (IsEnabled()) 0 else State.Disabled.v)

    override fun OnMouseDown(pos: IPointInt) {
        m_state = m_state or State.Pressed
        OnBtnDown()
        Invalidate()
    }

    override fun OnMouseUp(pos: IPointInt) {
        if (m_state and State.Pressed != 0) {
            m_state = m_state xor State.Pressed
            OnBtnUp()
            Invalidate()
        }
    }

    override fun OnMouseClick(pos: IPointInt) {
        m_pCmdHandler?.let {
            if (IsEnabled()) {
                val nt = GetTickCount()
                if (m_lastClick != 0u && nt in m_lastClick until (500u + m_lastClick)) {  // TODO: Extract the constant
                    m_lastClick = 0u
                    it.iCMDH_ControlCommand(this, CTRL_CMD_ID.BUTTON_DOUBLE_CLICK, 0)
                } else {
                    m_lastClick = nt
                    it.iCMDH_ControlCommand(this, CTRL_CMD_ID.BUTTON_CLICK, 0)
                }
            }
        }
    }

    override fun OnMouseTrack(pos: IPointInt) {
        if (pos in GetScrRect()) {
            if ((m_state and State.Pressed) == 0) {
                m_state = m_state or State.Pressed
                Invalidate()
            }
        } else if (m_state and State.Pressed != 0) {
            m_state = m_state xor State.Pressed
            Invalidate()
        }
    }

    protected open fun OnBtnDown() {}
    protected open fun OnBtnUp() {}
}


/** Tabbed switch control. */
abstract class iTabbedSwitch : iBaseCtrl {

    protected var m_tabStates: UIntArray
    protected var m_ItemWidth: SizeT
    protected var m_TabsCount: Int
    protected var m_CurTab: Int
    protected var m_CurFocTab: Int
    protected var m_FocTab: Int

    constructor(
        viewMgr: iViewMgr,
        cmdHandler: IViewCmdHandler?,
        rect: IRectangleInt,
        tabCount: SizeT,
        uid: UInt,
        state: Int = ViewState.Visible or ViewState.Enabled
    ) : super(viewMgr, cmdHandler, rect, VIEWCLSID.TABBED_SWITCH, uid, state) {
        m_TabsCount = tabCount
        m_CurTab = 0
        m_FocTab = -1
        m_CurFocTab = -1
        m_tabStates = UIntArray(tabCount) { 1u }
        m_ItemWidth = m_Rect.width / tabCount
        check(m_ItemWidth != 0)
        check(rect.width % tabCount == 0)
    }

    override fun `$destruct`() {}

    abstract fun ComposeTabItem(idx: Int, itemState: Int, rect: IRectangleInt)

    fun EnableTab(idx: Int, bEnable: Boolean = true) {
        check(idx < m_TabsCount)
        m_tabStates[idx] = if (bEnable) 1u else 0u
    }

    fun IsTabEnabled(idx: Int): Boolean {
        check(idx < m_TabsCount)
        return m_tabStates[idx] != 0u
    }

    fun GetTabsCount(): Int = m_TabsCount

    fun SetCurrentTab(ntab: Int) {
        check(ntab < m_TabsCount)
        m_CurTab = ntab
    }

    fun GetCurrentTab(): Int = m_CurTab
    fun GetFocusedTab(): Int = m_CurFocTab

    override fun OnMouseDown(pos: IPointInt) {
        val tabId = GetTabByPos(pos - GetScrRect().asPoint())
        if (tabId in m_tabStates.indices && m_tabStates[tabId] != 0u) {
            m_FocTab = tabId
            m_CurFocTab = tabId
        }
        Invalidate()
    }

    override fun OnMouseUp(pos: IPointInt) {
        val tab = GetTabByPos(pos - GetScrRect().asPoint())
        if (tab == m_FocTab) {
            if (m_CurTab == m_FocTab) {
                if (m_pCmdHandler != null && IsEnabled()) {
                    m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.BUTTON_CLICK, m_CurTab)
                }
            } else {
                m_CurTab = m_FocTab
                if (m_pCmdHandler != null && IsEnabled()) {
                    m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.TABCHANGED, m_CurTab)
                }
            }
            Invalidate()
        }
        m_FocTab = -1
        m_CurFocTab = -1
    }

    override fun OnMouseTrack(pos: IPointInt) {
        val tab = GetTabByPos(pos - GetScrRect().asPoint())
        if (tab != m_CurFocTab) {
            Invalidate()
        }
        m_CurFocTab = if (tab == m_FocTab) tab else -1
    }

    override fun OnCompose() {
        val rc = GetScrRect()
        rc.width = m_ItemWidth
        for (xx in 0 until m_TabsCount) {
            var state = 0
            if (xx == m_CurFocTab) {
                state = state or iButton.State.Pressed
            }
            if (xx == m_CurTab) {
                state = state or iButton.State.Selected
            }
            if (m_tabStates[xx] == 0u) {
                state = state or iButton.State.Disabled
            }
            ComposeTabItem(xx, state, rc)
            rc.x += m_ItemWidth
        }
    }

    protected fun GetTabByPos(pos: IPointInt): Int {
        if (pos !in m_Rect) {
            return -1
        }
        return pos.x / m_ItemWidth
    }
}

/** Scroll Bar. */
abstract class iScrollBar : iBaseCtrl, IViewCmdHandler {

    enum class Element {
        Bkgnd,
        Thumb,
        BtnUp,
        BtnDown,
        BtnLeft,
        BtnRight,
    }

    enum class HitTestRes {
        PgUp,
        PgDown,
        Thumb,
    }

    enum class Flags(override val v: Int) :
        EnumWithValue {  // TODO: Remove the class, seems like boolean is enough here
        Horizontal(0x1); // default is vertical
    }

    class iTBButton : iButton {

        private val m_pScrollBar: iScrollBar
        private val m_el: Element

        constructor(
            viewMgr: iViewMgr,
            cmdHandler: IViewCmdHandler?,
            rect: IRectangleInt,
            uid: UInt,
            state: Int,
            scrollBar: iScrollBar,
            el: Element
        ) : super(viewMgr, cmdHandler, rect, uid, state) {
            m_pScrollBar = scrollBar
            m_el = el
        }

        override fun OnCompose() {
            m_pScrollBar.ComposeSBElement(m_el, GetScrRect(), GetButtonState())
        }
    }

    protected class ThumbMetrix(var es: Int, var h: Int, var s1: Int, var th: Int, var s2: Int)

    protected var m_flags: Int
    protected var m_bThumbTrack: Boolean
    protected val m_trackAnchor: PointInt = PointInt()
    protected var m_trackPos: Int = 0

    protected val m_thumbMetrix: ThumbMetrix
    protected var m_totSiz: Int
    protected var m_pagSiz: Int
    protected var m_curPos: Int
    protected val m_pUp: iTBButton
    protected val m_pDown: iTBButton

    constructor(
        viewMgr: iViewMgr,
        cmdHandler: IViewCmdHandler?,
        rect: IRectangleInt,
        uid: UInt,
        flags: Int = 0
    ) : super(
        viewMgr,
        cmdHandler,
        rect,
        VIEWCLSID.SCROLL_BAR,
        uid,
        ViewState.Visible or ViewState.Enabled
    ) {
        m_flags = flags
        m_totSiz = 0
        m_pagSiz = 0
        m_curPos = 0
        m_bThumbTrack = false
        m_thumbMetrix = ThumbMetrix(-42, 42, -42, 42, -42)
        if (IsHorizontal()) {
            m_pUp = iTBButton(
                viewMgr, this, IRectangleInt(0, 0, GetDefBtnSiz(), rect.height),
                uid + 1u, ViewState.Visible or ViewState.Enabled, this, Element.BtnLeft,
            ).also {
                AddChild(it)
            }

            m_pDown = iTBButton(
                viewMgr, this,
                IRectangleInt(rect.width - GetDefBtnSiz(), 0, GetDefBtnSiz(), rect.height), uid + 2u,
                ViewState.Visible or ViewState.Enabled, this, Element.BtnRight,
            ).also {
                AddChild(it)
            }
        } else {
            m_pUp = iTBButton(
                viewMgr, this, IRectangleInt(0, 0, rect.width, GetDefBtnSiz()), uid + 1u,
                ViewState.Visible or ViewState.Enabled, this, Element.BtnUp,
            ).also {
                AddChild(it)
            }

            m_pDown = iTBButton(
                viewMgr, this,
                IRectangleInt(0, rect.height - GetDefBtnSiz(), rect.width, GetDefBtnSiz()), uid + 2u,
                ViewState.Visible or ViewState.Enabled, this, Element.BtnDown,
            ).also {
                AddChild(it)
            }
        }
        CalcThumbMetrix()
    }

    override fun `$destruct`() {}

    fun SetMetrics(totSiz: Int, pagSiz: Int) {
        m_totSiz = totSiz
        m_pagSiz = pagSiz
        m_curPos = 0
        CalcThumbMetrix()
    }

    fun SetCurPos(nVal: Int): Boolean {
        val nVal = nVal.clamp(0, maxOf(0, m_totSiz - m_pagSiz))
        if (m_totSiz == 0 || nVal == m_curPos) {
            return false
        }
        m_curPos = nVal
        CalcThumbMetrix()
        return true
    }

    fun PageUp(): Boolean {
        if (SetCurPos(m_curPos - m_pagSiz)) {
            m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBPAGEUP, m_curPos)
            Invalidate()
            return true
        }
        return false
    }

    fun PageDown(): Boolean {
        if (SetCurPos(m_curPos + m_pagSiz)) {
            m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBPAGEDOWN, m_curPos)
            Invalidate()
            return true
        }
        return false
    }

    fun CurPos(): Int = m_curPos
    fun MaxPos(): Int = m_totSiz - m_pagSiz
    fun IsHorizontal(): Boolean = (m_flags and Flags.Horizontal) != 0
    fun GetDefBtnSiz(): Int = 19

    abstract fun ComposeSBElement(el: Element, rc: IRectangleInt, flags: Int)

    override fun OnMouseDown(pos: IPointInt) {
        val res = HitTest(pos - GetScrRect().asPoint())

        if (res == HitTestRes.Thumb) {
            m_bThumbTrack = true
            m_trackAnchor.setTo(pos)
            m_trackPos = m_curPos
            Invalidate()
        } else if (res == HitTestRes.PgUp) {
            if (PageUp()) {
                Invalidate()
            }
        } else if (res == HitTestRes.PgDown) {
            if (PageDown()) {
                Invalidate()
            }
        }
    }

    override fun OnMouseUp(pos: IPointInt) {
        m_bThumbTrack = false
        Invalidate()
    }

    override fun OnMouseTrack(pos: IPointInt) {
        if (m_bThumbTrack) {
            val n = if (IsHorizontal()) pos.x - m_trackAnchor.x else pos.y - m_trackAnchor.y
            if (SetCurPos(m_trackPos + NItems(n))) {
                m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBTRACKING, m_curPos)
                Invalidate()
            }
        }
    }

    override fun OnCompose() {
        fun flags(): Int = (if (m_bEnabled) 0 else iButton.State.Disabled.v) or
                (if (m_bThumbTrack) iButton.State.Pressed.v else 0)

        val rc = RectangleInt(GetScrRect())
        if (IsHorizontal()) {
            ComposeSBElement(
                Element.Bkgnd,
                IRectangleInt(rc.x + m_thumbMetrix.es, rc.y, m_thumbMetrix.h, rc.height), 0
            )
            ComposeSBElement(
                Element.Thumb,
                IRectangleInt(rc.x + m_thumbMetrix.es + m_thumbMetrix.s1, rc.y, m_thumbMetrix.th, rc.height), flags()
            )
        } else {
            ComposeSBElement(Element.Bkgnd, IRectangleInt(rc.x, rc.y + m_thumbMetrix.es, rc.width, m_thumbMetrix.h), 0)
            ComposeSBElement(
                Element.Thumb,
                IRectangleInt(rc.x, rc.y + m_thumbMetrix.es + m_thumbMetrix.s1, rc.width, m_thumbMetrix.th), flags()
            )
        }
    }

    protected fun HitTest(pos: IPointInt): HitTestRes {
        var value = if (IsHorizontal()) {
            pos.x - m_thumbMetrix.es
        } else {
            pos.y - m_thumbMetrix.es
        }
        if (value < m_thumbMetrix.s1) {
            return HitTestRes.PgUp
        }
        value -= m_thumbMetrix.s1
        if (value < m_thumbMetrix.th) {
            return HitTestRes.Thumb
        }
        return HitTestRes.PgDown
    }

    protected fun ItemsHeight(itemCount: Int): Int = (m_thumbMetrix.h.toInt() * itemCount) / m_totSiz
    protected fun NItems(n: Int): Int = (m_totSiz * n) / m_thumbMetrix.h.toInt()

    protected fun CalcThumbMetrix() {
        if (IsHorizontal()) {
            m_thumbMetrix.es = GetDefBtnSiz()
            m_thumbMetrix.h = m_Rect.width - m_thumbMetrix.es * 2
        } else {
            m_thumbMetrix.es = GetDefBtnSiz()
            m_thumbMetrix.h = m_Rect.height - m_thumbMetrix.es * 2
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
        if (uid == m_pUp.GetUID()) {
            // Scroll up
            if (SetCurPos(m_curPos - 1)) {
                m_pCmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBLINEUP, m_curPos)
            }
        } else if (uid == m_pDown.GetUID()) {
            // Scroll down
            if (SetCurPos(m_curPos + 1)) {
                m_pCmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBLINEDOWN, m_curPos)
            }
        }
    }
}

/** List Box (virtual). */
abstract class iListBox : iBaseCtrl, IViewCmdHandler {

    protected var m_pScrollBar: iScrollBar?
    protected var m_scrVal: Int
    protected var m_selItem: Int
    protected var m_lcTime: UInt
    protected var m_lcIdx: Int

    constructor(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?, rect: IRectangleInt, uid: UInt) : super(
        pViewMgr, pCmdHandler, rect, VIEWCLSID.LIST_BOX, uid, ViewState.Visible or ViewState.Enabled
    ) {
        m_pScrollBar = null
        m_selItem = -1
        m_scrVal = -1
        m_lcTime = 0u
        m_lcIdx = -1
    }

    override fun `$destruct`() {}

    fun SetScrollBar(pScrollBar: iScrollBar?) {
        m_pScrollBar = pScrollBar
        m_pScrollBar?.let {
            it.SetCommandHandler(this)
            it.SetMetrics(LBItemsCount(), PageSize())
            it.SetCurPos(m_scrVal)
        }
    }

    fun UpdateListBox() {
        m_scrVal = 0
        m_pScrollBar!!.SetMetrics(LBItemsCount(), PageSize())
        m_pScrollBar!!.SetCurPos(m_scrVal)
        m_selItem = if (LBItemsCount() != 0) 0 else -1
        m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.LBSELCHANGED, m_selItem)
        Invalidate()
    }

    fun SetCurPos(sval: Int) {
        m_scrVal = sval
    }

    fun SetCurSel(idx: Int, notify: Boolean) {
        require(idx == -1 || IsValidIdx(idx)) { "Not valid idx: $idx" }
        if (idx != m_selItem) {
            m_selItem = idx
            if (notify) {
                m_pCmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.LBSELCHANGED, m_selItem)
            }
            if (idx != -1) {
                if (m_selItem < m_scrVal) {
                    m_pScrollBar?.SetCurPos(m_selItem)
                    m_scrVal = m_selItem
                } else if (m_selItem > m_scrVal + PageSize() - 1) {
                    val npos = m_selItem - PageSize() + 1
                    m_pScrollBar?.SetCurPos(npos)
                    m_scrVal = npos
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

    fun SelItem(): Int = m_selItem
    fun PageSize(): SizeT = m_Rect.height / LBItemHeight()

    abstract fun ComposeLBBackground(rect: IRectangleInt)
    abstract fun ComposeLBItem(iIdx: Int, bSel: Boolean, irc: IRectangleInt)
    abstract fun LBItemHeight(): SizeT
    abstract fun LBItemsCount(): SizeT

    override fun OnMouseDown(pos: IPointInt) {
        var nIdx = IdxByPos(pos)
        if (!IsValidIdx(nIdx)) {
            nIdx = -1
        }
        SetCurSel(nIdx, true)
    }

    override fun OnMouseClick(pos: IPointInt) {
        m_pCmdHandler?.let {
            val nt = GetTickCount()
            if (m_lcTime != 0u && m_lcTime in (nt - 500u) until nt && m_selItem != -1 && m_lcIdx == m_selItem) {
                m_lcTime = 0u
                m_lcIdx = -1
                it.iCMDH_ControlCommand(this, CTRL_CMD_ID.LBSELDBLCLICK, m_selItem)
            } else {
                m_lcTime = nt
                m_lcIdx = m_selItem
            }
        }
    }

    override fun OnCompose() {
        val rc = GetScrRect()
        ComposeLBBackground(rc)
        val ih = LBItemHeight()
        val ic = LBItemsCount()
        val ps = PageSize()
        val irc = RectangleInt(rc.x, rc.y, rc.width, ih)
        for (xx in m_scrVal until minOf(ic, m_scrVal + ps)) {
            ComposeLBItem(xx, xx == m_selItem, irc)
            irc.y += ih
        }
    }

    override fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        require(m_pScrollBar !== null && m_pScrollBar === pView)
        m_scrVal = param
    }

    protected fun IsValidIdx(idx: Int) = idx < LBItemsCount()
    protected fun IdxByPos(pos: IPointInt): Int = m_scrVal + (pos.y - GetScrRect().y) / LBItemHeight()
}
