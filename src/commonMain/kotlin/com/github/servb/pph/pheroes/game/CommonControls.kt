package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.asPoint
import com.github.servb.pph.util.helpertype.and
import com.github.servb.pph.util.helpertype.or
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt

abstract class iFramePopupView : iPopupView {

    private val m_pid: PlayerId

    constructor(pViewMgr: iViewMgr, pid: PlayerId) : super(pViewMgr) {
        m_pid = pid
    }

    abstract fun DoCompose(clRect: IRectangleInt)

    abstract fun ClientSize(): SizeInt

    override fun OnCompose() {
        ComposeDlgBkgnd(gApp.Surface(), m_Rect, m_pid, false)
        val rc = RectangleInt(m_Rect)
        rc.rect.inflate(-12)
        DoCompose(rc)
    }

    override fun PopupViewSize(): SizeInt {
        val result = ClientSize()
        result.width += 24
        result.height += 24
        return result
    }

    override fun PopupViewMinSize(): SizeInt = SizeInt(80, 40)
}

class iPHLabel : iBaseCtrl {

    var m_text: String
    val m_align: Alignment
    val m_fc: iTextComposer.IFontConfig

    constructor(
        pViewMgr: iViewMgr,
        rect: IRectangleInt,
        text: String,
        align: Alignment,
        fc: iTextComposer.IFontConfig
    ) : super(pViewMgr, null, rect, VIEWCLSID.GENERIC_VIEWPORT, 0u, ViewState.Visible.v) {
        m_text = text
        m_align = align
        m_fc = fc
    }

    fun SetText(text: String) {
        m_text = text
        Invalidate()
    }

    override fun OnCompose() {
        val rc = GetScrRect()
        gTextComposer.TextOut(m_fc, gApp.Surface(), rc.asPoint(), m_text, rc, m_align)
    }
}

class iTBSplitter : iBaseCtrl {

    constructor(pViewMgr: iViewMgr, rect: IRectangleInt) : super(
        pViewMgr,
        null,
        rect,
        VIEWCLSID.GENERIC_VIEWPORT,
        0u,
        ViewState.Visible.v
    )

    override fun OnCompose() {
        val rc = GetScrRect()
        gGfxMgr.BlitTile(GfxId.PDGG_BKTILE.v, gApp.Surface(), rc)
        ButtonFrame(gApp.Surface(), rc, 0)
        gApp.Surface().Darken25Rect(rc)
    }
}

class iTextButton : iButton {

    private var m_TextKey: TextResId

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        textKey: TextResId,
        uid: UInt,
        state: Int = ViewState.Visible or ViewState.Enabled
    ) : super(pViewMgr, pCmdHandler, rect, uid, state) {
        m_TextKey = textKey
    }

    override fun OnBtnDown() {
        //	gSfxMgr.PlaySound(CSND_BUTTON);  // commented in sources
    }

    override fun OnCompose() {
        ComposeDlgTextButton(gApp.Surface(), GetScrRect(), GetButtonState(), m_TextKey)
    }

    fun SetCaption(textKey: TextResId) {
        m_TextKey = textKey
        Invalidate()
    }
}

class iIconButton : iButton {

    private val m_spriteId: SpriteId

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        sid: SpriteId,
        uid: UInt,
        state: Int = iView.ViewState.Visible or ViewState.Enabled
    ) : super(pViewMgr, pCmdHandler, rect, uid, state) {
        m_spriteId = sid
    }

    override fun OnBtnDown() {
        // gSfxMgr.PlaySound(CSND_BUTTON);  // commented in sources
    }

    override fun OnCompose() {
        ComposeIconButton(gApp.Surface(), GetScrRect(), GetButtonState(), m_spriteId)
    }
}

class iDlgIconButton : iButton {

    private val m_spriteId: SpriteId

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        sid: SpriteId,
        uid: UInt,
        state: Int = iView.ViewState.Visible or ViewState.Enabled
    ) : super(pViewMgr, pCmdHandler, rect, uid, state) {
        m_spriteId = sid
    }

    override fun OnBtnDown() {
        // gSfxMgr.PlaySound(CSND_BUTTON)  // commented in sources
    }

    override fun OnCompose() {
        ComposeDlgIconButton(gApp.Surface(), GetScrRect(), GetButtonState(), m_spriteId)
    }
}

class iPHScrollBar : iScrollBar {

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        uid: UInt,
        flags: Int = 0
    ) : super(pViewMgr, pCmdHandler, rect, uid, flags)

    override fun ComposeSBElement(el: Element, rc: IRectangleInt, flags: Int) {
        when (el) {
            Element.Bkgnd -> {
                gGfxMgr.BlitTile(GfxId.PDGG_BKTILE.v, gApp.Surface(), rc)
                gApp.Surface().Darken50Rect(rc)
                gApp.Surface()
                    .FrameRect(IRectangleInt(rc.x - 1, rc.y - 1, rc.width + 2, rc.height + 2), cColor.Black.pixel)
            }
            Element.Thumb -> {
                val bksid = if (!IsEnabled()) GfxId.PDGG_BKTILE else GfxId.PDGG_CTILE
                gGfxMgr.BlitTile(bksid.v, gApp.Surface(), rc)
                ComposeDlgButton(
                    gApp.Surface(),
                    rc,
                    if ((flags and iButton.State.Disabled) != 0) iButton.State.Disabled.v else 0
                )
            }
            else -> {
                val orc = RectangleInt(rc)
                when (el) {
                    Element.BtnUp -> {
                        orc.height -= 1
                        ComposeDlgIconButton(gApp.Surface(), orc, flags, GfxId.PDGG_SCRBAR_BTNS(0))
                    }
                    Element.BtnDown -> {
                        orc.y += 1
                        orc.height -= 1
                        ComposeDlgIconButton(gApp.Surface(), orc, flags, GfxId.PDGG_SCRBAR_BTNS(1))
                    }
                    Element.BtnLeft -> {
                        orc.width -= 1
                        ComposeDlgIconButton(gApp.Surface(), orc, flags, GfxId.PDGG_SCRBAR_BTNS(2))
                    }
                    Element.BtnRight -> {
                        orc.x += 1
                        orc.width -= 1
                        ComposeDlgIconButton(gApp.Surface(), orc, flags, GfxId.PDGG_SCRBAR_BTNS(3))
                    }
                }
            }
        }
    }
}

// Tool bar tabbed switch
class iBarTabSwitch : iTabbedSwitch {

    private val m_Tabs: MutableList<SpriteId> = mutableListOf()

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        tabcnt: SizeT,
        uid: UInt
    ) : super(pViewMgr, pCmdHandler, rect, tabcnt, uid)

    fun SetTabIcon(sid: SpriteId) {
        m_Tabs.add(sid)
    }

    override fun ComposeTabItem(idx: Int, itemState: Int, rect: IRectangleInt) {
        ComposeIconButton(gApp.Surface(), rect, itemState, m_Tabs[idx])
        if (!IsEnabled()) gApp.Surface().Darken25Rect(rect)
    }
}
