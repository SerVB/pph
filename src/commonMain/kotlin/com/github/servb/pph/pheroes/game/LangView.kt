package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.util.asRectangle
import com.github.servb.pph.util.helpertype.and
import com.github.servb.pph.util.helpertype.or
import com.github.servb.pph.util.inflate
import com.github.servb.pph.util.invoke
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt

// todo: this is a PoC for language selector, copied from iMainMenu

private val menuBtnText = ushortArrayOf(
    RGB16(210, 190, 115), RGB16(214, 192, 110), RGB16(216, 192, 102), RGB16(219, 193, 96), RGB16(221, 193, 85),
    RGB16(224, 194, 76), RGB16(228, 196, 67), RGB16(231, 195, 59), RGB16(233, 196, 49), RGB16(236, 196, 40),
    RGB16(239, 198, 31), RGB16(242, 198, 23), RGB16(224, 198, 16), RGB16(247, 199, 0), RGB16(248, 200, 0)
)  // todo: check size == 15

class iLangMenuDlg : iDialog, IViewCmdHandler {

    class iLangMenuBtn : iButton {

        private val text: String

        constructor(
            pViewMgr: iViewMgr,
            pCmdHandler: IViewCmdHandler,
            rect: IRectangleInt,
            rawText: String,
            uid: UInt,
            state: Int = ViewState.Visible or ViewState.Enabled
        ) : super(pViewMgr, pCmdHandler, rect, uid, state) {
            text = rawText
        }

        override fun OnBtnDown() {
            //gSfxMgr.PlaySound(CSND_BUTTON)  // commented in sources
        }

        override fun OnCompose() {
            //			gApp.Surface().Darken50Rect(GetScrRect())  // commented in sources
            // Compose outer frame
            val rect = GetScrRect()
            rect.rect.inflate(1)

            var props = IiDibFont.ComposeProps(
                iGradient(IDibPixelPointer(menuBtnText, 0), 15),
                cColor.Black.pixel,
                IiDibFont.Decor.Border
            )
            val state = GetButtonState()

            if (state and State.Disabled != 0) {
                props = IiDibFont.ComposeProps(RGB16(255, 160, 80), cColor.Black.pixel, IiDibFont.Decor.Border)
            } else if (state and State.Pressed != 0) {
                props = IiDibFont.ComposeProps(RGB16(255, 255, 255), cColor.Black.pixel, IiDibFont.Decor.Border)
                val cColor_Grey = RGB16(32, 32, 32)
                gApp.Surface().HLine(IPointInt(rect.x + 2, rect.y), rect.x + rect.width - 3, cColor_Grey)
                gApp.Surface()
                    .HLine(IPointInt(rect.x + 2, rect.y + rect.height - 1), rect.x + rect.width - 3, cColor_Grey)
                gApp.Surface().VLine(IPointInt(rect.x, rect.y + 2), rect.y + rect.height - 2, cColor_Grey)
                gApp.Surface()
                    .VLine(IPointInt(rect.x + rect.width - 1, rect.y + 2), rect.y + rect.height - 2, cColor_Grey)
                gApp.Surface().Darken50Rect(GetScrRect())
            }

            val fc = iTextComposer.FontConfig(iTextComposer.FontSize.LARGE, props)
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                IPointInt(0, 0),
                text,
                GetScrRect(),
                Alignment.AlignCenter
            )
        }
    }

    constructor(pViewMgr: iViewMgr) : super(pViewMgr)

    override fun OnCreateDlg() {
        val rc = RectangleInt(GetDialogMetrics().asRectangle())
        rc.height = DEF_BTN_HEIGHT + 2

        Language.values().forEach {
            AddChild(
                iLangMenuBtn(
                    m_pMgr,
                    this,
                    rc,
                    it.displayName,
                    (it.ordinal + 100).toUInt(),
                    ViewState.Visible or ViewState.Enabled
                )
            )
            rc.y += DEF_BTN_HEIGHT + BTN_DIST
        }
    }

    override fun GetDialogMetrics(): SizeInt {
        return SizeInt(150, Language.values().size * (DEF_BTN_HEIGHT + 2) + 12)
    }

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        EndDialog(pView.GetUID().toInt())
    }

    companion object {

        const val BTN_DIST = 5
    }
}
