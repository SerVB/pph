package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.cInvalidPoint
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.army.Army
import com.github.servb.pph.pheroes.common.army.CreatGroupC
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.common.skill.FurtherSkill
import com.github.servb.pph.pheroes.common.common.skill.FurtherSkills
import com.github.servb.pph.pheroes.common.creature.CreatureType
import com.github.servb.pph.pheroes.common.creature.Perk
import com.github.servb.pph.pheroes.common.creature.TransportationType
import com.github.servb.pph.pheroes.common.magic.SpellDisposition
import com.github.servb.pph.pheroes.common.magic.SpellLevel
import com.github.servb.pph.util.Mutable
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.and
import com.github.servb.pph.util.helpertype.getByValue
import com.soywiz.korio.lang.format
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.PointInt
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.random.Random

// Tail offsets [right, left direction]
val TAIL_OFFSET = listOf(-1, +1)

class iBattleGroup {

    enum class ORIENT(override val v: Int) : UniqueValueEnum {

        Right(0), Left(1),
    }

    enum class STATE(override val v: Int) : CountValueEnum, UniqueValueEnum {

        Idle(0),
        Moving(1),
        Rotating(2),
        Melee(3),
        Shooting(4),
        RecDamage(5),
        CastingSpell(6),
        ResistSpell(7),
        Dead(8),
        COUNT(9),
    }

    enum class MORLUCK_MOD(override val v: Int) : UniqueValueEnum {
        NEUTRAL(0),
        NEGATIVE(1),
        POSITIVE(2),
    }

    enum class AttackFlags(override val v: Int) : UniqueValueEnum {
        RangeAttack(0x1),
        LichCloud(0x2),
    }

    protected val m_furtSkills: FurtherSkills

    private var m_spImmunity: SizeT

    //    private val m_spells: iSpellList  // todo
    private var m_bBlinded: Boolean
    private var m_retPenalty: Int

    private val m_pOwner: iBattleMember
    private val m_pWrapper: iBattleWrapper
    private val m_Pos: PointInt
    private val m_Size: Int
    private val m_creatType: CreatureType
    private var m_creatCount: Int
    private val m_initCount: Int
    private val m_idx: Int
    private var m_blessState: Int

    private var m_casualties: Int

    private var m_bWaited: Boolean
    private var m_toDefend: Int
    private var m_retails: Int
    private val m_bMoraleAttack: Boolean
    private val m_bInMoat: Boolean
    private var m_roundMorale: MORLUCK_MOD
    private var m_hitPoints: Int
    private var m_shots: Int
    private var m_State: STATE
    private var m_Orient: ORIENT

    // todo: other 4 fields
    private val m_shotList: MutableList<iBattleView.iShootEntry> = mutableListOf()

    constructor(cg: CreatGroupC, idx: Int, pOwner: iBattleMember, pWrapper: iBattleWrapper, moraleModifier: Int) {
        m_creatType = cg.type
        m_initCount = cg.count
        m_creatCount = cg.count
        m_idx = idx
        m_bWaited = false
        m_toDefend = 0
        m_spImmunity = 0
        m_retails = 0
        m_bMoraleAttack = false
        m_bInMoat = false
        m_roundMorale = MORLUCK_MOD.NEUTRAL
        m_bBlinded = false
        m_retPenalty = 1
        m_blessState = 0
        m_hitPoints = cg.type.descriptor!!.hits + pOwner.FurtSkills().Value(FurtherSkill.HITS)
        m_shots = cg.type.descriptor!!.shots
        m_Pos = PointInt(cInvalidPoint)
        m_State = STATE.Idle
        m_pOwner = pOwner
        m_pWrapper = pWrapper
        m_Orient = ORIENT.values()[pOwner.GetSide().ordinal]
        m_Size = cg.type.descriptor!!.size
//        m_passMap = (13,11)  // todo
//        m_distMap = (13,11)  // todo
        m_casualties = 0
        m_furtSkills = FurtherSkills(pOwner.FurtSkills())

        // special creatures processing
        if ((m_creatType.descriptor!!.perks and Perk.PROCRESIST40) != 0) {
            m_furtSkills.SetValue(FurtherSkill.RESIST, 40 + m_furtSkills.Value(FurtherSkill.RESIST))
        }

        m_furtSkills.SetValue(FurtherSkill.MORALE, moraleModifier + m_furtSkills.Value(FurtherSkill.MORALE))

        NewRound()
    }

    fun Owner(): iBattleMember = m_pOwner
    fun Type(): CreatureType = m_creatType
    fun Count(): Int = m_creatCount
    fun InitialCount(): Int = m_initCount
    fun ArmyGroupIndex(): SizeT = m_idx
    fun Orient(): ORIENT = m_Orient
    fun State(): STATE = m_State
    fun IsAlive(): Boolean = m_State != STATE.Dead
    fun CanShoot(): Boolean = m_creatType.descriptor!!.shots != 0
    fun Shots(): Int = m_shots
    fun HitPointsLeft(): Int = m_hitPoints
    fun HitPoints(): Int = m_creatType.descriptor!!.hits + m_furtSkills.Value(FurtherSkill.HITS)
    fun TotalCasHitPoints(): Int = maxOf(0, m_initCount * HitPoints() - TotalHitPoints())
    fun TotalHitPoints(): Int = (m_creatCount - 1) * HitPoints() + m_hitPoints
    fun AttackSkill(): Int = maxOf(1, m_creatType.descriptor!!.attack + m_furtSkills.Value(FurtherSkill.ATTACK))
    fun DefenceSkill(): Int = maxOf(1, m_creatType.descriptor!!.defence + m_furtSkills.Value(FurtherSkill.DEFENCE))
    fun CanWait(): Boolean = !m_bWaited
    fun CanAct(): Boolean = !m_bBlinded

    fun CanRetail(): Boolean = (!m_bBlinded || m_retPenalty != 0) &&
            ((m_creatType.descriptor!!.perks and Perk.RETALTOALL) != 0 ||
                    m_retails < (1 + m_furtSkills.Value(FurtherSkill.COUNTERSTRIKE)))

    fun SetRetailed() {
        ++m_retails
    }

    fun Size(): Int = m_Size

    fun Morale(): Int = when ((m_creatType.descriptor!!.perks and Perk.UNDEAD) != 0) {
        true -> 0
        false -> m_furtSkills.Value(FurtherSkill.MORALE)
    }.coerceIn(-3, 3)

    fun Luck(): Int = m_furtSkills.Value(FurtherSkill.LUCK).coerceIn(-3, 3)
    fun RoundMorale(): MORLUCK_MOD = m_roundMorale

    fun ResetRoundMorale() {
        m_roundMorale = MORLUCK_MOD.NEUTRAL
    }

    fun CalcLuck(): MORLUCK_MOD {
        val lm = Luck()
        val rval = Random.nextInt(100)//gGame.Map().Rand(100) // todo
        if (lm > 0 && lm * 5 > rval) {
            return MORLUCK_MOD.POSITIVE
        } else if (lm < 0 && (-lm) * 5 > rval) {
            return MORLUCK_MOD.NEGATIVE
        } else {
            return MORLUCK_MOD.NEUTRAL
        }
    }

    // Common creature abilities
    fun TransType(): TransportationType = m_creatType.descriptor!!.transType
    fun Perks(): Int = m_creatType.descriptor!!.perks
    fun HasPerk(perk: Perk): Boolean = (m_creatType.descriptor!!.perks and perk) != 0
    fun Speed(): Int = maxOf(1, m_creatType.descriptor!!.speed + m_furtSkills.Value(FurtherSkill.SPEED))
    fun MinDamage(): Int = m_creatType.descriptor!!.damage_min
    fun MaxDamage(): Int = m_creatType.descriptor!!.damage_max

    fun ActMinDamage(): Int = when (m_blessState > 0) {
        true -> m_creatType.descriptor!!.damage_max
        false -> m_creatType.descriptor!!.damage_min
    }

    fun ActMaxDamage(): Int = when (m_blessState < 0) {
        true -> m_creatType.descriptor!!.damage_min
        false -> m_creatType.descriptor!!.damage_max
    }

    // Dislocation helpers
    fun IsGroupCell(pos: IPointInt): Boolean = m_Pos == pos || (m_Size == 2 && TailPos() == pos)
    fun Pos(): PointInt = PointInt(m_Pos)

    fun TailPos(): PointInt = when (m_Size) {
        1 -> Pos()
        else -> PointInt(m_Pos.x + TAIL_OFFSET[m_Orient.ordinal], m_Pos.y)
    }

    // Moat
    fun InMoat(): Boolean = m_bInMoat
    // todo

    // Spells
    // todo
    fun SpellsDispos(): SpellDisposition = SpellDisposition.NONE

    fun SetSpellImmunity(spim: SizeT) {
        m_spImmunity = spim
    }

    fun SpellImmunity(): SizeT = m_spImmunity
    fun SpellImmunity(splevel: SpellLevel): Boolean = (m_spImmunity and (1 shl splevel.v)) != 0

    // Bless / Curse state
    fun BlessState(): Int = m_blessState

    fun SetBlessState(newState: Int) {
        m_blessState = newState
    }

    // Blind state
    fun SetBlindState(bBlinded: Boolean, penalty: Int) {
        m_bBlinded = bBlinded
        m_retPenalty = penalty
    }

    fun IsBlinded(): Boolean = m_bBlinded
    fun GetRetPenalty(): Int = m_retPenalty

    //
    fun NewRound() {
        m_bWaited = false
        m_retails = 0

        // Defend
        if (m_toDefend != 0) {
            m_furtSkills.SetValue(FurtherSkill.DEFENCE, -m_toDefend + m_furtSkills.Value(FurtherSkill.DEFENCE))
            m_toDefend = 0
        }

        // Calculate morale state
        // todo

        // Process special perks
        if ((m_creatType.descriptor!!.perks and Perk.REGENERATES) != 0) {
            m_hitPoints = HitPoints()
        }

        // Update spells
        // todo
    }

    // todo

    fun GoodMorale() {
        m_pWrapper.AddLogEvent(
            "#S0${GetUnitsColor(m_pOwner.Owner())}" + gTextMgr[TextResId.TRID_MSG_GOOD_MORALE].format(
                gTextMgr[TextResId.TRID_CREATURE_PEASANT_F3.v + m_creatType.v * 3]
            )
        )
        m_pWrapper.AddCellEvent("#I%04X".format(GfxId.PDGG_ICN_MORALE.v), m_Pos)
    }

    fun BadMorale() {
        // todo
    }

    fun GoodLuck() {
        // todo
    }

    fun BadLuck() {
        // todo
    }

    fun CalcJoustingBonus(pTarget: iBattleGroup): SizeT {
        if ((m_creatType.descriptor!!.perks and Perk.JOUSTING) != 0) {
            return ((BattleCellsDelta(m_Pos.x, m_Pos.y, pTarget.Pos().x, pTarget.Pos().y) - 1) * 5).coerceIn(0, 50)
        }
        return 0
    }

    fun CalcDamage(pTarget: iBattleGroup, damage: Int, bRange: Boolean, penalty: Int, joustBonus: SizeT): Int {
        val udmg = damage.toDouble()

        var adif = (AttackSkill() - pTarget.DefenceSkill()).toDouble()
        // Increase melee attack skill according to Melee attack skill
        adif += m_furtSkills.Value(if (bRange) FurtherSkill.RANGEATTACK else FurtherSkill.MELEEATTACK)

        var damage = when (adif >= 0) {
            true -> maxOf(1, (udmg * (1.0 + 0.1 * minOf(adif, 20.0)) * m_creatCount).toInt())
            false -> maxOf(1, (udmg * (1.0 + 0.05 * maxOf(adif, -16.0)) * m_creatCount).toInt())
        }

        // Check half damage cases (far range attack and melee for shooters w/o NO_MELEE_PENALTY perk)
        if (bRange && penalty > 1) {
            damage = maxOf(damage / penalty, 1)
        } else if (!bRange && m_creatType.descriptor!!.shots != 0 && (m_creatType.descriptor.perks and Perk.NOMELEEPENALTY) == 0) {
            damage = maxOf(damage / 2, 1)
        }

        // Increase melee damage according to Offence skill
        // todo

        // Increase range attack damage according to Archery skill
        // todo

        // Increase damage according to jousting bonus
        if (joustBonus != 0) {
            damage += (damage * joustBonus) / 100
        }

        // Decrease damage according to Armorer skill
        // todo

        // Decrease damage according to air shield skill
        // todo

        // Decrease damage according to shield skill
        // todo

        return damage
    }

    fun Attack(pTarget: iBattleGroup, aflags: Int, penalty: Int, joustBonus: SizeT, luck: MORLUCK_MOD) {
        val bRange = (aflags and AttackFlags.RangeAttack) != 0

        // calculate damage
        var damage = CalcDamage(pTarget, ActMinDamage(), bRange, penalty, joustBonus)
        if (ActMinDamage() != ActMaxDamage()) {
            val max_dmg = CalcDamage(pTarget, ActMaxDamage(), bRange, penalty, joustBonus)
            damage += Random.nextInt(max_dmg - damage + 1)  // gGame.Map().Rand(max_dmg-damage+1) todo
        }

        if (bRange && (aflags and AttackFlags.LichCloud) != 0) {
            check(m_shots > 0)
            --m_shots
        }

        // Modify damage according to luck
        if (luck == MORLUCK_MOD.POSITIVE) {
            damage *= 2
        } else if (luck == MORLUCK_MOD.NEGATIVE) {
            damage = maxOf(damage / 2, 1)
        }

        // Genie's half
        if (HasPerk(Perk.DOHALF) /* todo */) {
            TODO()
        } else {
            m_pWrapper.AddLogEvent(
                "#S0${GetUnitsColor(m_pOwner.Owner())}" + gTextMgr[TextResId.TRID_MSG_BAT_DODAMAGE].format(
                    gTextMgr[TextResId.TRID_CREATURE_PEASANT_F3.v + m_creatType.v * 3],
                    damage
                )
            )
            val mutableDamage = Mutable(damage)
            val dead = pTarget.ReceiveDamage(mutableDamage, bRange)
            damage = mutableDamage.element
            //OutputDebugString(iFormat(_T("%s do %d damage to %s (penalty: %d)\n"),gTextMgr[TRID_CREATURE_PEASANT_F2+m_creatType*3],damage,gTextMgr[TRID_CREATURE_PEASANT_F2+pTarget->Type()*3],penalty).CStr());
            m_pOwner.AddExperience(damage)

            // special creatures
            if (HasPerk(Perk.GHOST)) {
                // Flies, creatures killed by Ghosts become Ghosts
                TODO()
            } else if (HasPerk(Perk.DRAINLIFES)) {
                // Regenerates from the blood of target
                TODO()
            }
        }
    }

    fun ReceiveDamage(damage: Mutable<Int>, bRange: Boolean): Int {
        var cellMsg = FormatNumber(damage.element)

        // Remove Paralize and Blind spells
        // todo

        //OutputDebugString(iFormat(_T("%s receives %d of damage: "),gTextMgr[TRID_CREATURE_PEASANT_F2+m_creatType*3],damage).CStr());
        var dead = 0
        if (damage.element >= TotalHitPoints()) {
            m_casualties += m_creatCount
            damage.element = TotalHitPoints()
            //OutputDebugString(iFormat(_T("%d units dead (Group destroyed)\n"),m_creatCount).CStr());
            dead = m_creatCount
            m_creatCount = 0
            m_hitPoints = 0
            SetState(STATE.Dead)
        } else {
            m_hitPoints -= damage.element
            while (m_hitPoints <= 0) {
                ++dead
                --m_creatCount
                m_hitPoints += HitPoints()
            }
            //OutputDebugString(iFormat(_T("%d units dead\n"),dead).CStr());
            check(m_creatCount > 0)
            m_casualties += dead
        }

        if (dead != 0) {
            m_pWrapper.AddLogEvent(
                "${GetUnitsColor(m_pOwner.Owner())}#S0--- " + gTextMgr[TextResId.TRID_MSG_BAT_PERISH].format(
                    dead,
                    gTextMgr[TextResId.TRID_CREATURE_PEASANT_F3.v + m_creatType.v * 3]
                )
            )
            cellMsg += " (-%d)".format(dead)
        }

        m_pWrapper.AddCellEvent(cellMsg, m_Pos)

        return dead
    }

    // todo
    fun SetState(nState: STATE) {
        //_LOG(iFormat(_T("%s changes state to '%s'\n"),gTextMgr[TRID_CREATURE_PEASANT_F2+m_creatType*3], STATE_NAMES[nState]).CStr());  // commented in sources
        m_State = nState
    }

    fun Place(pos: IPointInt) {
        // todo

        m_Pos.setTo(pos)
    }

    fun Rotate() {
        //_LOG(iFormat(_T("%s rotates\n"),gTextMgr[TRID_CREATURE_PEASANT_F2+m_creatType*3]).CStr());
        if (m_Size == 2) {
            m_Pos.x += TAIL_OFFSET[m_Orient.ordinal]
        }
        m_Orient = getByValue(1 - m_Orient.ordinal)
    }
    // todo

    //
    fun Synchronize(army: Army) = Unit  // todo

    // todo
    fun UpdatePassMap(/* todo */) {
        // reset
        // todo
        m_shotList.clear()
        // todo
    }

    // todo

    fun AddShootEntry(pos: IPointInt, penalty: Int) {
        m_shotList.add(iBattleView.iShootEntry(pos, penalty))
    }

    // todo

    fun GetShootEntry(pos: IPointInt): iBattleView.iShootEntry? = m_shotList.firstOrNull { it.m_pos == pos }

    // todo

    fun CanMove(x: Int, y: Int): Boolean = true  // todo

    fun Casualties(): Int = m_casualties
    fun BattleWrapper(): iBattleWrapper = m_pWrapper

    fun FurtSkills(): FurtherSkills = m_furtSkills
}

sealed class iBattleUnit {

    enum class UnitType {

        Hero,
        CreatGroup,
        Catapult,
        Turret,
        Moat,
    }

    protected val m_pOwner: iBattleMember
    protected val m_ut: UnitType  // todo: is it needed?

    constructor(ut: UnitType, pOwner: iBattleMember) {
        m_ut = ut
        m_pOwner = pOwner
    }

    fun GetUnitType(): UnitType = m_ut
    fun Owner(): iBattleMember = m_pOwner

    abstract fun IsAlive(): Boolean
    abstract fun CanWait(): Boolean
    abstract fun Speed(): Int
}

class iBattleUnit_Hero : iBattleUnit {

    constructor(pOwner: iBattleMember) : super(UnitType.Hero, pOwner) {  // todo: fixed bug
        if (pOwner !is iBattleMember_Hero) {
//            val pCastle = pOwner as iBattleMember_Castle  // todo
//            check(pCastle.GetVisitor())  // todo
        }
    }

    fun GetHero(): iHero? {
        return when (m_pOwner) {
            is iBattleMember_Hero -> m_pOwner.GetHero()
//            is iBattleMember_Castle -> TODO()
            else -> {
                check(false)
                null
            }
        }
    }

    override fun IsAlive(): Boolean = true
    override fun CanWait(): Boolean = false
    override fun Speed(): Int = 0xFF
}

class iBattleUnit_CreatGroup : iBattleUnit {

    protected val m_pCreatGroup: iBattleGroup

    constructor(pCreatGroup: iBattleGroup) : super(UnitType.CreatGroup, pCreatGroup.Owner()) {
        m_pCreatGroup = pCreatGroup
    }

    fun GetCreatGroup(): iBattleGroup = m_pCreatGroup

    override fun IsAlive(): Boolean = m_pCreatGroup.IsAlive()
    override fun CanWait(): Boolean = m_pCreatGroup.CanWait()
    override fun Speed(): Int = m_pCreatGroup.Speed()
}

class iBattleUnit_Catapult  // todo

class iBattleUnit_Turret  // todo

class iBattleUnit_Moat  // todo

fun GetUnitsColor(pid: PlayerId): String = when (pid) {
    PlayerId.NEUTRAL -> "#FAAA"
    else -> pid.textColor
}

// Calculates delta distance between two cells
fun BattleCellsDelta(px1: Int, py1: Int, px2: Int, py2: Int): SizeT {
    val nx1 = px1 - py1 / 2
    val ny1 = px1 + (py1 + 1) / 2
    val nx2 = px2 - py2 / 2
    val ny2 = px2 + (py2 + 1) / 2

    val dx = nx2 - nx1
    val dy = ny2 - ny1

    if (dx.sign == dy.sign) {
        return maxOf(dx.absoluteValue, dy.absoluteValue)
    }
    return dx.absoluteValue + dy.absoluteValue
}

fun BattleCellsOrient(fx: Int, fy: Int, tx: Int, ty: Int, orient: Int): iBattleGroup.ORIENT {
    var orient = orient
    var xdiff = when (orient) {
        0 -> tx - fx
        else -> fx - tx
    }
    if ((fy and 1) == 0 && (ty and 1) != 0 && orient == 0 || (fy and 1) == 1 && (ty and 1) == 0 && orient != 0) {
        xdiff -= 1
    }
    if (xdiff < 0) {
        orient = 1 - orient
    }
    return iBattleGroup.ORIENT.values()[orient]
}

fun BattleDirOrient(dir: Int): iBattleGroup.ORIENT {
    return when (dir) {
        in 0 until 4 -> iBattleGroup.ORIENT.Right
        else -> iBattleGroup.ORIENT.Left
    }
}
