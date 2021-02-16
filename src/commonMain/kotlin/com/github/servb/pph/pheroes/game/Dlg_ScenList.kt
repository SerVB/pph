package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.common.EMAP_FILE_HDR_KEY
import com.github.servb.pph.pheroes.common.common.GMAP_FILE_HDR_KEY
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.asPoint
import com.github.servb.pph.util.deflate
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.invoke
import com.soywiz.klogger.Logger
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.y2

typealias iScenList = MutableList<iMapInfo>

private val logger = Logger("Dlg_ScenListKt")

// todo: compress preinstalled maps to zip archive and mount it like described here:
//       https://discord.com/channels/728582275884908604/801658049244430377/807359417083232317
internal val preinstalledMaps = listOf(
    "Ancient_Lands.hmm",
    "Arena.hmm",
    "Armageddons_Blade.hmm",
    "Around_The_Bay.hmm",
    "Barbarians_Revenge.hmm",
    "Bloody_Sands.hmm",
    "Confrontation.hmm",
    "Consolidation.hmm",
    "Crossroads.hmm",
    "Delta.hmm",
    "Disagreements.hmm",
    "Fire-eater.hmm",
    "Gods_War.hmm",
    "H2O.hmm",
    "Harlem_War.hmm",
    "Henry.hmm",
    "heroes.hmm",
    "Hostile_Neighbors.hmm",
    "Ice_Age.hmm",
    "Island.hmm",
    "King_Of_The_Hill.hmm",
    "KneeDeepInTheDead.hmm",
    "Land_Bridge.hmm",
    "Loose_Borders.hmm",
    "Magic_Forests.hmm",
    "Might_And_Magic.hmm",
    "Quest_for_Glory.hmm",
    "Swampy.hmm",
    "TerritorialDivide.hmm",
    "The_Great_Nile.hmm",
    "The_Labyrynth.hmm",
    "The_Mystic_Valley.hmm",
    "ThePyramid.hmm",
    "Tutorial.hmm",
    "Two_by_the_river.hmm",
    "WarLords.hmm",
    "Winter_Assault.hmm",
    "Winter_Wars.hmm",
)

private suspend fun EnumScenarios(scList: iScenList) {
    preinstalledMaps.forEach { name ->
        val file = resourcesVfs["$gMapsPath/$name"]
        if (file.isFile()) {
            val mapInfo = iMapInfo()
            mapInfo.m_bNewGame = true
            mapInfo.m_FileName = file.path
            val pFile = file.openInputStream()
            val fourcc = pFile.ReadU32()
            if (fourcc == GMAP_FILE_HDR_KEY && mapInfo.ReadMapInfoPhm(pFile)) {
                scList.add(mapInfo)
            } else if (fourcc == EMAP_FILE_HDR_KEY && mapInfo.ReadMapInfoHmm(pFile)) {
                scList.add(mapInfo)
            } else {
                logger.warn { "Map '${mapInfo.m_FileName}' is not supported" }
            }
        }
    }
}

class iScenListBox : iListBox {

    private val m_scList: List<iMapInfo>

    constructor(
        pViewMgr: iViewMgr,
        pCmdHandler: IViewCmdHandler,
        rect: IRectangleInt,
        uid: UInt,
        scList: List<iMapInfo>
    ) : super(pViewMgr, pCmdHandler, rect, uid) {
        m_scList = scList
    }

    override fun LBItemHeight(): SizeT = 15
    override fun LBItemsCount(): SizeT = m_scList.size

    override fun ComposeLBBackground(rect: IRectangleInt) {
        gApp.Surface().Darken25Rect(rect)
    }

    override fun ComposeLBItem(iIdx: SizeT, bSel: Boolean, irc: IRectangleInt) {
        val fc = iTextComposer.FontConfig(dlgfc_plain)
        val rc = RectangleInt(irc)

        ButtonFrame(gApp.Surface(), rc, iButton.State.Pressed.v)
        rc.rect.inflate(-1)
        if (bSel) {
            gGfxMgr.BlitTile(GfxId.PDGG_CTILE.v, gApp.Surface(), rc)
            ButtonFrame(gApp.Surface(), rc, 0)
        }

        rc.rect.inflate(-1)

        if (!m_scList[iIdx].Supported()) {
            fc.cmpProps.faceColor = RGB16(192, 160, 160)
        }

        // Map Size
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_SHORT_MAPSIZ_SMALL.v + m_scList[iIdx].m_Size.v],
            IRectangleInt(rc.x, rc.y, 20, rc.height),
            Alignment.AlignCenter
        )
        rc.rect.deflate(23, 0, 0, 0)

        // Players count
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            rc.asPoint(),
            "${m_scList[iIdx].HumanPlayers()}/${m_scList[iIdx].TotalPlayers()}",
            IRectangleInt(rc.x, rc.y, 25, rc.height),
            Alignment.AlignCenter
        )
        rc.rect.deflate(25, 0, 0, 0)

        // Map name
        var title = m_scList[iIdx].m_Name
        if (m_scList[iIdx].m_Version.isNotBlank()) {
            title += " v.${m_scList[iIdx].m_Version}"
        }
        gTextComposer.TextOut(fc, gApp.Surface(), rc.asPoint(), title, rc, Alignment.AlignLeft)
    }
}

class iScenListDlg : iBaseGameDlg {

    private var m_selScen: Int
    private val m_scList: iScenList = mutableListOf()

    constructor(pViewMgr: iViewMgr) : super(pViewMgr, PlayerId.NEUTRAL) {
        m_selScen = -1
    }

    fun SelScen(): iMapInfo = m_scList[m_selScen]

    private enum class SortBy(override val v: Int, val comparator: (iMapInfo) -> Comparable<*>) : UniqueValueEnum {
        Size(0, { it.m_Size.v }),
        Players(1, { it.m_Players.size }),
        Name(2, { it.m_Name }),
    }

    private fun SortScenarios(sort_by: SortBy) {
        m_scList.sortWith(
            compareBy(
                iMapInfo::Supported,
                sort_by.comparator,
            )
        )
    }

    override suspend fun OnCreateDlg() {
        val clRect = ClientRect()

        EnumScenarios(m_scList)
        SortScenarios(SortBy.Name)

        // Listbox header
        AddChild(
            iDlgIconButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x, clRect.y + yoffs, 24, DEF_BTN_HEIGHT),
                GfxId.PDGG_BTN_MAPSIZE.v,
                501u
            )
        )
        AddChild(
            iDlgIconButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x + 25, clRect.y + yoffs, 24, DEF_BTN_HEIGHT),
                GfxId.PDGG_BTN_PLAYERS_COUNT.v,
                502u
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x + 50, clRect.y + yoffs, 280 - 16 - 50, DEF_BTN_HEIGHT),
                TextResId.TRID_MAP_NAME,
                503u
            )
        )

        // Listbox
        val pLB = iScenListBox(
            m_pMgr,
            this,
            IRectangleInt(clRect.x, clRect.y + yoffs + DEF_BTN_HEIGHT + 1, 280 - 16, 120),
            100u,
            m_scList
        )
        AddChild(pLB)
        // Scroll bar
        val pScrollBar = iPHScrollBar(
            m_pMgr,
            this,
            IRectangleInt(clRect.x + clRect.width - 15, clRect.y + yoffs, 15, 120 + DEF_BTN_HEIGHT + 1),
            300u
        )
        AddChild(pScrollBar)
        pLB.SetScrollBar(pScrollBar)

        // Buttons
        val npos = clRect.x + (clRect.width / 2 - 80)
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_OK,
                DLG_RETCODE.OK.v.toUInt(),
                iView.ViewState.Visible.v
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos + 55, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_INFO,
                301u,
                iView.ViewState.Visible.v
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

        // Init list
        SortScenarios(
            gSettings.GetEntryValue(ConfigEntryType.NGDSORT).let { id -> SortBy.values().single { it.v == id } })
        if (m_scList.isNotEmpty()) {
            val selScen = gSettings.GetEntryValue(ConfigEntryType.NGDPOS).coerceIn(m_scList.indices)
            pLB.SetCurSel(selScen, true)
        }
    }

    override fun DoCompose(clRect: IRectangleInt) {
        gTextComposer.TextBoxOut(
            dlgfc_hdr,
            gApp.Surface(),
            gTextMgr[TextResId.TRID_SELECT_SCENARIO_DLG_HDR],
            IRectangleInt(clRect.x, clRect.y, clRect.width, 24)
        )
        gApp.Surface().FrameRect(
            IRectangleInt(clRect.x - 1, clRect.y - 1 + yoffs + DEF_BTN_HEIGHT + 1, 282 - 16, 120 + 2),
            cColor.Black.pixel
        )
    }

    override fun ClientSize(): SizeInt = SizeInt(280, 150 + DEF_BTN_HEIGHT + DEF_BTN_HEIGHT)

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID().toInt()
        when (uid) {
            DLG_RETCODE.OK.v, DLG_RETCODE.CANCEL.v -> EndDialog(uid)
            301 -> {
                var title = m_scList[m_selScen].m_Name
                if (m_scList[m_selScen].m_Version.isNotBlank()) {
                    title += " v.${m_scList[m_selScen].m_Version}"
                }
                var desc = m_scList[m_selScen].m_Description
                if (m_scList[m_selScen].m_Author.isNotBlank()) {
                    desc += "\n\n${gTextMgr[TextResId.TRID_MAP_AUTHOR]}: ${m_scList[m_selScen].m_Author}"
                }
                val tdlg = iTextDlg(m_pMgr, title, desc, PlayerId.NEUTRAL, dlgfc_topic, dlgfc_splain)
                tdlg.DoModal()
            }
            in 501..503 -> {
                val nval = uid - 501
                gSettings.SetEntryValue(ConfigEntryType.NGDSORT, nval)
                SortScenarios(SortBy.values().single { it.v == nval })
                Invalidate()
            }
            100 -> {
                if (cmd == CTRL_CMD_ID.LBSELCHANGED) {
                    m_selScen = param
                    gSettings.SetEntryValue(ConfigEntryType.NGDPOS, param)
                    GetChildById(DLG_RETCODE.OK.v.toUInt())!!.SetEnabled(m_selScen != -1 && m_scList[m_selScen].Supported() && m_scList[m_selScen].HumanPlayers() > 0)
                    GetChildById(301u)!!.SetEnabled(m_selScen != -1 && m_scList[m_selScen].HumanPlayers() > 0)
                } else if (cmd == CTRL_CMD_ID.LBSELDBLCLICK) {
                    if (m_selScen != -1 && m_scList[m_selScen].Supported()) {
                        EndDialog(DLG_RETCODE.OK.v)
                    }
                }
            }
        }
    }

    private companion object {

        private const val yoffs = 18
    }
}
