package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.*
import com.soywiz.korio.lang.format
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.y2

const val SAVE_GAME_SLOTS: SizeT = 8

typealias iSaveSlots = MutableList<iMapInfo?>

private fun EnumSaveGames(saveSlots: iSaveSlots) {
    // todo
    repeat(SAVE_GAME_SLOTS) {
        saveSlots.add(null)
    }
}

private class iSaveListBox : iListBox {

    private val m_saveSlots: List<iMapInfo?>

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        uid: UInt,
        saveSlots: iSaveSlots
    ) : super(pViewMgr, pCmdHandler, rect, uid) {
        m_saveSlots = saveSlots
    }

    override fun LBItemHeight(): SizeT = 15
    override fun LBItemsCount(): SizeT = m_saveSlots.size

    override fun ComposeLBBackground(rect: IRectangleInt) {
        gApp.Surface().Darken25Rect(rect)
    }

    override fun ComposeLBItem(iIdx: Int, bSel: Boolean, irc: IRectangleInt) {
        val rc = RectangleInt(irc)

        ButtonFrame(gApp.Surface(), rc, iButton.State.Pressed.v)
        rc.rect.inflate(-1)
        if (bSel) {
            gGfxMgr.BlitTile(GfxId.PDGG_CTILE.v, gApp.Surface(), rc)
            ButtonFrame(gApp.Surface(), rc, 0)
        }

        rc.setTo(irc)
        val currentSlot = m_saveSlots[iIdx]
        if (currentSlot != null) {
            // Map Size
            gTextComposer.TextOut(
                dlgfc_splain,
                gApp.Surface(),
                rc.asPoint(),
                gTextMgr[TextResId.TRID_SHORT_MAPSIZ_SMALL.v + m_saveSlots[iIdx]!!.m_Size.v],
                IRectangleInt(rc.x, rc.y, 20, rc.height),
                Alignment.AlignCenter
            )
            rc.rect.deflate(20, 0, 0, 0)

            // Players count
            gTextComposer.TextOut(
                dlgfc_splain,
                gApp.Surface(),
                rc.asPoint(),
                "${m_saveSlots[iIdx]!!.HumanPlayers()}/${m_saveSlots[iIdx]!!.TotalPlayers()}",
                IRectangleInt(rc.x + rc.width - 25, rc.y, 25, rc.height),
                Alignment.AlignCenter
            )
            rc.rect.deflate(0, 0, 25, 0)

            // Map name
            gTextComposer.TextOut(
                dlgfc_stopic,
                gApp.Surface(),
                rc.asPoint(),
                m_saveSlots[iIdx]!!.m_Name,
                rc,
                Alignment.AlignLeft
            )
        } else {
            gTextComposer.TextOut(
                dlgfc_splain,
                gApp.Surface(),
                rc.asPoint(),
                gTextMgr[TextResId.TRID_EMPTY],
                rc,
                Alignment.AlignCenter
            )
        }
    }
}

class iSaveDlg : iBaseGameDlg {

    private val m_bSave: Boolean
    private var m_selSlot: Int
    private val m_saveSlots: iSaveSlots = mutableListOf()

    constructor(pViewMgr: iViewMgr, bSave: Boolean) : super(pViewMgr, PlayerId.NEUTRAL) {
        m_bSave = bSave
        m_selSlot = -1
    }

    fun SelFile(): String {
        check(m_selSlot in 0 until SAVE_GAME_SLOTS)
        return GetSaveFileName(m_selSlot)
    }

    fun SelScenario(): iMapInfo {
        check(m_selSlot in 0 until SAVE_GAME_SLOTS && m_saveSlots[m_selSlot] != null)
        return m_saveSlots[m_selSlot]!!
    }

    override suspend fun OnCreateDlg() {
        val clRect = ClientRect()

        EnumSaveGames(m_saveSlots)
        AddChild(iSaveListBox(m_pMgr, this, IRectangleInt(clRect.x, clRect.y + 22, 150, 120), 100u, m_saveSlots))

        val npos = clRect.x + (clRect.width / 2 - 65)
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos, clRect.y2 - DEF_BTN_HEIGHT, 60, DEF_BTN_HEIGHT),
                if (m_bSave) TextResId.TRID_SAVE else TextResId.TRID_LOAD,
                DLG_RETCODE.OK.v.toUInt(),
                ViewState.Visible.v
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos + 70, clRect.y2 - DEF_BTN_HEIGHT, 60, DEF_BTN_HEIGHT),
                TextResId.TRID_CANCEL,
                DLG_RETCODE.CANCEL.v.toUInt()
            )
        )
    }

    override fun DoCompose(clRect: IRectangleInt) {
        gTextComposer.TextOut(
            dlgfc_hdr,
            gApp.Surface(),
            clRect.asPoint(),
            gTextMgr[if (m_bSave) TextResId.TRID_SAVE_DLG_HDR else TextResId.TRID_LOAD_DLG_HDR],
            clRect,
            Alignment.AlignTop
        )
        gApp.Surface().FrameRect(IRectangleInt(clRect.x - 1, clRect.y + 22 - 1, 152, 122), cColor.Black.pixel)

        // Compose selected map information
        val orc = RectangleInt(clRect.x + 150, clRect.y + 21, 101, 122)
        gApp.Surface().FrameRect(orc, cColor.Black.pixel)
        orc.rect.inflate(-1)
        gApp.Surface().Darken25Rect(orc)
        if (m_selSlot != -1 && m_saveSlots[m_selSlot] != null) {
            TODO()
        } else {
            gTextComposer.TextOut(dlgfc_plain, gApp.Surface(), clRect.asPoint(), "-", orc, Alignment.AlignCenter)
        }
    }

    override fun ClientSize(): SizeInt = SizeInt(250, 155 + DEF_BTN_HEIGHT)

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID()
        when (uid.toInt()) {
            DLG_RETCODE.OK.v -> {
                if (CanSelect() && Confirmed()) {
                    EndDialog(DLG_RETCODE.OK.v)
                }
            }
            DLG_RETCODE.CANCEL.v -> {
                EndDialog(DLG_RETCODE.CANCEL.v)
            }
            100 -> {
                if (cmd == CTRL_CMD_ID.LBSELCHANGED) {
                    m_selSlot = param
                    GetChildById(DLG_RETCODE.OK.v.toUInt())!!.SetEnabled(CanSelect())
                } else if (cmd == CTRL_CMD_ID.LBSELDBLCLICK) {
                    if (CanSelect() && Confirmed()) {
                        EndDialog(DLG_RETCODE.OK.v)
                    }
                }
            }
        }
    }

    private fun CanSelect(): Boolean = m_selSlot != -1 && (m_bSave || m_saveSlots[m_selSlot] != null)

    private fun Confirmed(): Boolean {
        check(m_selSlot != -1)
        if (m_bSave && m_saveSlots[m_selSlot] != null) {
            TODO()
        }
        return true
    }

    companion object {

        fun GetSaveFileName(slot: SizeT): String = gSavePath + "save%02d.phs".format(slot + 1)
    }
}
