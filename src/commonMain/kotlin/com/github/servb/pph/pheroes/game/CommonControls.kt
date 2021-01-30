package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.IViewCmdHandler
import com.github.servb.pph.gxlib.iButton
import com.github.servb.pph.gxlib.iViewMgr
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.util.helpertype.or
import com.soywiz.korma.geom.IRectangleInt

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