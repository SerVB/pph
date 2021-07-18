package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.util.asRectangle
import com.github.servb.pph.util.helpertype.and
import com.github.servb.pph.util.helpertype.or
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt

private val menuBtnText = ushortArrayOf(
    RGB16(210, 190, 115), RGB16(214, 192, 110), RGB16(216, 192, 102), RGB16(219, 193, 96), RGB16(221, 193, 85),
    RGB16(224, 194, 76), RGB16(228, 196, 67), RGB16(231, 195, 59), RGB16(233, 196, 49), RGB16(236, 196, 40),
    RGB16(239, 198, 31), RGB16(242, 198, 23), RGB16(224, 198, 16), RGB16(247, 199, 0), RGB16(248, 200, 0)
)  // todo: check size == 15

/** Main dialog. */
private class iMainMenuDlg : iDialog, IViewCmdHandler {

    class iMainMenuBtn : iButton {

        private val m_TextKey: TextResId

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
            //gSfxMgr.PlaySound(CSND_BUTTON)  // commented in sources
        }

        override fun OnCompose() {
            gApp.Surface().Darken50Rect(GetScrRect())
            // Compose outer frame
            val rect = GetScrRect()
            rect.rect.inflate(1)
            val cColor_Grey = RGB16(32, 32, 32)
            gApp.Surface().HLine(IPointInt(rect.x + 2, rect.y), rect.x + rect.width - 3, cColor_Grey)
            gApp.Surface().HLine(IPointInt(rect.x + 2, rect.y + rect.height - 1), rect.x + rect.width - 3, cColor_Grey)
            gApp.Surface().VLine(IPointInt(rect.x, rect.y + 2), rect.y + rect.height - 2, cColor_Grey)
            gApp.Surface().VLine(IPointInt(rect.x + rect.width - 1, rect.y + 2), rect.y + rect.height - 2, cColor_Grey)

            var props = IiDibFont.ComposeProps(
                iGradient(IDibPixelPointer(menuBtnText, 0), 15),
                cColor.Black.pixel,
                IiDibFont.Decor.Border
            )
            val state = GetButtonState()
            if ((state and State.Disabled) != 0) {
                props = IiDibFont.ComposeProps(RGB16(128, 100, 80), cColor.Black.pixel, IiDibFont.Decor.Border)
            } else if ((state and State.Pressed) != 0) {
                props = IiDibFont.ComposeProps(RGB16(255, 255, 255), cColor.Black.pixel, IiDibFont.Decor.Border)
                gApp.Surface().Darken50Rect(GetScrRect())
            }

            val fc = iTextComposer.FontConfig(iTextComposer.FontSize.LARGE, props)
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                IPointInt(0, 0),
                gTextMgr[m_TextKey],
                GetScrRect(),
                Alignment.AlignCenter,
                if ((state and State.Pressed) != 0) {
                    IPointInt(1, 1)
                } else {
                    IPointInt(0, 0)
                }
            )
        }
    }

    constructor(pViewMgr: iViewMgr) : super(pViewMgr)

    override suspend fun OnCreateDlg() {
        val rc = RectangleInt(GetDialogMetrics().asRectangle())
        rc.height = DEF_BTN_HEIGHT + 2
        AddChild(
            iMainMenuBtn(
                m_pMgr,
                this,
                rc,
                TextResId.TRID_MENU_NEWGAME,
                100u,
                ViewState.Visible or ViewState.Enabled
            )
        )
        rc.y += DEF_BTN_HEIGHT + BTN_DIST
        AddChild(
            iMainMenuBtn(
                m_pMgr,
                this,
                rc,
                TextResId.TRID_MENU_LOADGAME,
                101u,
                ViewState.Visible or ViewState.Enabled
            )
        )
        rc.y += DEF_BTN_HEIGHT + BTN_DIST
        AddChild(
            iMainMenuBtn(
                m_pMgr,
                this,
                rc,
                TextResId.TRID_MENU_HIGHSCORE,
                102u,
                ViewState.Visible or ViewState.Enabled
            )
        )
        rc.y += DEF_BTN_HEIGHT + BTN_DIST
        AddChild(
            iMainMenuBtn(
                m_pMgr,
                this,
                rc,
                TextResId.TRID_MENU_CREDITS,
                103u,
                ViewState.Visible or ViewState.Enabled
            )
        )
        rc.y += DEF_BTN_HEIGHT + BTN_DIST
        AddChild(
            iMainMenuBtn(
                m_pMgr,
                this,
                rc,
                TextResId.TRID_MENU_EXITGAME,
                104u,
                ViewState.Visible or ViewState.Enabled
            )
        )
    }

    override fun OnPlace(rect: RectangleInt) {
        rect.y += 40
    }

    override fun OnCompose() {
        // empty in sources
    }

    override fun GetDialogMetrics(): SizeInt {
        return SizeInt(150, 5 * (DEF_BTN_HEIGHT + 2) + 12)
    }

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        EndDialog(pView.GetUID().toInt())
    }

    companion object {

        const val BTN_DIST = 5
    }
}


class iMenuView private constructor() : iChildGameView(false, CHILD_VIEW.UNDEFINED) {

    private val m_crComposer: iCreditsComposer = iCreditsComposer()

    companion object {

        suspend fun construct(): iMenuView = iMenuView().apply { m_crComposer.Init() }
    }

    suspend fun Start() {
        while (true) {
            val mdlg = iMainMenuDlg(gApp.ViewMgr())
            val res = mdlg.DoModal()

            when (res) {
                100 -> {
                    // Start new game
                    val sldlg = iScenListDlg(gApp.ViewMgr())
                    val res = sldlg.DoModal()
                    if (res == DLG_RETCODE.OK.v) {
                        val scenProps = sldlg.SelScen()
                        val spdlg = iScenPropsDlg(gApp.ViewMgr(), scenProps, false)
                        if (spdlg.DoModal() == DLG_RETCODE.OK.v) {
                            scenProps.ReorderPlayers()
                            val a = iHero()
                            val d = iHero()
                            val bi = iBattleInfo(a, d)
                            gGame.BeginBattle(bi)
//                            gGame.StartNewGame(scenProps, true)  // todo
                            break
                        }
                    } else {
                        continue
                    }
                }
                101 -> {
                    // Load saved game
                    val saveDlg = iSaveDlg(gApp.ViewMgr(), false)
                    val res = saveDlg.DoModal()
                    if (res == DLG_RETCODE.OK.v) {
                        // todo
                    } else {
                        continue
                    }
                }
                102 -> {
                    val dlg = iDlg_HallOfFame.construct(gApp.ViewMgr(), "PalmHeroes.hsc")
                    dlg.DoModal()
                }
                103 -> {
                    StartCredits()
                    break
                }
                104 -> {
                    gGame.Quit()
                    break
                }
            }
        }
    }

    override fun OnCompose() {
        m_crComposer.Compose(gApp.Surface())

        // commented in sources:
//	gGfxMgr.Blit(PDGG_LOGO, gApp.Surface(), iPoint(44,2));
//	gGfxMgr.Blit(PDGG_LOGO2, gApp.Surface(), iPoint(174,3));
//        gTextComposer.TextOut(
//            iTextComposer::FontConfig(iTextComposer::FS_MEDIUM, iDibFont::ComposeProps(cColor_White, cColor_Black, iDibFont::DecBorder ) ),
//            gApp.Surface(), iPoint(), _T("Эксклюзивная версия для читателей журнала Mobi (www.mobi.ru)"),
//            iRect(0,m_Rect.y2()-15,m_Rect.w, 15), AlignCenter);
    }

    override suspend fun Process(t: Double): Boolean {
        if (m_crComposer.IsCreaditsStarted() && m_crComposer.IsCreaditsEnd()) {
            StopCredits()
        }
        Invalidate()
        return true
    }

    override suspend fun OnMouseClick(pos: IPointInt) {
        StopCredits()
    }

    fun StartCredits() {
        m_crComposer.StartCredits()
    }

    suspend fun StopCredits() {
        m_crComposer.StopCredits()
        Start()
    }

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        pView.GetUID()  // strange no behavior in sources
    }
}