package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.castle.CastleType
import com.github.servb.pph.pheroes.common.common.DifficultyLevel
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.common.PlayerType
import com.github.servb.pph.pheroes.common.common.PlayerTypeMask
import com.github.servb.pph.util.helpertype.and
import com.github.servb.pph.util.helpertype.getByValue
import com.github.servb.pph.util.helpertype.or
import com.soywiz.korma.geom.*

private class iPlayerBtn : iButton {

    private val m_pid: PlayerId
    private var m_pt: PlayerType

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        pid: PlayerId,
        pt: PlayerType,
        uid: UInt,
        state: Int = ViewState.Visible or ViewState.Enabled
    ) : super(pViewMgr, pCmdHandler, rect, uid, state) {
        m_pid = pid
        m_pt = pt
    }

    override fun OnCompose() {
        val rc = GetScrRect()
        gApp.Surface().FrameRect(rc, cColor.Black.pixel)
        rc.rect.inflate(-1)
        gApp.Surface().FillRect(rc, m_pid.color, 96u)
        ButtonFrame(gApp.Surface(), rc, m_state)

        val sid = if (m_pt == PlayerType.HUMAN) {
            GfxId.PDGG_ICN_PLT_HUMAN
        } else {
            GfxId.PDGG_ICN_PLT_AI
        }.v
        val op = IPointInt(
            rc.x + (rc.width / 2) - (gGfxMgr.Dimension(sid).width / 2),
            rc.y + (rc.height / 2) - (gGfxMgr.Dimension(sid).height / 2)
        )
        gGfxMgr.Blit(sid, gApp.Surface(), op)
        /*  // commented in sources
		if (m_pt == PT_HUMAN) gTextComposer.TextOut(dlgfc_stopic, gApp.Surface(), iPoint(), _T("Human"), rc, AlignCenter);
		else if (m_pt == PT_COMPUTER) gTextComposer.TextOut(dlgfc_splain, gApp.Surface(), iPoint(), _T("CPU"), rc, AlignCenter);
		*/

        if (!IsEnabled()) {
            gApp.Surface().FillRect(rc, cColor.Gray64.pixel, 128u)
        }
    }

    fun PlayerType(): PlayerType = m_pt

    fun TogglePlayerType(): PlayerType {
        if (m_pt == PlayerType.HUMAN) {
            m_pt = PlayerType.COMPUTER
        } else {
            m_pt = PlayerType.HUMAN
        }
        return m_pt
    }
}

private class iNationBtn : iButton, IViewCmdHandler {

    private val m_bFixed: Boolean
    private val m_pid: PlayerId
    private var m_nt: CastleType

    constructor(
        pViewMgr: iViewMgr,
        rect: IRectangleInt,
        pid: PlayerId,
        nt: CastleType,
        uid: UInt,
        bFixed: Boolean
    ) : super(
        pViewMgr,
        null,
        rect,
        uid,
        if (bFixed) ViewState.Visible.v else (ViewState.Visible or ViewState.Enabled)
    ) {
        m_pCmdHandler = this
        m_pid = pid
        m_nt = nt
        m_bFixed = bFixed
    }

    fun PlayerNation(): CastleType = m_nt

    override fun OnCompose() {
        val rc = GetScrRect()
        gApp.Surface().FrameRect(rc, cColor.Black.pixel)
        rc.rect.inflate(-1)

        val icn = GfxId.PDGG_CTL_SICONS(CastleType.COUNT.v * 2 + m_nt.v)
        //PDGG_CTL_SICONS + (NATION_COUNT-NATION_HIGHMEN)*2 + (m_nt-NATION_HIGHMEN)  // commented in sources
        BlitIcon(gApp.Surface(), icn, rc)
        ButtonFrame(gApp.Surface(), rc, m_state)

        if (!IsEnabled()) {
            gApp.Surface().FillRect(rc, cColor.Gray64.pixel, 128u)
        }
    }

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        check(!m_bFixed)
        if (cmd in setOf(CTRL_CMD_ID.BUTTON_CLICK, CTRL_CMD_ID.BUTTON_DOUBLE_CLICK)) {
            m_nt = when (m_nt) {
                CastleType.RANDOM -> CastleType.CITADEL
                else -> getByValue(m_nt.v + 1)
            }
        }
    }
}

private class iDifLvlTab : iTabbedSwitch {

    constructor(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler, rect: IRectangleInt, uid: UInt, state: Int) : super(
        pViewMgr,
        pCmdHandler,
        rect,
        DifficultyLevel.COUNT.v,
        uid,
        state
    )

    override fun ComposeTabItem(idx: Int, itemState: Int, rect: IRectangleInt) {
        val orc = RectangleInt(rect)
        orc.rect.inflate(-1)
        val icn = GfxId.PDGG_ICN_DIF_LEVEL(idx)
        BlitIcon(gApp.Surface(), icn, orc)
        if ((itemState and iButton.State.Selected) != 0) {
            gApp.Surface().FrameRect(orc, cColor.Red.pixel)
        }
        if (!IsEnabled()) {
            gApp.Surface().FillRect(orc, cColor.Gray64.pixel, 128u)
        }
    }
}

class iScenPropsDlg : iBaseGameDlg {

    private lateinit var m_pDfcLabel: iPHLabel
    private lateinit var m_pOkBtn: iTextButton
    private val m_btnPlayers: MutableList<iPlayerBtn> = mutableListOf()
    private val m_btnNations: MutableList<iNationBtn> = mutableListOf()
    private lateinit var m_difLevel: iDifLvlTab
    private val m_scProps: iMapInfo
    private val m_bReadOnly: Boolean

    constructor(pViewMgr: iViewMgr, scProps: iMapInfo, bReadOnly: Boolean) : super(pViewMgr, PlayerId.NEUTRAL) {
        m_scProps = scProps
        m_bReadOnly = bReadOnly
    }

    fun ScenProps(): iMapInfo = m_scProps

    override suspend fun OnCreateDlg() {
        val clRect = ClientRect()

        var yp = clRect.y
        AddChild(
            iPHLabel(
                m_pMgr,
                IRectangleInt(clRect.x, yp, clRect.width, 15),
                m_scProps.m_Name,
                Alignment.AlignTop,
                dlgfc_hdr
            )
        )
        yp += 20

        m_pDfcLabel = iPHLabel(
            m_pMgr,
            IRectangleInt(clRect.x, yp, clRect.width, 15),
            GetDfcString(m_scProps.m_Difficulty),
            Alignment.AlignTop,
            dlgfc_topic
        )
        AddChild(m_pDfcLabel)
        yp += 15

        m_difLevel = iDifLvlTab(
            m_pMgr,
            this,
            IRectangleInt(clRect.x + (clRect.width / 2 - 95), yp, 190, 38),
            301u,
            if (m_bReadOnly) ViewState.Visible.v else (ViewState.Visible or ViewState.Enabled)
        )
        AddChild(m_difLevel)
        if (m_scProps.m_Difficulty == DifficultyLevel.UNDEFINED) {
            m_scProps.m_Difficulty = DifficultyLevel.NORMAL
        }
        m_difLevel.SetCurrentTab(m_scProps.m_Difficulty.v)
        m_pDfcLabel.SetText(GetDfcString(m_scProps.m_Difficulty))
        yp += 45

        val cnt = m_scProps.TotalPlayers()
        val btnsw = cnt * 34 + (cnt - 1) * 3
        val sy = yp
        var sx = clRect.x + (clRect.width / 2 - btnsw / 2)
        repeat(cnt) { xx ->
            val pNatBtn = iNationBtn(
                m_pMgr,
                IRectangleInt(sx, sy, 34, 22),
                m_scProps.m_Players[xx].m_Id,
                m_scProps.m_Players[xx].m_Nation,
                (150 + xx).toUInt(),
                m_scProps.m_Players[xx].m_Nation != CastleType.RANDOM
            )
            AddChild(pNatBtn)
            m_btnNations.add(pNatBtn)
            val tp = when (m_scProps.m_Players[xx].m_TypeMask) {
                PlayerTypeMask.HUMAN_ONLY -> PlayerType.HUMAN
                PlayerTypeMask.COMPUTER_ONLY -> PlayerType.COMPUTER
                else -> when (xx) {
                    0 -> PlayerType.HUMAN
                    else -> PlayerType.COMPUTER
                }
            }
            val pPlBtn = iPlayerBtn(
                m_pMgr,
                this,
                IRectangleInt(sx, sy + 25, 34, 34),
                m_scProps.m_Players[xx].m_Id,
                if (m_bReadOnly) m_scProps.m_Players[xx].m_Type else tp,
                (200 + xx).toUInt(),
                if (!m_bReadOnly && (m_scProps.m_Players[xx].m_TypeMask == PlayerTypeMask.HUMAN_OR_COMPUTER)) (ViewState.Visible or ViewState.Enabled) else ViewState.Visible.v
            )
            AddChild(pPlBtn)
            m_btnPlayers.add(pPlBtn)
            sx += 37
        }

        // Buttons
        val npos = clRect.x + (clRect.width / 2 - 80)
        m_pOkBtn = iTextButton(
            m_pMgr,
            this,
            IRectangleInt(npos, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
            TextResId.TRID_OK,
            DLG_RETCODE.OK.v.toUInt(),
            ViewState.Visible.v
        )
        AddChild(m_pOkBtn)
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos + 55, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_INFO,
                401u
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos + 110, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_CANCEL,
                DLG_RETCODE.CANCEL.v.toUInt()
            )
        )

        UpdateControls()
    }

    override fun DoCompose(clRect: IRectangleInt) {
        // empty in sources
    }

    override fun ClientSize(): SizeInt = SizeInt(270, 150 + DEF_BTN_HEIGHT)

    private fun UpdateControls() {
        val bHasHuman = m_scProps.m_Players.indices.any { m_btnPlayers[it].PlayerType() == PlayerType.HUMAN }
//        m_pOkBtn.SetEnabled(bHasHuman)  // todo: uncomment when new game start is supported
    }

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID().toInt()
        when {
            (uid == DLG_RETCODE.OK.v || uid == DLG_RETCODE.CANCEL.v) && cmd == CTRL_CMD_ID.BUTTON_CLICK -> {
                // Setup difficulty
                m_scProps.m_Difficulty = getByValue(m_difLevel.GetCurrentTab())
                // Setup players
                m_scProps.m_Players.indices.forEach { xx ->
                    m_scProps.m_Players[xx].m_Type = m_btnPlayers[xx].PlayerType()
                    m_scProps.m_Players[xx].m_Nation = m_btnNations[xx].PlayerNation()
                }
                EndDialog(uid)
            }
            uid == 301 -> {
                m_pDfcLabel.SetText(GetDfcString(getByValue(m_difLevel.GetCurrentTab())))
            }
            uid == 401 -> {
                var title = m_scProps.m_Name
                if (m_scProps.m_Version.isNotBlank()) {
                    title += " v.${m_scProps.m_Version}"
                }
                var desc = m_scProps.m_Description
                if (m_scProps.m_Author.isNotBlank()) {
                    desc += "\n\n${gTextMgr[TextResId.TRID_MAP_AUTHOR]}: ${m_scProps.m_Author}"
                }
                val tdlg = iTextDlg(m_pMgr, title, desc, PlayerId.NEUTRAL, dlgfc_topic, dlgfc_splain)
                tdlg.DoModal()
            }
            (uid in 200 until (200 + m_scProps.m_Players.size)) && cmd == CTRL_CMD_ID.BUTTON_CLICK -> {
                val value = uid - 200
                m_btnPlayers[value].TogglePlayerType()
                UpdateControls()
            }
        }
    }

    companion object {

        fun GetDfcString(dl: DifficultyLevel): String =
            "#FCCC${gTextMgr[TextResId.TRID_DIFFICULTY_LEVEL]}: #FFF0${gTextMgr[TextResId.TRID_DIFF_EASY.v + dl.v]}"
    }
}