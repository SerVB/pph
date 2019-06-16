package com.github.servb.pph.gxlib

import com.github.servb.pph.gxlib.gxlmetrics.*
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr

enum class VIEWCLSID {
    GENERIC_VIEWPORT,
    MODAL_DIALOG,
    PUSH_BUTTON,
    TABBED_SWITCH,
    SCROLL_BAR,
    LIST_BOX,
}

enum class CTRL_EVT_ID {
    BUTTON_DOWN,
    BUTTON_UP,
    TRACKING,
    CLICK,
}

@ExperimentalUnsignedTypes
abstract class iView(viewMgr: iViewMgr, rect: Rect, val classId: VIEWCLSID, val uid: UInt, state: Set<ViewState>) {

    enum class ViewState(val v: Int) {
        Visible(0x1),
        Enabled(0x2)
    }

    var size: Sizec
        set(value) {
            relativeRect.w = value.w
            relativeRect.h = value.h
            onRectChanged(relativeRect)
        }
        get() = relativeRect.toSizec()

    var pos: Pointc
        set(value) {
            relativeRect.x = value.x
            relativeRect.y = value.y
            onRectChanged(relativeRect)
        }
        get() = relativeRect.toPointc()

    var rect: Rectc
        set(value) {
            relativeRect = Rect(value)
            onRectChanged(relativeRect)
        }
        get() = Rect(relativeRect)

    val screenPos: Point get() = screenRect.toPoint()

    val screenRect: Rect
        get() {
            val res = Rect(relativeRect)
            res += Rect(parent?.screenPos ?: Point(0, 0))
            return res
        }

    var relativeRect: Rect = Rect(rect)

    var isEnabled: Boolean = ViewState.Enabled in state
        get() = field && parent.let { it == null || it.isEnabled }
        set(value) {
            field = value
            invalidate()
        }

    var isVisible: Boolean = ViewState.Visible in state
        set(value) {
            field = value
            invalidate()
        }

    protected var tracking: Boolean = false

    var needRedraw: Boolean = true
        protected set

    protected var mgr: iViewMgr = viewMgr

    var parent: iView? = null
        protected set

    protected var children: MutableList<iView> = mutableListOf()

    fun addChild(child: iView): Boolean {
        child.parent = this
        return children.add(child)
    }

    fun removeChild(child: iView?): Boolean {
        if (child != null && children.remove(child)) {
            child.parent = null
            return true
        }
        return false
    }

    fun getChildByPos(pos: Pointc): iView? {
        // TODO: (from sources) reverse find (in future remake to z-order)
        if (children.isEmpty()) {
            return null
        }

        return children.lastOrNull { it.isVisible && it.isEnabled && it.relativeRect.PtInRect(pos) }
    }

    fun getChildById(uid: UInt): iView? = children.firstOrNull { it.uid == uid }

    // Message process
    fun compose(rect: Rect) {
        if (isVisible) {
            onCompose()

            children.forEach { it.compose(rect) }

            if (needRedraw) {
                rect += screenRect
                needRedraw = false
            }
        }
    }

    fun mouseDown(pos: Pointc): Boolean {
        if (!isEnabled) {
            check(parent == null) { "only topmost window can receive messages in disabled state" }
            mgr.SetViewCapture(this)
            tracking = true
            return true
        }

        val vp = getChildByPos(pos - screenRect.toPoint())
        if (vp == null || !vp.mouseDown(pos)) {
            onMouseDown(pos)
            mgr.SetViewCapture(this)
            tracking = true
        }
        return true
    }

    fun mouseUp(pos: Pointc): Boolean {
        if (tracking) {
            tracking = false
            mgr.ReleaseViewCapture()
            onMouseUp(pos)
            if (screenRect.PtInRect(pos)) {
                onMouseClick(pos)
            }
        } else {
            check(false)
        }

        return true
    }

    fun mouseTrack(pos: Pointc): Boolean {
        if (tracking && isEnabled) {
            onMouseTrack(pos)
        }
        return true
    }

    open fun invalidate() {
        needRedraw = true
        parent?.invalidate()
    }

    abstract fun onTimer(tid: Int)
    abstract fun onCompose()
    abstract fun onRectChanged(rc: Rectc)
    abstract fun onMouseDown(pos: Pointc)
    abstract fun onMouseUp(pos: Pointc)
    abstract fun onMouseClick(pos: Pointc)
    abstract fun onMouseTrack(pos: Pointc)
}
