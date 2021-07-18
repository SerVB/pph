package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.IsoMetric
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.common.CalcCellSeqGame
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.common.SurfaceType
import com.github.servb.pph.pheroes.common.creature.Perk
import com.github.servb.pph.pheroes.common.iSortArray
import com.github.servb.pph.pheroes.common.magic.SpellDisposition
import com.github.servb.pph.util.asPoint
import com.github.servb.pph.util.asSize
import com.github.servb.pph.util.helpertype.*
import com.soywiz.korio.lang.format
import com.soywiz.korma.geom.*
import kotlin.properties.Delegates

class iCreatInfoPopup : iFramePopupView {

    private val m_pGroup: iBattleGroup

    constructor(pViewMgr: iViewMgr, pid: PlayerId, pGroup: iBattleGroup) : super(pViewMgr, pid) {
        m_pGroup = pGroup
    }

    override fun DoCompose(clRect: IRectangleInt) {
        val rc = RectangleInt(clRect)
        val ct = m_pGroup.Type()
        val cnt = m_pGroup.Count()

        // title
        rc.y += gTextComposer.TextBoxOut(
            dlgfc_topic,
            gApp.Surface(),
            gTextMgr[ct.v * 3 + TextResId.TRID_CREATURE_PEASANT_F2.v],
            IRectangleInt(rc.x, rc.y, rc.width, 15)
        )

        // icon
        BlitIcon(gApp.Surface(), GfxId.PDGG_MINIMON.v + ct.v, IRectangleInt(rc.x, rc.y, rc.width, 45))
        rc.y += 45

        // Perks
        val perks = Perk.values().filter { (ct.descriptor!!.perks and it.v) != 0 }
        val tw = perks.size * 15 - 1
        var ox = rc.x + (rc.width / 2 - tw / 2)
        perks.forEach {
            gGfxMgr.Blit(GfxId.PDGG_CREAT_PERKS(it.ordinal - 1), gApp.Surface(), IPointInt(ox, rc.y))
            ox += 15
        }
        rc.y += 15

        // specs
        val lh = 10
        val trc = RectangleInt(rc.x, rc.y, 38, lh)
        val fc = iTextComposer.FontConfig(iTextComposer.FontSize.SMALL, IiDibFont.ComposeProps(RGB16(192, 192, 220)))
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_QUANT_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_ATTACK_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_DEFENCE_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh
        if (m_pGroup.CanShoot()) {
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                trc.asPoint(),
                gTextMgr[TextResId.TRID_SKILL_SHOTS_S] + " :",
                trc,
                Alignment.AlignTopRight
            )
            trc.y += lh
        }
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_DAMAGE_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_HEALTH_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_SPEED_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_MORALE_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            gTextMgr[TextResId.TRID_SKILL_LUCK_S] + " :",
            trc,
            Alignment.AlignTopRight
        )
        trc.y += lh

        fc.cmpProps.faceColor = RGB16(220, 220, 160)
        trc.setTo(rc.x + 40, rc.y, rc.width - 40, lh)
        gTextComposer.TextOut(fc, gApp.Surface(), trc.asPoint(), FormatNumber(cnt), trc, Alignment.AlignTopLeft)
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            "%d (%d)".format(ct.descriptor!!.attack, m_pGroup.AttackSkill()),
            trc,
            Alignment.AlignTopLeft
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            "%d (%d)".format(ct.descriptor.defence, m_pGroup.DefenceSkill()),
            trc,
            Alignment.AlignTopLeft
        )
        trc.y += lh
        if (m_pGroup.CanShoot()) {
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                trc.asPoint(),
                "%d/%d".format(m_pGroup.Shots(), ct.descriptor.shots),
                trc,
                Alignment.AlignTopLeft
            )
            trc.y += lh
        }
        if (m_pGroup.MinDamage() == m_pGroup.MaxDamage()) {
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                trc.asPoint(),
                FormatNumber(m_pGroup.MinDamage()),
                trc,
                Alignment.AlignTopLeft
            )
        } else {
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                trc.asPoint(),
                "%d-%d".format(m_pGroup.MinDamage(), m_pGroup.MaxDamage()),
                trc,
                Alignment.AlignTopLeft
            )
        }
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            "%d/%d".format(m_pGroup.HitPointsLeft(), m_pGroup.HitPoints()),
            trc,
            Alignment.AlignTopLeft
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            "%d (%d)".format(ct.descriptor.speed, m_pGroup.Speed()),
            trc,
            Alignment.AlignTopLeft
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            FormatNumber(m_pGroup.Morale()),
            trc,
            Alignment.AlignTopLeft
        )
        trc.y += lh
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            trc.asPoint(),
            FormatNumber(m_pGroup.Luck()),
            trc,
            Alignment.AlignTopLeft
        )
        trc.y += lh

        // spells
        // todo
    }

    override fun ClientSize(): SizeInt {
        val w = 80
        var h = 0

        // title
        h += gTextComposer.GetTextBoxSize(
            gTextMgr[m_pGroup.Type().v * 3 + TextResId.TRID_CREATURE_PEASANT_F2.v],
            80,
            dlgfc_topic
        ).height

        // icon
        h += 45

        // perks
        h += 15

        // specs
        h += 8 * 10
        if (m_pGroup.CanShoot()) {
            h += 10
        }

        // spells
//        if (m_pGroup.SpellList().GetSize()) {  // todo
//            h += 10 + minOf(5, m_pGroup.SpellList().GetSize()) * 10
//        }

        h += 5
        return SizeInt(w, h)
    }
}

enum class BattleNavMode(override val v: Int) : CountValueEnum, UniqueValueEnum {

    MELEE(0),
    SHOOT(1),
    INFO(2),
    COUNT(3),
}

class iBatObstEntry {

    val obstId: UShort
    val cell: IPointInt

    constructor(_obstOd: UShort, _cell: IPointInt) {
        obstId = _obstOd
        cell = PointInt(_cell)
    }
}

typealias iBatObstList = MutableList<iBatObstEntry>

private enum class BATTLE_CONTROLS_IDS(override val v: Int) : UniqueValueEnum {

    DEFAULT(1024),
    ASSAULTER_BTN(1025),
    DEFENDER_BTN(1026),
    MODE_SWITCH(1027),
    WAIT_BTN(1028),
    DEFEND_BTN(1029),
    MSGLOG_BTN(1030),
    AUTOBATTLE_BTN(1031),
    CASTSPELL_BTN(1032),
    SETTINGS_BTN(1033),
}

/*
 *	Battle composer
 */
abstract class iBatCmpItem {

    val m_pos: IPointInt
    val m_bCurrent: Boolean

    constructor(pos: IPointInt, bCurrent: Boolean) {
        m_pos = PointInt(pos)
        m_bCurrent = bCurrent
    }

    abstract fun ComposeShadow(surf: iDib, anchor: IPointInt)
    abstract fun Compose(surf: iDib, anchor: IPointInt, bInt: Boolean)
}

class iBatCmpItem_Creature : iBatCmpItem {

    val m_pGroup: iBattleGroup

    constructor(pGroup: iBattleGroup, bCurrent: Boolean) : super(pGroup.Pos(), bCurrent) {
        m_pGroup = pGroup
    }

    override fun ComposeShadow(surf: iDib, anchor: IPointInt) {
        val cop = PointInt(anchor)
        val ct = m_pGroup.Type()

        if (ct.descriptor!!.size == 1) {
            cop.x -= 11
        } else if (ct.descriptor.size == 2) {
            if (m_pGroup.Orient() == iBattleGroup.ORIENT.Right) {
                cop.x -= 22
            }
        }

        if (m_pGroup.IsAlive()) {
            var sid =
                (if (m_pGroup.Orient() === iBattleGroup.ORIENT.Right) GfxId.PDGG_MINIMON else GfxId.PDGG_RMINIMON)(ct.v)
            if (m_pGroup.State() == iBattleGroup.STATE.Melee || m_pGroup.State() == iBattleGroup.STATE.Shooting) {
                sid += 45
            }
            gGfxMgr.BlitEffect(sid, surf, cop, iGfxManager.Effects.ShadowIso)
        } else {
            val sid =
                (if (m_pGroup.Orient() === iBattleGroup.ORIENT.Right) GfxId.PDGG_MINIMON else GfxId.PDGG_RMINIMON)(ct.v + 135)
            gGfxMgr.Blit(sid, surf, cop)
        }
    }

    override fun Compose(surf: iDib, anchor: IPointInt, bInt: Boolean) {
        val ct = m_pGroup.Type()
        val cop = PointInt(anchor)

        if (!m_pGroup.IsAlive()) {
            return
        }

        if (ct.descriptor!!.size == 1) {
            cop.x -= 11
        } else if (ct.descriptor.size == 2) {
            if (m_pGroup.Orient() == iBattleGroup.ORIENT.Right) {
                cop.x -= 22
            }
        }

        // Draw group
        var sid =
            (if (m_pGroup.Orient() === iBattleGroup.ORIENT.Right) GfxId.PDGG_MINIMON else GfxId.PDGG_RMINIMON)(ct.v)
        if (m_pGroup.State() == iBattleGroup.STATE.Melee || m_pGroup.State() == iBattleGroup.STATE.Shooting) {
            sid += 45
        } else if (m_pGroup.State() == iBattleGroup.STATE.RecDamage) {
            sid += 90
        }
        gGfxMgr.Blit(sid, surf, cop)

        // Draw current group edging
        if (bInt && m_bCurrent) {
            val frameSid =
                (if (m_pGroup.Orient() === iBattleGroup.ORIENT.Right) GfxId.PDGG_MINIMONF else GfxId.PDGG_RMINIMONF)(ct.v)
            gGfxMgr.Blit(frameSid, surf, cop)
        }

        // Draw group's quanity
        val p = PointInt(anchor)
        val rc = IRectangleInt(p.x + 1, p.y + 9, 20, 11)
        val sd = m_pGroup.SpellsDispos()
        when (sd) {
            SpellDisposition.NEUTRAL -> surf.VGradientRect(rc, RGB16(140, 140, 46), RGB16(58, 58, 0))
            SpellDisposition.POSITIVE -> surf.VGradientRect(rc, RGB16(56, 160, 22), RGB16(32, 64, 16))
            SpellDisposition.NEGATIVE -> surf.VGradientRect(rc, RGB16(192, 76, 32), RGB16(80, 32, 16))
            else -> surf.VGradientRect(rc, RGB16(120, 76, 160), RGB16(44, 8, 80))
        }

        // commented in sources:
        //if (composeList[xx].value->PassMap().GetAt(composeList[xx].value->Pos().x+TAIL_OFFSET[!composeList[xx].value->Orient()], composeList[xx].value->Pos().y) == CT_PASSABLE) rc.x += TAIL_OFFSET[!composeList[xx].value->Orient()] * 20;
        /*if (m_pGroup->InMoat()) surf.VGradientRect(rc,RGB16(192,76,32),RGB16(80,32,16));
        else */
        val fc = iTextComposer.FontConfig(
            iTextComposer.FontSize.MEDIUM,
            IiDibFont.ComposeProps(RGB16(255, 255, 192), RGB16(48, 48, 64))
        )
        surf.FrameRect(rc, RGB16(208, 176, 28))
        gTextComposer.TextOut(fc, surf, rc.asPoint(), FormatShortNumber(m_pGroup.Count()), rc, Alignment.AlignCenter)

        // receive damage glyph
        // todo
    }
}

private val SKY_GRAD_COLORS = listOf(
    // size = [STYPE_COUNT][2]
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Water
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Sand
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Dirt
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Grass
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Swamp
    RGB16(16, 0, 0) to RGB16(255, 0, 0),  // Lava
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Wasteland
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Desert
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Snow
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // New Desert
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // Pavement
    RGB16(255, 255, 255) to RGB16(64, 192, 255),  // New Wasteland
)

private val TOOLTIP_COLORS = listOf(
    // size = [STYPE_COUNT][2]
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Water
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Sand
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Dirt
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Grass
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Swamp
    RGB16(16, 0, 0) to RGB16(255, 128, 128),  // Lava
    RGB16(0, 64, 128) to RGB16(255, 255, 255),  // Wasteland
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Desert
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Snow
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // New Desert
    RGB16(32, 32, 96) to RGB16(220, 220, 255),  // Pavement
    RGB16(0, 64, 128) to RGB16(255, 255, 255),  // New Wasteland
)

/*
 *	Helpers and constants
 *  TODO:: PRE: Hides declaration - need to rename const
 */
private val anchor = IPointInt(13, 31)  //(35,45);  // commented in sources

fun Map2Screen(pos: IPointInt): PointInt {
    return PointInt(anchor.x + pos.x * 24 - (pos.y and 1) * 13, anchor.y + pos.y * 17)
}

fun Screen2Map(pos: IPointInt): PointInt {
    val y = (pos.y - anchor.y) / 17
    val x = (pos.x - anchor.x + (y and 1) * 13) / 24
    return PointInt(x, y)
}

private val BATTLE_POPUP_BOUND = IRectangleInt(2, 2, 316, 216)

class iBattleView : iChildGameView {

    class iShootEntry {

        val m_penalty: Int
        val m_pos: IPointInt

        constructor(pos: IPointInt, penalty: Int) {
            m_pos = PointInt(pos)
            m_penalty = penalty
        }
    }

    //    private val m_pCastSpellToolBar: iCastSpellToolBar  // todo
    private val m_pToolBar: iBattleToolBar
    private var m_pAutoBattleToolBar: iAutoBattleToolBar?
    private var m_battleMode: BattleNavMode
    private var m_pBattle: iBattleWrapper?
    private lateinit var m_surfType: SurfaceType
    private val m_trackPos: PointInt
    private val m_trackCell: PointInt
    private val m_dibSurf: iDib = iDib()
    private val m_dibTurnGlyph: iDib = iDib()
    private var m_bForceInfo: Boolean
    private var m_bHumanTurn: Boolean
    private var m_toolTip: String = ""

//    private val m_obstacles: iBatObstList  // todo

    //    private val m_cellEvents: iEventList  // todo
    private val m_log: MutableList<String> = mutableListOf()

    private var m_pCreatInfoPopup: iCreatInfoPopup?

    private var m_actTimer: Double by Delegates.notNull()
    private var m_bAni: Boolean

//    private val m_pMeleeTrack: iBattleGroup.iMeleeEntry  // todo
//    private val m_meleeDir: Int  // todo

    private var m_pShootTrack: iShootEntry? = null

//    private val m_spellTargets: MutableList<IPointInt>  // todo

    constructor() : super(false, CHILD_VIEW.MENU) {  // todo: change to OVERLAND
        m_trackCell = PointInt(cInvalidPoint)
        m_trackPos = PointInt(cInvalidPoint)
        m_pBattle = null
        m_battleMode = BattleNavMode.MELEE
        m_bAni = false
//        m_bMeleeTrack = null  // todo
//        m_pShootTrack = null  // todo
        m_pAutoBattleToolBar = null
        m_pCreatInfoPopup = null
//        m_pCastSpellToolbar = null  // todo
        m_bForceInfo = false
        m_bHumanTurn = false

        m_pToolBar = iBattleToolBar(gApp.ViewMgr(), this, IRectangleInt(0, m_Rect.y2 - 20, m_Rect.width, 21))
        AddChild(m_pToolBar)
    }

    fun BeginBattle(pBattle: iBattleWrapper, st: SurfaceType) {
        m_pBattle = pBattle
        m_surfType = st
        val pAssaulter = m_pBattle!!.Engine().GetBattleInfo().m_pAssaulter as? iBattleMember_Hero
        check(pAssaulter != null)
//        m_pToolBar.m_pBtnAssaulter.SetHero(pAssaulter.GetHero())  // todo
        val pHero = m_pBattle!!.Engine().GetBattleInfo().m_pDefender as? iBattleMember_Hero
        if (pHero != null) {
//            m_pToolBar.m_pBtnDefender.SetHero(pHero.GetHero())  // todo
        } else {
            val pCastle = m_pBattle!!.Engine().GetBattleInfo().m_pDefender as? iBattleMember_Castle
            if (pCastle != null) {
//                if (pCastle.GetVisitor() != null) {  // todo
//                    m_pToolBar.m_pBtnDefender.SetHero(pCastle.GetVisitor())
//                }
            }
        }
        PrepareSurface()

        // Prepare and distribute obstacles
        // todo
    }

    fun Start() {
//        gSfxMgr.PlaySound(CSND_PREBATTLE)  // todo
        OnGroupChanged()
    }

    suspend fun EndBattle(br: iBattleResult) {
        // Prepare battle result
        m_pBattle!!.Engine().PrepareBattleResult(br)

        // Show result
        // todo

        // End battle
        m_pBattle!!.EndBattle(br)
    }

    fun OnKeyDown(key: Int): Boolean {
        if (!m_bHumanTurn) {
            return false
        }
        if (key == gSettings.ActionKey(ButtonActionType.HELP_MODE).v) {
            m_bForceInfo = true
            m_pToolBar.SetEnabled(false)
        } else {
            return false
        }
        return true
    }

    fun OnKeyUp(key: Int): Boolean {
        if (!m_bHumanTurn) {
            return false
        }
        if (key == gSettings.ActionKey(ButtonActionType.HELP_MODE).v) {
            m_bForceInfo = false
            m_pToolBar.SetEnabled(true)
        } else {
            return false
        }
        return true
    }

    override suspend fun OnMouseDown(pos: IPointInt) {
        if (!m_bHumanTurn || m_bAni) {
            return
        }
        m_trackPos.setTo(pos)
        m_trackCell.setTo(Screen2Map(pos))

        if (SpellTracking()) {
            // todo
        } else if (m_battleMode == BattleNavMode.INFO || m_bForceInfo) {
            val pGroup = m_pBattle!!.Engine().FindGroup(m_trackCell)
            if (pGroup != null) {
                val creatInfoPopup = iCreatInfoPopup(m_pMgr, pGroup.Owner().Owner(), pGroup)
                m_pCreatInfoPopup = creatInfoPopup
                AddChild(creatInfoPopup)
                if (pGroup.Pos().x > 5) {
                    creatInfoPopup.TrackPopup(
                        IPointInt(BATTLE_POPUP_BOUND.x, BATTLE_POPUP_BOUND.y),
                        BATTLE_POPUP_BOUND,
                        Alignment.AlignLeft
                    )
                } else {
                    creatInfoPopup.TrackPopup(
                        IPointInt(BATTLE_POPUP_BOUND.x2, BATTLE_POPUP_BOUND.y),
                        BATTLE_POPUP_BOUND,
                        Alignment.AlignLeft
                    )
                }
            } else {
                val pCurCreatGroup = m_pBattle!!.Engine().TurnSeq().CurUnit() as iBattleUnit_CreatGroup
                val pCreatGroup = pCurCreatGroup.GetCreatGroup()
                // commented in sources:
                //OutputDebugString(iFormat(_T("Cells delta (%d,%d) -> (%d,%d) is %d\n"),pCreatGroup->Pos().x,pCreatGroup->Pos().y,m_trackCell.x,m_trackCell.y,CellsDelta2(pCreatGroup->Pos().x,pCreatGroup->Pos().y,m_trackCell.x,m_trackCell.y)).CStr())
            }
        } else if (m_battleMode == BattleNavMode.MELEE) {
            // todo
        } else if (m_battleMode == BattleNavMode.SHOOT) {
            val pCurCreatGroup = m_pBattle!!.Engine().TurnSeq().CurUnit() as iBattleUnit_CreatGroup
            m_pShootTrack = pCurCreatGroup.GetCreatGroup().GetShootEntry(m_trackCell)
            val pGroup = m_pBattle!!.Engine().FindGroup(m_trackCell)
            if (pGroup != null && m_pShootTrack != null) {
                m_toolTip =
                    gTextMgr[TextResId.TRID_MSG_BAT_SHOOT].format(gTextMgr[TextResId.TRID_CREATURE_PEASANT_F3.v + pGroup.Type().v * 3])
                val min_dmg = pCurCreatGroup.GetCreatGroup().CalcDamage(
                    pGroup,
                    pCurCreatGroup.GetCreatGroup().ActMinDamage(),
                    true,
                    m_pShootTrack!!.m_penalty,
                    pCurCreatGroup.GetCreatGroup().CalcJoustingBonus(pGroup)
                )
                val max_dmg = pCurCreatGroup.GetCreatGroup().CalcDamage(
                    pGroup,
                    pCurCreatGroup.GetCreatGroup().ActMaxDamage(),
                    true,
                    m_pShootTrack!!.m_penalty,
                    pCurCreatGroup.GetCreatGroup().CalcJoustingBonus(pGroup)
                )
                if (max_dmg == min_dmg) {
                    m_toolTip += gTextMgr[TextResId.TRID_MSG_BAT_DAMAGE1].format(min_dmg)
                } else {
                    m_toolTip += gTextMgr[TextResId.TRID_MSG_BAT_DAMAGE2].format(min_dmg, max_dmg)
                }
            }
            Invalidate()
        }
    }

    override suspend fun OnMouseUp(pos: IPointInt) {
        if (!m_bHumanTurn || m_bAni) {
            return
        }

        if (SpellTracking()) {
            EndSpellTrack(m_trackCell)
            Invalidate()
        } else if (m_pCreatInfoPopup != null || m_battleMode == BattleNavMode.INFO || m_bForceInfo) {
            m_pCreatInfoPopup?.let {
                RemoveChild(it)
                it.HidePopup()
                m_pCreatInfoPopup = null
            }
        } else if (m_battleMode == BattleNavMode.MELEE /*&& m_pMeleeTrack*/) {
            // todo
        } else if (m_battleMode == BattleNavMode.SHOOT) {
            if (m_pShootTrack != null) {
                m_pBattle!!.Engine().Shot(m_pShootTrack!!.m_pos, m_pShootTrack!!.m_penalty)
                BeginAni()
                m_pShootTrack = null
            }
        } else {
            val pCurCreatGroup = m_pBattle!!.Engine().TurnSeq().CurUnit() as? iBattleUnit_CreatGroup
            if (pCurCreatGroup != null) {
                val nCell = Screen2Map(pos)
                if (nCell == m_trackCell && pCurCreatGroup.GetCreatGroup()
                        .CanMove(nCell.x, nCell.y) && !pCurCreatGroup.GetCreatGroup().IsGroupCell(nCell)
                ) {
                    m_pBattle!!.Engine().Move(nCell, pCurCreatGroup.GetCreatGroup().Orient())
                    BeginAni()
                }
            }
        }
        m_trackCell.setTo(cInvalidPoint)
        m_trackPos.setTo(cInvalidPoint)
        if (m_toolTip.isNotEmpty()) {
            m_toolTip = ""
        }
        Invalidate()
    }

    override suspend fun OnMouseTrack(pos: IPointInt) {
        if (!m_bHumanTurn || m_bAni || m_pCreatInfoPopup != null) {
            return
        }
        m_trackPos.setTo(pos)

        if (SpellTracking()) {
            // todo
        } else if (m_battleMode == BattleNavMode.MELEE /*&& m_pMeleeTrack*/) {
            // todo
        } else if (m_battleMode == BattleNavMode.SHOOT) {
            m_trackCell.setTo(Screen2Map(pos))
            val pCurCreatGroup = m_pBattle!!.Engine().TurnSeq().CurUnit() as iBattleUnit_CreatGroup
            val ncell = Screen2Map(pos)
            if (m_pShootTrack == null || ncell != m_pShootTrack!!.m_pos) {
                m_pShootTrack = pCurCreatGroup.GetCreatGroup().GetShootEntry(ncell)
            }

            val pGroup = m_pBattle!!.Engine().FindGroup(ncell)
            if (m_pShootTrack != null && pGroup != null) {
                m_toolTip =
                    gTextMgr[TextResId.TRID_MSG_BAT_SHOOT].format(gTextMgr[TextResId.TRID_CREATURE_PEASANT_F3.v + pGroup.Type().v * 3])
                val min_dmg = pCurCreatGroup.GetCreatGroup().CalcDamage(
                    pGroup,
                    pCurCreatGroup.GetCreatGroup().ActMinDamage(),
                    true,
                    m_pShootTrack!!.m_penalty,
                    pCurCreatGroup.GetCreatGroup().CalcJoustingBonus(pGroup)
                )
                val max_dmg = pCurCreatGroup.GetCreatGroup().CalcDamage(
                    pGroup,
                    pCurCreatGroup.GetCreatGroup().ActMaxDamage(),
                    true,
                    m_pShootTrack!!.m_penalty,
                    pCurCreatGroup.GetCreatGroup().CalcJoustingBonus(pGroup)
                )
                if (max_dmg == min_dmg) {
                    m_toolTip += gTextMgr[TextResId.TRID_MSG_BAT_DAMAGE1].format(min_dmg)
                } else {
                    m_toolTip += gTextMgr[TextResId.TRID_MSG_BAT_DAMAGE2].format(min_dmg, max_dmg)
                }
            } else {
                m_toolTip = ""
            }

            Invalidate()
        }
    }

    override suspend fun Process(t: Double): Boolean {
        // actions
        if (m_pBattle!!.Engine().ActionCount() != 0) {
            m_actTimer += t
            while (m_pBattle!!.Engine().ActionCount() != 0 && m_actTimer >= m_pBattle!!.Engine()
                    .CurAction()!!.m_delay
            ) {
                val pAct = m_pBattle!!.Engine().StepAction()
                Invalidate()
                m_actTimer -= pAct!!.m_delay

                // No more actions for current group
                if (m_pBattle!!.Engine().ActionCount() == 0) {
                    EndAni()
                    // Check victory conditions
                    val br = m_pBattle!!.Engine().CheckBattleState()
                    if (br != BATTLE_RESULT.NA) {
                        EndBattle(iBattleResult(br, DEFEAT_CAUSE.DEFEAT))
                        return true
                    }
                    m_pBattle!!.Engine().NextGroup()
                    if (!OnGroupChanged()) {
                        return true
                    }
                    Invalidate()
                }
            }
        }

        // events
        // todo

        return true
    }

    fun PrepareSurface() {
        m_dibSurf.Init(m_Rect.asSize(), IiDib.Type.RGB)

        val im = IsoMetric(5)

        val step_x = im.cellStepX * 2
        val step_y = im.cellStepY
        val cnt_x = 10
        val cnt_y = 20

        // Draw background surface
        repeat(cnt_y) { yy ->
            repeat(cnt_x) { xx ->
                var xpos = xx * step_x
                val ypos = yy * step_y
                if (yy % 2 != 0) {
                    xpos -= step_x shr 1
                }
                val op = IPointInt(xpos - step_x, ypos + 20)
                val cseq = CalcCellSeqGame(IPointInt(xx, yy), 4u).toInt()
                gGfxMgr.BlitMasked(
                    GfxId.PDGG_SURF_TILES(m_surfType.v * 4 + cseq),
                    GfxId.PDGG_TRANS_TILES(14),
                    m_dibSurf,
                    op
                )  // todo: it seems that we need either provide GfxId.PDGG_TRANS_TILES(14) or change it to WS_TILES(0)
            }
        }

        // Draw moat
        if (false) {  // todo
            if (true) {  // todo
                gGfxMgr.Blit(GfxId.PDGG_COMBAT_MOAT.v, m_dibSurf, Map2Screen(IPointInt(8, 0)))
            }
        }

        // Draw Sky
        m_dibSurf.VGradientRect(
            IRectangleInt(m_Rect.x, m_Rect.y, m_Rect.width, 30),
            SKY_GRAD_COLORS[m_surfType.v].first,
            SKY_GRAD_COLORS[m_surfType.v].second
        )
        m_dibSurf.HLine(IPointInt(m_Rect.x, m_Rect.y + 30), m_Rect.x2, SKY_GRAD_COLORS[m_surfType.v].second, 192u)
        m_dibSurf.HLine(IPointInt(m_Rect.x, m_Rect.y + 31), m_Rect.x2, SKY_GRAD_COLORS[m_surfType.v].second, 128u)
        m_dibSurf.HLine(IPointInt(m_Rect.x, m_Rect.y + 32), m_Rect.x2, SKY_GRAD_COLORS[m_surfType.v].second, 64u)

//        m_dibSurf.FillRect(IRectangleInt(0,0,m_Rect.width,30),cColor.Blue.pixel)  // commented in sources
    }

    override fun OnCompose() {
        // Compose background
        m_dibSurf.CopyToDibXY(gApp.Surface(), IPointInt(0, 0))
        if (m_pBattle == null) {
            return
        }

        // Show turn sequence (for current round)
        val glyphSize = ISizeInt(30, 26)
        if (m_dibTurnGlyph.GetSize() != glyphSize) {
            m_dibTurnGlyph.Resize(glyphSize)
        }
        val turnSeq = m_pBattle!!.Engine().TurnSeq()
        val tseqRect = RectangleInt(1, 1, glyphSize.width, glyphSize.height)
        repeat(10) { xx ->
            gApp.Surface().CopyRectToDibXY(m_dibTurnGlyph, tseqRect, IPointInt(0, 0))
            if (xx < turnSeq.GetTurnSeqSize()) {
                val curTurnSeqItem = turnSeq.GetTurnSeqItem(xx)
                when (curTurnSeqItem) {
                    is iBattleUnit_CreatGroup -> ComposeCreatureIcon(
                        m_dibTurnGlyph,
                        IRectangleInt(1, 1, glyphSize.width - 2, glyphSize.height - 2),
                        curTurnSeqItem.GetCreatGroup().Type(),
                        curTurnSeqItem.Owner().GetSide() == iBattleMember.Side.Defender
                    )
//                    is iBattleUnit_Catapult -> gGfxMgr.Blit(GfxId.PDGG_ICN_CATAPULT.v, m_dibTurnGlyph, IPointInt(1,1))  // todo
//                    is iBattleUnit_Turret -> gGfxMgr.Blit(GfxId.PDGG_ICN_TURRET.v, m_dibTurnGlyph, IPointInt(1,1))  // todo
                }
            } else {
                // commented in sources:
                //gGfxMgr.BlitTile(PDGG_BKTILE, m_dibTurnGlyph ,glyphSize);
                //ButtonFrame(m_dibTurnGlyph, glyphSize, 0);
            }
            m_dibTurnGlyph.CopyToDibXY(gApp.Surface(), tseqRect.asPoint(), ((15 - xx) * 10).toUByte())
            tseqRect.x += 32
        }

        val pCurUnit = m_pBattle!!.Engine().TurnSeq().CurUnit()
        val pid = pCurUnit!!.Owner().Owner()
        val bInt = pid != PlayerId.NEUTRAL &&
//                gGame.Map().FindPlayer(pid).PlayerType() != PlayerType.COMPUTER &&  // todo
                m_pBattle!!.Engine().ActionCount() == 0 &&
                m_pBattle!!.Engine().CheckBattleState() == BATTLE_RESULT.NA

        val op = PointInt()
        // Draw Grid
        if (gSettings.GetEntryValue(ConfigEntryType.COMBATGRID) != 0) {
            op.setTo(anchor)
            repeat(11) { yy ->
                repeat(13) { xx ->
                    gGfxMgr.BlitEffect(GfxId.PDGG_GRID_HEX.v, gApp.Surface(), op, iGfxManager.Effects.Transparent)
                    // Draw cover map (only for interactive side)
                    // todo
                    op.x += 24
                }
                op.y += 17
                op.x = anchor.x
                if (yy % 2 == 0) {
                    op.x -= 13
                }
            }
        }

        // Show path (for debug)  // commented in sources
        //if (!m_Actions.GetSize()) for (uint32 pp=0; pp<m_Path.GetSize(); ++pp) gfxMgr.BlitEffect( PDGG_HEXCELLS, dib, Map2Screen(m_Path[pp]), iGfxManager::EfxTransparent);

        // Draw current troop and targets highlighters (only for interactive side)
        if (bInt) {
            if (SpellTracking()) {
                // todo
            } else {
                val pCurCreatGroup = pCurUnit as? iBattleUnit_CreatGroup
                if (pCurCreatGroup != null) {
                    when (m_battleMode) {
                        BattleNavMode.MELEE -> {
                            // todo
                        }
                        BattleNavMode.SHOOT -> {
                            // todo
                        }
                    }
                }
//                if (m_pMeleeTrack)  // todo
            }
        }

        val cmpList = iSortArray<iBatCmpItem>()

        // Add castle's fort elements
        // todo

        // Add units
        op.setTo(anchor)
        var pGroup: iBattleGroup? = null
        val pCurCreatGroup = pCurUnit as? iBattleUnit_CreatGroup
        if (pCurCreatGroup != null) {
            pGroup = pCurCreatGroup.GetCreatGroup()
        }
        fun add(army: iBattleArmy) {
            repeat(army.Count()) { xx ->
                var value = army[xx].Pos().y * 13 + army[xx].Pos().x
                if (army[xx].State() == iBattleGroup.STATE.Melee || army[xx].State() == iBattleGroup.STATE.Shooting) {
                    value += 2
                }
                cmpList.Insert(iBatCmpItem_Creature(army[xx], army[xx] == pGroup && !SpellTracking()), value)
            }
        }

        add(m_pBattle!!.Engine().AArmy())
        add(m_pBattle!!.Engine().DArmy())

        // Add obstacles
        // todo

        // Draw shadow
        repeat(cmpList.Size()) { xx ->
            cmpList[xx].value.ComposeShadow(gApp.Surface(), Map2Screen(cmpList[xx].value.m_pos))
        }

        // Draw sprites
        repeat(cmpList.Size()) { xx ->
            cmpList[xx].value.Compose(gApp.Surface(), Map2Screen(cmpList[xx].value.m_pos), bInt)
        }

        // Show toolTip
        if (m_toolTip.isNotEmpty()) {
            val fontcfg = iTextComposer.FontConfig(
                iTextComposer.FontSize.MEDIUM,
                IiDibFont.ComposeProps(
                    TOOLTIP_COLORS[m_surfType.v].second,
                    TOOLTIP_COLORS[m_surfType.v].first,
                    IiDibFont.Decor.Border,
                )
            )
            gTextComposer.TextOut(
                fontcfg,
                gApp.Surface(),
                IPointInt(0, 0),
                m_toolTip,
                IRectangleInt(0, 0, 320, 30),
                Alignment.AlignCenter
            )
        }

        // Draw  cursor and action info
        if (SpellTracking()) {
            // todo
        } else if (m_battleMode == BattleNavMode.MELEE) {
            // todo
        } else if (m_battleMode == BattleNavMode.SHOOT) {
            m_pShootTrack?.let {
                var sid = GfxId.PDGG_SHOOT_CURSOR.v
                if (it.m_penalty > 1) {
                    sid += it.m_penalty / 2
                }
                val cop = IPointInt(
                    m_trackPos.x - gGfxMgr.Dimension(sid).width / 2,
                    m_trackPos.y - gGfxMgr.Dimension(sid).height / 2
                )
                gGfxMgr.BlitEffect(sid, gApp.Surface(), cop, iGfxManager.Effects.Shadow2D)
                gGfxMgr.Blit(sid, gApp.Surface(), cop)
            }
        }
    }

    fun AddLogEvent(msg: String) {
        m_log.add(msg)
    }

    fun AddCellEvent(msg: String, pos: IPointInt) {
//        m_cellEvents.add(iGameEvent(2.0, msg, pos))  // todo
    }

    private fun BeginAutobattle(): Unit = TODO()

    private fun EndAutobattle(): Unit = TODO()

    private fun IsAutobattle(): Boolean = false  // todo

    private fun BeginSpellTrack(): Boolean = TODO()

    private fun EndSpellTrack(cell: IPointInt): Unit = TODO()

    private fun SpellTracking(): Boolean = false // todo

    private fun OnGroupChanged(): Boolean {
        val pCurUnit = m_pBattle!!.Engine().TurnSeq().CurUnit()
        val pCurCreatGroup = pCurUnit as? iBattleUnit_CreatGroup
        if (pCurCreatGroup != null) {
            val pGroup = pCurCreatGroup.GetCreatGroup()
            // First check morale
            if (pGroup.RoundMorale() == iBattleGroup.MORLUCK_MOD.NEGATIVE) {
                m_pBattle!!.Engine().BadMorale()
                return true
            }

            val pid = pGroup.Owner().Owner()
            val bAiPlayer = pid == PlayerId.NEUTRAL // ||  // todo
//                    gGame.Map().FindPlayer(pid).PlayerType() == PlayerType.COMPUTER  // todo
            if (bAiPlayer || IsAutobattle()) {
                TODO()
            } else {
                m_bHumanTurn = true
                var flags = iBattleToolBar.StateFlags.CanInfo or iBattleToolBar.StateFlags.CanMelee
                val bCanShot = pGroup.Shots() != 0 /*&& pGroup.ShotListCount()*/ // todo
                if (bCanShot) {
                    flags = flags or iBattleToolBar.StateFlags.CanShoot
                }
                if (pGroup.Owner().CanCastSpell()) {
                    flags = flags or iBattleToolBar.StateFlags.CanCast
                }
                if (m_pBattle!!.Engine().CanWait()) {
                    flags = flags or iBattleToolBar.StateFlags.CanWait
                }
                m_pToolBar.EnableControls(flags)
                if (m_battleMode != BattleNavMode.SHOOT && bCanShot) {
                    m_battleMode = BattleNavMode.SHOOT
                    m_pToolBar.m_pModeSwitch.SetCurrentTab(BattleNavMode.SHOOT.v)
                } else if (!bCanShot && m_battleMode != BattleNavMode.MELEE) {
                    m_battleMode = BattleNavMode.MELEE
                    m_pToolBar.m_pModeSwitch.SetCurrentTab(BattleNavMode.MELEE.v)
                }
            }
        } // todo: else
        return true
    }

    private fun BeginAni() {
        //gLogMgr.Log(iFormat(_T("BeginAni: %d actions\r\n"),m_pBattle->Engine().ActionCount()));  // commented in sources
        m_pToolBar.EnableControls(iBattleToolBar.StateFlags.Acting.v)
        m_bAni = true
        m_actTimer = 0.0
        Invalidate()
    }

    private fun EndAni() {
        //gLogMgr.Log(_T("EndAni\r\n"));  // commented in sources
        m_bAni = false
    }

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID().toInt()

        // todo
        if (uid == BATTLE_CONTROLS_IDS.WAIT_BTN.v) {
            val tdlg = iTextDlg(
                m_pMgr,
                "",
                gTextMgr[TextResId.TRID_MSG_NOTIMPL],
                m_pBattle!!.Engine().TurnSeq().CurUnit()!!.Owner().Owner()
            )
            tdlg.DoModal()
//            m_pBattle!!.Engine().Wait()  // todo
//            OnGroupChanged()
        } else if (uid == BATTLE_CONTROLS_IDS.DEFEND_BTN.v) {
            val tdlg = iTextDlg(
                m_pMgr,
                "",
                gTextMgr[TextResId.TRID_MSG_NOTIMPL],
                m_pBattle!!.Engine().TurnSeq().CurUnit()!!.Owner().Owner()
            )
            tdlg.DoModal()
//            m_pBattle!!.Engine().Defend()  // todo
//            OnGroupChanged()
        } else if (uid == BATTLE_CONTROLS_IDS.CASTSPELL_BTN.v) {
            val tdlg = iTextDlg(
                m_pMgr,
                "",
                gTextMgr[TextResId.TRID_MSG_NOTIMPL],
                m_pBattle!!.Engine().TurnSeq().CurUnit()!!.Owner().Owner()
            )
            tdlg.DoModal()
            // todo
        } else if (uid == BATTLE_CONTROLS_IDS.AUTOBATTLE_BTN.v) {
            val tdlg = iTextDlg(
                m_pMgr,
                "",
                gTextMgr[TextResId.TRID_MSG_NOTIMPL],
                m_pBattle!!.Engine().TurnSeq().CurUnit()!!.Owner().Owner()
            )
            tdlg.DoModal()
            // BeginAutobattle() // todo
        } else if (uid == BATTLE_CONTROLS_IDS.MSGLOG_BTN.v) {
            val tdlg = iTextDlg(
                m_pMgr,
                "",
                gTextMgr[TextResId.TRID_MSG_NOTIMPL],
                m_pBattle!!.Engine().TurnSeq().CurUnit()!!.Owner().Owner()
            )
            tdlg.DoModal()
            // todo
        } else if (uid == BATTLE_CONTROLS_IDS.MODE_SWITCH.v) {
            if (cmd == CTRL_CMD_ID.TABCHANGED) {
                m_battleMode = getByValue(param)
            }
        } else if (uid == BATTLE_CONTROLS_IDS.SETTINGS_BTN.v) {
            val tdlg = iTextDlg(
                m_pMgr,
                "",
                gTextMgr[TextResId.TRID_MSG_NOTIMPL],
                m_pBattle!!.Engine().TurnSeq().CurUnit()!!.Owner().Owner()
            )
            tdlg.DoModal()
            // todo
        }
    }
}

class iBattleToolBar : iView {

    val m_pBtnWait: iIconButton
    val m_pBtnDefend: iIconButton
    val m_pBtnCastSpell: iIconButton
    val m_pBtnAutoBattle: iIconButton
    val m_pBtnMsgLog: iIconButton
    val m_pBtnSettings: iIconButton

    //    val m_pBtnAssaulter: iHeroPortBtn  // todo
//    val m_pBtnDefender: iHeroPortBtn  // todo
    val m_pModeSwitch: iBarTabSwitch

    enum class StateFlags(override val v: Int) : UniqueValueEnum {
        EnemyTurn(0x01),
        Acting(0x02),
        CanMelee(0x04),
        CanShoot(0x08),
        CanInfo(0x10),
        CanCast(0x20),
        CanWait(0x40),
    }

    constructor(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler, rect: IRectangleInt) : super(
        pViewMgr,
        rect,
        VIEWCLSID.GENERIC_VIEWPORT,
        0u,
        ViewState.Enabled or ViewState.Visible
    ) {
        var xpos = 0

        // Assaulter
        // todo
        xpos += 32

        // splitter
        xpos += 1
        AddChild(iTBSplitter(pViewMgr, IRectangleInt(1, 1, 5 + 32, 20)))  // todo: change x to xpos, width to 5
        xpos += 5 + 1

        // Current creature mode tab
        m_pModeSwitch = iBarTabSwitch(
            pViewMgr,
            pCmdHandler,
            IRectangleInt(xpos, 1, BattleNavMode.COUNT.v * 25, 20),
            BattleNavMode.COUNT.v,
            BATTLE_CONTROLS_IDS.MODE_SWITCH.v.toUInt()
        )
        m_pModeSwitch.SetTabIcon(GfxId.PDGG_BTN_ATTACK.v)
        m_pModeSwitch.SetTabIcon(GfxId.PDGG_BTN_SHOOT.v)
        m_pModeSwitch.SetTabIcon(GfxId.PDGG_BTN_INFO.v)
        m_pModeSwitch.SetCurrentTab(BattleNavMode.MELEE.v)
        AddChild(m_pModeSwitch)
        xpos += BattleNavMode.COUNT.v * 25

        // splitter
        xpos += 1
        AddChild(iTBSplitter(pViewMgr, IRectangleInt(xpos, 1, 5, 20)))
        xpos += 5 + 1

        // Wait and Defend buttons
        m_pBtnWait = iIconButton(
            pViewMgr,
            pCmdHandler,
            IRectangleInt(xpos, 1, 25, 20),
            GfxId.PDGG_BTN_ENDTURN.v,
            BATTLE_CONTROLS_IDS.WAIT_BTN.v.toUInt()
        )
        AddChild(m_pBtnWait)
        xpos += 25
        m_pBtnDefend = iIconButton(
            pViewMgr,
            pCmdHandler,
            IRectangleInt(xpos, 1, 25, 20),
            GfxId.PDGG_BTN_DEFEND.v,
            BATTLE_CONTROLS_IDS.DEFEND_BTN.v.toUInt()
        )
        AddChild(m_pBtnDefend)
        xpos += 25

        // splitter
        xpos += 1
        AddChild(iTBSplitter(pViewMgr, IRectangleInt(xpos, 1, 5, 20)))
        xpos += 5 + 1

        // Cast spell button
        m_pBtnCastSpell = iIconButton(
            pViewMgr,
            pCmdHandler,
            IRectangleInt(xpos, 1, 24, 20),
            GfxId.PDGG_TAB_SPELLBOOK.v,
            BATTLE_CONTROLS_IDS.CASTSPELL_BTN.v.toUInt()
        )
        AddChild(m_pBtnCastSpell)
        xpos += 24

        // Autobattle button
        m_pBtnAutoBattle = iIconButton(
            pViewMgr,
            pCmdHandler,
            IRectangleInt(xpos, 1, 24, 20),
            GfxId.PDGG_BTN_AUTOBATTLE.v,
            BATTLE_CONTROLS_IDS.AUTOBATTLE_BTN.v.toUInt()
        )
        AddChild(m_pBtnAutoBattle)
        xpos += 24

        // splitter
        xpos += 1
        AddChild(iTBSplitter(pViewMgr, IRectangleInt(xpos, 1, 5, 20)))
        xpos += 5 + 1

        // Battle message log
        m_pBtnMsgLog = iIconButton(
            pViewMgr,
            pCmdHandler,
            IRectangleInt(xpos, 1, 24, 20),
            GfxId.PDGG_TAB_QUESTLOG.v,
            BATTLE_CONTROLS_IDS.MSGLOG_BTN.v.toUInt()
        )
        AddChild(m_pBtnMsgLog)
        xpos += 24

        // Settings button
        m_pBtnSettings = iIconButton(
            pViewMgr,
            pCmdHandler,
            IRectangleInt(xpos, 1, 24, 20),
            GfxId.PDGG_BTN_MAINMENU.v,
            BATTLE_CONTROLS_IDS.SETTINGS_BTN.v.toUInt()
        )
        AddChild(m_pBtnSettings)
        xpos += 24

        // splitter
        xpos += 1
        AddChild(iTBSplitter(pViewMgr, IRectangleInt(xpos, 1, 5 + 32, 20)))  // todo: change width to 5
        xpos += 5 + 1

        // Defender
        // todo
    }

    override fun OnCompose() {
        val rect = GetScrRect()
        gApp.Surface().FillRect(rect, cColor.Black.pixel)
    }

    fun EnableControls(flags: Int) {
        val bCommon = (flags and StateFlags.EnemyTurn) == 0 && (flags and StateFlags.Acting) == 0

//        m_pBtnAssaulter.SetEnabled(bCommon)  // todo
        m_pModeSwitch.SetEnabled(bCommon)
        m_pBtnWait.SetEnabled(bCommon && (flags and StateFlags.CanWait) != 0)
        m_pBtnDefend.SetEnabled(bCommon)
        m_pBtnCastSpell.SetEnabled(bCommon && (flags and StateFlags.CanCast) != 0)
        m_pBtnAutoBattle.SetEnabled(bCommon)
        m_pBtnMsgLog.SetEnabled(bCommon)
        m_pBtnSettings.SetEnabled(bCommon)
//        if (m_pBtnDefender.Hero()) m_pBtnDefender.SetEnabled(bCommon)  // todo

        m_pModeSwitch.EnableTab(BattleNavMode.MELEE.v, (flags and StateFlags.CanMelee) != 0)
        m_pModeSwitch.EnableTab(BattleNavMode.SHOOT.v, (flags and StateFlags.CanShoot) != 0)
        m_pModeSwitch.EnableTab(BattleNavMode.INFO.v, (flags and StateFlags.CanInfo) != 0)
    }
}

// todo
//class iCastSpellToolBar : iView {
//
//    private val m_pSpell: iCombatSpell
//
//    constructor(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler, rect: IRectangleInt, pSpell: iCombatSpell)
//
//    override fun OnCompose()
//
//    fun Spell(): iCombatSpell = m_pSpell
//}

class iAutoBattleToolBar : iView {

    constructor(pViewMgr: iViewMgr, pCmdHandler: IViewCmdHandler, rect: IRectangleInt) : super(
        pViewMgr,
        rect,
        VIEWCLSID.GENERIC_VIEWPORT,
        0u,
        ViewState.Enabled or ViewState.Visible
    ) {
        AddChild(
            iTextButton(
                pViewMgr,
                pCmdHandler,
                IRectangleInt(rect.width - 50, 1, 50, 20),
                TextResId.TRID_CANCEL,
                DLG_RETCODE.CANCEL.v.toUInt()
            )
        )
    }

    override fun OnCompose() {
        val rect = GetScrRect()
        gApp.Surface().FillRect(rect, cColor.Black.pixel)

        val rc = IRectangleInt(rect.x, rect.y + 1, rect.width - 51, rect.height - 1)
        gGfxMgr.BlitTile(GfxId.PDGG_BKTILE.v, gApp.Surface(), rc)
        ButtonFrame(gApp.Surface(), rc, 0)
        gTextComposer.TextOut(
            dlgfc_hdr,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_AUTO_COMBAT],
            rc,
            Alignment.AlignCenter
        )
    }
}
