package com.github.servb.pph.gxlib

import com.github.servb.pph.gxlib.gxlcommontpl.iCLAMP
import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.gxlmetrics.Pointc
import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxltimer.GetTickCount
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr
import com.github.servb.pph.util.helpertype.UniqueValueEnum

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

@ExperimentalUnsignedTypes
interface IViewCmdHandler {
    fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int)
}

/** Base view-port based control. */
@ExperimentalUnsignedTypes
abstract class iBaseCtrl(viewMgr: iViewMgr, var cmdHandler: IViewCmdHandler?, rect: Rect,
                         clsId: ViewClassId, uid: UInt, state: Set<ViewState>)
    : iView(viewMgr, rect, clsId, uid, state)

/** Generic push button. */
@ExperimentalUnsignedTypes
open class iButton(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?, rect: Rect, uid: UInt, state: Set<ViewState>)
    : iBaseCtrl(pViewMgr, pCmdHandler, rect, ViewClassId.PUSH_BUTTON, uid, state) {

    private var state: Set<State> = emptySet()
    private var lastClick: Int = 0

    enum class State(override val v: Int) : UniqueValueEnum {
        PRESSED(0b001),
        SELECTED(0b010),
        DISABLED(0b100);
    }

    fun getButtonState() = state + if (isEnabled) emptySet() else setOf(State.DISABLED)

    private fun onMouseDown(pos: Point) {
        state = state + setOf(State.PRESSED)
        onBtnDown()
        invalidate()
    }

    private fun onMouseUp(pos: Point) {
        if (State.PRESSED in state) {
            state = state - setOf(State.PRESSED)
            onBtnUp()
            invalidate()
        }
    }

    private fun onMouseClick(pos: Point) {
        if (cmdHandler != null && isEnabled) {
            val nt = GetTickCount()
            if (lastClick != 0 && nt in lastClick until (500 + lastClick)) {  // TODO: Extract the constant
                lastClick = 0
                cmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.BUTTON_DOUBLE_CLICK, 0)
            } else {
                lastClick = nt
                cmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.BUTTON_CLICK, 0)
            }
        }
    }

    private fun onMouseTrack(pos: Point) {
        if (screenRect.PtInRect(pos)) {
            if (State.PRESSED !in state) {
                state = state + setOf(State.PRESSED)
                invalidate()
            }
        } else if (State.PRESSED in state) {
            state = state - setOf(State.PRESSED)
            invalidate()
        }
    }

    protected open fun onBtnDown() {}
    protected open fun onBtnUp() {}
}


/** Tabbed switch control. */
@ExperimentalUnsignedTypes
abstract class iTabbedSwitch(viewMgr: iViewMgr, cmdHandler: IViewCmdHandler?,
                             rect: Rect, tabCount: Int, uid: UInt,
                             state: Set<ViewState> = setOf(ViewState.Visible, ViewState.Enabled))
    : iBaseCtrl(viewMgr, cmdHandler, rect, ViewClassId.TABBED_SWITCH, uid, state) {

    protected var tabStates: UIntArray = UIntArray(tabCount) { 1u }

    var tabCount = tabCount
        protected set

    protected var itemWidth: UInt = this.relativeRect.w / tabCount.toUInt()

    var currentTab: Int = 0
        set(value) {
            check(value < tabCount)
            field = value
        }

    protected var currentFocusedTab: Int = -1

    var focusedTab: Int = -1
        protected set

    init {
        check(itemWidth != 0u)
        check(rect.w % tabCount.toUInt() == 0u)
    }

    abstract fun composeTabItem(idx: Int, itemState: Int, rect: Rect)

    fun enableTab(idx: Int, bEnable: Boolean = true) {
        check(idx < tabCount)
        tabStates[idx] = if (bEnable) 1u else 0u
    }

    fun isTabEnabled(idx: Int): Boolean {
        check(idx < tabCount)
        return tabStates[idx] != 0u
    }

    private fun onMouseDown(pos: Point) {
        val tabId = getTabByPos(pos - screenRect.Point())
        if (tabId in tabStates.indices && tabStates[tabId] != 0u) {
            focusedTab = tabId
            currentFocusedTab = tabId
        }
        invalidate()
    }

    private fun onMouseUp(pos: Point) {
        val tab = getTabByPos(pos - screenRect.toPoint())
        if (tab == focusedTab) {
            if (currentTab == focusedTab) {
                if (cmdHandler != null && isEnabled) {
                    cmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.BUTTON_CLICK, currentTab)
                }
            } else {
                currentTab = focusedTab
                if (cmdHandler != null && isEnabled) {
                    cmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.TABCHANGED, currentTab)
                }
            }
            invalidate()
        }
        focusedTab = -1
        currentFocusedTab = -1
    }

    private fun onMouseTrack(pos: Point) {
        val tab = getTabByPos(pos - screenRect.Point())
        if (tab != currentFocusedTab) {
            invalidate()
        }
        currentFocusedTab = if (tab == focusedTab) tab else -1
    }

    override fun onCompose() {
        val rc = screenRect
        rc.w = itemWidth
        for (xx in 0 until tabCount) {
            var state = 0
            if (xx == currentFocusedTab) {
                state = state or iButton.State.PRESSED.v
            }
            if (xx == currentTab) {
                state = state or iButton.State.SELECTED.v
            }
            if (tabStates[xx] == 0u) {
                state = state or iButton.State.DISABLED.v
            }
            composeTabItem(xx, state, Rect(rc))
            rc.x += itemWidth.toInt()
        }
    }

    protected fun getTabByPos(pos: Point): Int {
        if (!Rect(relativeRect.Size()).PtInRect(pos)) {
            return -1
        }
        return pos.x / itemWidth.toInt()
    }
}

/** Scroll Bar. */
@ExperimentalUnsignedTypes
abstract class iScrollBar(viewMgr: iViewMgr, cmdHandler: IViewCmdHandler?, rect: Rect, uid: UInt,
                          protected var flags: Int = 0)
    : iBaseCtrl(viewMgr, cmdHandler, rect, ViewClassId.SCROLL_BAR, uid, setOf(ViewState.Visible, ViewState.Enabled)),
        IViewCmdHandler {

    protected var thumbTrack: Boolean = false
    protected abstract var trackAnchor: Point
    protected abstract var trackPos: Int

    protected var thumbMetrix: ThumbMetrix = ThumbMetrix(-42, 42u, -42, 42u, -42)
    protected var totalSiz: Int = 0
    protected var pagSiz: Int = 0

    var curPos: Int = 0
        protected set

    protected var pUp: iTBButton
    protected var pDown: iTBButton

    init {
        if (isHorizontal) {
            pUp = iTBButton(viewMgr, this, Rect(0, 0, defBtnSiz.toUInt(), rect.h),
                    uid + 1u, setOf(ViewState.Visible, ViewState.Enabled), this, Element.BtnLeft).also {
                addChild(it)
            }

            pDown = iTBButton(viewMgr, this,
                    Rect(rect.w.toInt() - defBtnSiz, 0, defBtnSiz.toUInt(), rect.h), uid + 2u,
                    setOf(ViewState.Visible, ViewState.Enabled), this, Element.BtnRight).also {
                addChild(it)
            }
        } else {
            pUp = iTBButton(viewMgr, this, Rect(0, 0, rect.w, defBtnSiz.toUInt()), uid + 1u,
                    setOf(ViewState.Visible, ViewState.Enabled), this, Element.BtnUp).also {
                addChild(it)
            }

            pDown = iTBButton(viewMgr, this,
                    Rect(0, rect.h.toInt() - defBtnSiz, rect.w, defBtnSiz.toUInt()), uid + 2u,
                    setOf(ViewState.Visible, ViewState.Enabled), this, Element.BtnDown).also {
                addChild(it)
            }
        }
        calcThumbMetrix()
    }

    protected data class ThumbMetrix(var es: Int, var h: UInt, var s1: Int, var th: UInt, var s2: Int)

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

    enum class Flags(val v: Int) {  // TODO: Remove the class
        Horizontal(0x1); // default is vertical
    }

    class iTBButton(viewMgr: iViewMgr, cmdHandler: IViewCmdHandler?, rect: Rect,
                    uid: UInt, state: Set<ViewState>, private val scrollBar: iScrollBar, private val el: Element)
        : iButton(viewMgr, cmdHandler, rect, uid, state) {

        override fun onCompose() {
            scrollBar.composeSBElement(el, Rect(screenRect), getButtonState())
        }
    }

    fun setMetrics(totSiz: Int, pagSiz: Int) {
        this.totalSiz = totSiz
        this.pagSiz = pagSiz
        curPos = 0
        calcThumbMetrix()
    }

    fun setCurPos(nVal: Int): Boolean {
        @Suppress("NAME_SHADOWING") val nVal = iCLAMP(0, maxOf(0, totalSiz - pagSiz), nVal)
        if (totalSiz == 0 || nVal == curPos) {
            return false
        }
        curPos = nVal
        calcThumbMetrix()
        return true
    }

    fun pageUp(): Boolean {
        if (setCurPos(curPos - pagSiz)) {
            cmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBPAGEUP, curPos)
            invalidate()
            return true
        }
        return false
    }

    fun pageDown(): Boolean {
        if (setCurPos(curPos + pagSiz)) {
            cmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBPAGEDOWN, curPos)
            invalidate()
            return true
        }
        return false
    }

    val maxPos: Int get() = totalSiz - pagSiz

    val isHorizontal: Boolean get() = (flags and Flags.Horizontal.v) != 0

    val defBtnSiz: Int get() = 19

    abstract fun composeSBElement(el: Element, rc: Rect, flags: Set<iButton.State>)

    private fun onMouseDown(pos: Point) {
        val res = hitTest(pos - screenRect.Point())

        if (res == HitTestRes.Thumb) {
            thumbTrack = true
            trackAnchor = Point(pos)
            trackPos = curPos
            invalidate()
        } else if (res == HitTestRes.PgUp) {
            if (pageUp()) {
                invalidate()
            }
        } else if (res == HitTestRes.PgDown) {
            if (pageDown()) {
                invalidate()
            }
        }
    }

    private fun onMouseUp(pos: Point) {
        thumbTrack = false
        invalidate()
    }

    private fun onMouseTrack(pos: Point) {
        if (thumbTrack) {
            val n = if (isHorizontal) pos.x - trackAnchor.x else pos.y - trackAnchor.y
            if (setCurPos(trackPos + nItems(n))) {
                cmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBTRACKING, curPos)
                invalidate()
            }
        }
    }

    override fun onCompose() {
        fun flags(): Set<iButton.State> = (if (isEnabled) emptySet() else setOf(iButton.State.DISABLED)) intersect
                (if (thumbTrack) setOf(iButton.State.PRESSED) else emptySet())

        val rc = Rect(screenRect)
        if (isHorizontal) {
            composeSBElement(Element.Bkgnd,
                    Rect(rc.x + thumbMetrix.es, rc.y, thumbMetrix.h, rc.h), emptySet())
            composeSBElement(Element.Thumb,
                    Rect(rc.x + thumbMetrix.es + thumbMetrix.s1, rc.y, thumbMetrix.th, rc.h), flags())
        } else {
            composeSBElement(Element.Bkgnd, Rect(rc.x, rc.y + thumbMetrix.es, rc.w, thumbMetrix.h), emptySet())
            composeSBElement(Element.Thumb,
                    Rect(rc.x, rc.y + thumbMetrix.es + thumbMetrix.s1, rc.w, thumbMetrix.th), flags())
        }
    }

    protected fun hitTest(pos: Point): HitTestRes {
        var value = if (isHorizontal) {
            pos.x - thumbMetrix.es
        } else {
            pos.y - thumbMetrix.es
        }
        if (value < thumbMetrix.s1) {
            return HitTestRes.PgUp
        }
        value -= thumbMetrix.s1
        if (value < thumbMetrix.th.toInt()) {
            return HitTestRes.Thumb
        }
        return HitTestRes.PgDown
    }

    protected fun itemsHeight(itemCount: Int): Int = (thumbMetrix.h.toInt() * itemCount) / totalSiz
    protected fun nItems(n: Int): Int = (totalSiz * n) / thumbMetrix.h.toInt()

    protected fun calcThumbMetrix() {
        if (isHorizontal) {
            thumbMetrix.es = defBtnSiz
            thumbMetrix.h = relativeRect.w - thumbMetrix.es.toUInt() * 2u
        } else {
            thumbMetrix.es = defBtnSiz
            thumbMetrix.h = relativeRect.h - thumbMetrix.es.toUInt() * 2u
        }

        if (totalSiz == 0) {
            thumbMetrix.th = thumbMetrix.h
            thumbMetrix.s2 = 0
            thumbMetrix.s1 = 0
        } else {
            thumbMetrix.th = maxOf(itemsHeight(minOf(pagSiz, totalSiz)), thumbMetrix.es).toUInt()
            if (curPos == 0) {
                thumbMetrix.s1 = 0
            } else {
                val fpx = thumbMetrix.h - thumbMetrix.th
                thumbMetrix.s1 = (fpx.toInt() * curPos) / (totalSiz - pagSiz)
            }
            thumbMetrix.s2 = thumbMetrix.h.toInt() - thumbMetrix.th.toInt() - thumbMetrix.s1
        }
    }

    override fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.uid
        if (uid == this.pUp.uid) {
            // Scroll up
            if (setCurPos(curPos - 1)) {
                cmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBLINEUP, curPos)
            }
        } else if (uid == this.pDown.uid) {
            // Scroll down
            if (setCurPos(curPos + 1)) {
                cmdHandler!!.iCMDH_ControlCommand(this, CTRL_CMD_ID.SBLINEDOWN, curPos)
            }
        }
    }
}

/** List Box (virtual). */
@ExperimentalUnsignedTypes
abstract class iListBox(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler?, rect: Rect, uid: UInt)
    : iBaseCtrl(pViewMgr, pCmdHandler, rect, ViewClassId.LIST_BOX, uid,
        setOf(ViewState.Visible, ViewState.Enabled)),
        IViewCmdHandler {

    val pageSize: UInt get() = relativeRect.h / lbItemHeight()

    var scrollBar: iScrollBar? = null
        set(value) {
            field = value

            field?.apply {
                cmdHandler = this
                setMetrics(lbItemsCount().toInt(), pageSize.toInt())
                setCurPos(currentPosition)
            }
        }

    var currentPosition: Int = 0

    var selItem: Int = -1
        protected set

    protected var lcTime: Int = 0
    protected var lcIdx: Int = -1

    fun updateListBox() {
        currentPosition = 0
        scrollBar!!.setMetrics(lbItemsCount().toInt(), pageSize.toInt())
        scrollBar!!.setCurPos(currentPosition)
        selItem = if (lbItemsCount() != 0u) 0 else -1
        cmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.LBSELCHANGED, selItem)
        invalidate()
    }

    fun setCurSel(idx: Int, notify: Boolean) {
        require(idx == -1 || isValidIdx(idx)) { "Not valid idx: $idx" }
        if (idx != selItem) {
            selItem = idx
            if (notify) {
                cmdHandler?.iCMDH_ControlCommand(this, CTRL_CMD_ID.LBSELCHANGED, selItem)
            }
            if (idx != -1) {
                if (selItem < currentPosition) {
                    scrollBar?.setCurPos(selItem)
                    currentPosition = selItem
                } else if (selItem > currentPosition + pageSize.toInt() - 1) {
                    val npos = selItem - pageSize.toInt() + 1
                    scrollBar?.setCurPos(npos)
                    currentPosition = npos
                }
            }
            invalidate()
        }
    }

    fun selPrev() {
        if (selItem > 0) {
            setCurSel(selItem - 1, true)
        }
    }

    fun selNext() {
        if (selItem + 1 < lbItemsCount().toInt()) {
            setCurSel(selItem + 1, true)
        }
    }

    abstract fun composeLBBackground(rect: Rect)

    abstract fun composeLBItem(iIdx: Int, bSel: Boolean, irc: Rect)
    abstract fun lbItemHeight(): UInt
    abstract fun lbItemsCount(): UInt

    private fun onMouseDown(pos: Point) {
        var nIdx = idxByPos(pos)
        if (!isValidIdx(nIdx)) {
            nIdx = -1
        }
        setCurSel(nIdx, true)
    }

    private fun onMouseClick(pos: Point) {
        cmdHandler?.let {
            val nt = GetTickCount()
            if (lcTime != 0 && lcTime in (nt - 500) until nt && selItem != -1 && lcIdx == selItem) {
                lcTime = 0
                lcIdx = -1
                it.iCMDH_ControlCommand(this, CTRL_CMD_ID.LBSELDBLCLICK, selItem)
            } else {
                lcTime = nt
                lcIdx = selItem
            }
        }
    }

    override fun onCompose() {
        val rc = screenRect
        composeLBBackground(Rect(rc))
        val ih = lbItemHeight()
        val ic = lbItemsCount()
        val ps = pageSize
        val irc = Rect(rc.x, rc.y, rc.w, ih)
        for (xx in currentPosition until minOf(ic.toInt(), currentPosition + ps.toInt())) {
            composeLBItem(xx, xx == selItem, Rect(irc))
            irc.y += ih.toInt()
        }
    }

    override fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        require(scrollBar !== null && scrollBar === pView)
        currentPosition = param
    }

    protected fun isValidIdx(idx: Int) = idx.toUInt() < lbItemsCount()
    protected fun idxByPos(pos: Pointc): Int = currentPosition + (pos.y - screenRect.y) / lbItemHeight().toInt()
}
