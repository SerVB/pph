package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.army.Army
import com.github.servb.pph.pheroes.common.army.ArmyC
import com.github.servb.pph.pheroes.common.army.CreatGroupC
import com.github.servb.pph.pheroes.common.common.MineralType
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.creature.CreatureType
import com.github.servb.pph.pheroes.common.creature.Perk
import com.github.servb.pph.pheroes.common.iSortArray
import com.github.servb.pph.pheroes.game.iTurnSeq.UpdateSequenceFlags.ACTIVES
import com.github.servb.pph.pheroes.game.iTurnSeq.UpdateSequenceFlags.DEADS
import com.github.servb.pph.util.Mutable
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.or
import com.soywiz.klogger.Logger
import com.soywiz.korio.lang.format
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.PointInt

/* Combat rules:
How the damage is counted?
DAM= (1+((A-D)*X))) * A_count * RND_damage
In words: Number of attackers times damage caused by a single unit [this
one is the only random factor - damage lies somewhere within damage
range] times modifier based on Attack skill of Attacker and Defense skill
of defender.
Value of X: X equals 0.1 if A is bigger than D, which means that the
damage is increased by 10% for every point between A and D X equals 0.05
if D is bigger than A, so damage is decreased by 5% for every point
between D and A.
We'll show a black dragon fighting different opponents. Damage modifier
counted below will in combat be multiplied by number of dragons and
random value between 25 and 50, which is black dragon's damage range.
So, black dragon attacking peasants: [Assume heroes with A and D=0]
Modifier = (1+(14-1)*0.1) = 2.30 or 230%, which gives damage from 68 to
115.
Now, the same dragon, attacking other black dragon:
Modifier = (1+(14-14)*0.1) = 1.00 or 100% which gives, of course, damage
from 25 to 50
And now, the same dragon attacking other black dragon, whose commanding
hero has the ultimate cloak of Protection, having defense bonus +12:
Modifier = (1+(14-26)*0.05) = ((1-12)*0.05) = 0.4 or 40%
There are limits to those numbers. Attack modifier is limited to 300%,
defense modifier is limited to 20%. That means that there's no need to
cast bloodlust on your army if it's attack is higher than it's target
defense + 20, or steelskin, if your unit's defense is higher than
attacking unit's attack + 16. Is that clear?
 */

private val SIDE_TEXT = listOf("->", "<-")

//private fun DumpBattleUnit(pUnit: iBattleUnit) {
//    todo
//}

private val armyA1 = listOf(5)
private val armyA2 = listOf(3, 7)
private val armyA3 = listOf(2, 5, 8)
private val armyA4 = listOf(0, 4, 6, 10)
private val armyA5 = listOf(0, 2, 5, 8, 10)
private val armyA6 = listOf(0, 2, 4, 6, 8, 10)
private val armyA7 = listOf(0, 2, 4, 5, 6, 8, 10)
private val armyArrangement = listOf(armyA1, armyA2, armyA3, armyA4, armyA5, armyA6, armyA7)

private val ANI_SPEED_COEF = listOf(2.0, 1.5, 1.0, 0.75, 0.5)

enum class MELEE_DIR(override val v: Int) : CountValueEnum, UniqueValueEnum {
    NORTHWEST(0),
    NORTH(1),
    NORTHEAST(2),
    RSPTOP(3),
    EAST(4),
    RSPBOTTOM(5),
    SOUTHEAST(6),
    SOUTH(7),
    SOUTHWEST(8),
    LSPBOTTOM(9),
    WEST(10),
    LSPTOP(11),
    COUNT(12),
}

enum class CELL_ACT_MASK(override val v: Int) : UniqueValueEnum {
    NONE(0x00),  // no action
    MOVE(0x01),  // can move
    ATTACK(0x02),  // can attack
    SHOOT(0x04),  // can shoot
    FSPELL(0x08),  // can cast friendly spell
    ESPELL(0x10),  // can cast enemy spell
    INFO(0x20),  // creature information
}

/*
 * Action list
 */
abstract class iBattleAct {

    enum class Type(override val v: Int) : UniqueValueEnum {
        Move(0),  // Move to specified cell (async)
        Rotate(1),  // Change group's orientation (async)
        Attack(2),  // Melee attack specified cell
        Shoot(3),  // Shoot to specified cell
        Delay(4),  // Delay
        GoodMorale(5),  // Good morale
        BadMorale(6),  // Bad morale
        GoodLuck(7),  // Good luck
        BadLuck(8),  // Bad luck
        CastSpell(9),  // Casting spell to specified cell
        Catapult(10),  // Catapult shot
        Turret(11),  // Turret shot
        Gate(12),  // Open/Close gate
    }

    val m_delay: Double

    constructor(delay: Double) {
        m_delay = delay * ANI_SPEED_COEF[gSettings.GetEntryValue(ConfigEntryType.COMBATSPEED)]
    }

    abstract fun IsValid(): Boolean
    open fun Normalize(): Boolean = true
    abstract fun BeginAct()
    abstract fun EndAct()
}

class iBattleAct_Move : iBattleAct {

    val m_bridgeFlag: UByte
    val m_pActor: iBattleGroup
    val m_target: IPointInt

    constructor(pActor: iBattleGroup, target: IPointInt) : super(0.3) {
        m_pActor = pActor
        m_target = target
        m_bridgeFlag = 0u
    }

    override fun IsValid(): Boolean = m_pActor.IsAlive()

    override fun BeginAct() {
        m_pActor.SetState(iBattleGroup.STATE.Moving)
    }

    override fun EndAct() {
        m_pActor.Place(m_target)
        m_pActor.SetState(iBattleGroup.STATE.Idle)
    }
}

class iBattleAct_Rotate : iBattleAct {

    val m_pActor: iBattleGroup

    constructor(pActor: iBattleGroup) : super(0.3) {
        m_pActor = pActor
    }

    override fun IsValid(): Boolean = m_pActor.IsAlive()

    override fun BeginAct() {
        m_pActor.SetState(iBattleGroup.STATE.Rotating)
    }

    override fun EndAct() {
        m_pActor.Rotate()
        m_pActor.SetState(iBattleGroup.STATE.Idle)
    }
}

class iBattleAct_Attack : iBattleAct {

    val m_luck: iBattleGroup.MORLUCK_MOD
    val m_bRetail: Boolean
    val m_penalty: Int
    val m_pActor: iBattleGroup
    val m_cell: IPointInt
    val m_targetList: MutableList<iBattleGroup> = mutableListOf()
    val m_joustBonus: SizeT

    constructor(
        pActor: iBattleGroup,
        cell: IPointInt,
        pTarget: iBattleGroup,
        penalty: Int,
        joustBonus: SizeT,
        bRetail: Boolean,
        luck: iBattleGroup.MORLUCK_MOD
    ) : super(0.6) {
        m_pActor = pActor
        m_cell = cell
        m_penalty = penalty
        m_joustBonus = joustBonus
        m_bRetail = bRetail
        m_luck = luck
        m_targetList.add(pTarget)
    }

    override fun Normalize(): Boolean {
        m_targetList.retainAll { it.IsAlive() }
        return m_targetList.isNotEmpty()
    }

    override fun IsValid(): Boolean = m_pActor.IsAlive()

    override fun BeginAct() {
        m_pActor.SetState(iBattleGroup.STATE.Melee)
        m_targetList.forEach { it.SetState(iBattleGroup.STATE.RecDamage) }
    }

    override fun EndAct() {
        m_targetList.forEach {
            m_pActor.Attack(it, 0, m_penalty, m_joustBonus, m_luck)
            if (it.IsAlive()) {
                it.SetState(iBattleGroup.STATE.Idle)
            }
        }
        m_pActor.SetState(iBattleGroup.STATE.Idle)
        if (m_bRetail) {
            m_pActor.SetRetailed()
        }
    }
}

class iBattleAct_Shoot : iBattleAct {

    val m_luck: iBattleGroup.MORLUCK_MOD
    val m_penalty: Int
    val m_pActor: iBattleGroup
    val m_cell: IPointInt
    val m_targetList: List<iBattleGroup>

    constructor(
        pActor: iBattleGroup,
        cell: IPointInt,
        pTarget: iBattleGroup,
        penalty: Int,
        luck: iBattleGroup.MORLUCK_MOD
    ) : super(0.5) {
        m_pActor = pActor
        m_cell = cell
        m_penalty = penalty
        m_luck = luck
        m_targetList = listOf(pTarget)
    }

    override fun Normalize(): Boolean {
        // todo
        return m_targetList.isNotEmpty()
    }

    override fun IsValid(): Boolean = m_pActor.IsAlive()

    override fun BeginAct() {
        m_pActor.SetState(iBattleGroup.STATE.Shooting)
        m_targetList.forEach { it.SetState(iBattleGroup.STATE.RecDamage) }
    }

    override fun EndAct() {
        var aflag = iBattleGroup.AttackFlags.RangeAttack.v
        m_targetList.forEachIndexed { xx, it ->
            m_pActor.Attack(it, aflag, m_penalty, 0, m_luck)
            if (it.IsAlive()) {
                it.SetState(iBattleGroup.STATE.Idle)
            }
            if (xx == 0 && m_pActor.HasPerk(Perk.LICHSHOOT)) {
                aflag = aflag or Perk.LICHSHOOT
            }
        }
        m_pActor.SetState(iBattleGroup.STATE.Idle)
    }
}

class iBattleAct_Delay : iBattleAct {

    constructor() : super(0.9)

    override fun IsValid(): Boolean = true

    override fun BeginAct() {
        // nop
    }

    override fun EndAct() {
        // nop
    }
}

class iBattleAct_GoodMorale : iBattleAct {

    val m_pActor: iBattleGroup

    constructor(pActor: iBattleGroup) : super(0.9) {
        m_pActor = pActor
    }

    override fun IsValid(): Boolean = m_pActor.IsAlive()

    override fun BeginAct() {
        m_pActor.GoodMorale()
    }

    override fun EndAct() {
        // nop
    }
}

class iBattleAct_BadMorale  // todo

class iBattleAct_GoodLuck : iBattleAct {

    val m_pActor: iBattleGroup

    constructor(pActor: iBattleGroup) : super(0.9) {
        m_pActor = pActor
    }

    override fun IsValid(): Boolean = m_pActor.IsAlive()

    override fun BeginAct() {
        m_pActor.GoodLuck()
    }

    override fun EndAct() {
        // nop
    }
}

class iBattleAct_BadLuck : iBattleAct {

    val m_pActor: iBattleGroup

    constructor(pActor: iBattleGroup) : super(0.9) {
        m_pActor = pActor
    }

    override fun IsValid(): Boolean = m_pActor.IsAlive()

    override fun BeginAct() {
        m_pActor.BadMorale()
    }

    override fun EndAct() {
        // nop
    }
}

class iBattleAct_CastSpell  // todo
class iBattleAct_Catapult  // todo
class iBattleAct_Turret  // todo
class iBattleAct_Gate  // todo
class iBattleAct_MoatDmg  // todo

typealias iActList = ArrayDeque<iBattleAct>

class iBattleActList {

    private val m_ActList: iActList = ArrayDeque()

    fun EndDir(pActor: iBattleGroup, dir: UByte): UByte {
        var ans = dir
        m_ActList.forEach {
            if (it is iBattleAct_Rotate && it.m_pActor == pActor) {
                ans = when (ans) {
                    0u.toUByte() -> 1u
                    else -> 0u
                }
            }
        }
        return ans
    }

    fun EndDirPos(pActor: iBattleGroup, pos: PointInt, dir: Mutable<UByte>) {
        m_ActList.forEach {
            when {
                it is iBattleAct_Rotate && it.m_pActor == pActor -> {
                    pos.x += TAIL_OFFSET[dir.element.toInt()]
                    dir.element = when (dir.element) {
                        0u.toUByte() -> 1u
                        else -> 0u
                    }
                }
                it is iBattleAct_Move && it.m_pActor == pActor -> {
                    pos.setTo(it.m_target)
                }
            }
        }
    }

    fun Reset(): Unit = m_ActList.clear()
    fun Count(): SizeT = m_ActList.size
    fun AddAction(pAct: iBattleAct): Unit = m_ActList.addLast(pAct)
    fun PushAction(pAct: iBattleAct): Unit = m_ActList.addFirst(pAct)
    fun InsertActionBefore(it: Int, pAct: iBattleAct): Unit = m_ActList.add(it, pAct)
    fun InsertActionAfter(it: Int, pAct: iBattleAct): Unit = m_ActList.add(it + 1, pAct)
    fun StepAction(): iBattleAct? = m_ActList.removeFirstOrNull()
    fun First(): iBattleAct? = m_ActList.firstOrNull()
}

class iBattleArmy {

    private lateinit var m_pOwner: iBattleMember
    private val m_pWrapper: iBattleWrapper
    private val m_groups: MutableList<iBattleGroup> = mutableListOf()

    constructor(pWrapper: iBattleWrapper) {
        m_pWrapper = pWrapper
    }

    fun AliveGroups(): SizeT = m_groups.count { it.IsAlive() }
    fun Count(): SizeT = m_groups.size
    fun Groups(): List<iBattleGroup> = m_groups
    operator fun get(idx: SizeT): iBattleGroup = m_groups[idx]
    fun FindGroupByIdx(idx: SizeT): iBattleGroup? = m_groups.firstOrNull { it.ArmyGroupIndex() == idx }

    fun Init(army: ArmyC, pOwner: iBattleMember) {
        m_pOwner = pOwner
        val moraleModifier = army.moraleModifier
        repeat(7) {
            if (army[it].type != CreatureType.UNKNOWN) {
                m_groups.add(iBattleGroup(army[it], it, pOwner, m_pWrapper, moraleModifier))
            }
        }
    }

    fun Summon(cg: CreatGroupC, pos: IPointInt): iBattleGroup {
        val pGroup = iBattleGroup(cg, -1, m_pOwner, m_pWrapper, 0)
        pGroup.Place(pos)
        m_groups.add(pGroup)
        return pGroup
    }

    fun Synchronize(army: Army) = m_groups.forEach { it.Synchronize(army) }

    fun Casualties(): Int = m_groups.sumOf { it.Casualties() }

    // Army cost (according with diplomacy level)
    fun Cost(): Int = m_groups.sumOf {
        when (it.IsAlive()) {
            true -> it.Count() * it.Type().descriptor!!.cost.quant[MineralType.GOLD.v]
            false -> 0
        }
    }

    fun ArmyPower(): SizeT = m_groups.sumOf {
        when (it.IsAlive()) {
            true -> it.Count() * it.Type().descriptor!!.pidx
            false -> 0
        }
    }

    fun Owner(): iBattleMember = m_pOwner
}

private fun <T> select(id: Int, first: T, second: T): T = when (id) {
    0 -> first
    else -> second
}

class iTurnSeq {

    private val m_pWrapper: iBattleWrapper
    private var m_pCurUnit: iBattleUnit?  // Current/Active unit

    //    private val m_pSpellCaster: iBattleUnit_Hero  // todo
    private val m_turnSeq: ArrayDeque<iBattleUnit> = ArrayDeque()  // Prepared turn sequence
    private val m_dead: MutableList<iBattleGroup> =
        mutableListOf() // Dead not moved units (can be resurrected to active or waiting list)
    private val m_active0: iSortArray<iBattleGroup> = iSortArray()  // Active units (not moved)
    private val m_active1: iSortArray<iBattleGroup> = iSortArray()
    private val m_waiting0: iSortArray<iBattleGroup> = iSortArray()  // Waiting units
    private val m_waiting1: iSortArray<iBattleGroup> = iSortArray()
    private var m_LastSide: iBattleMember.Side  // Last acting side
    private val m_bHasCatapult: Boolean  // Need catapult shoot
//    private val m_turrets: iTowerList  // todo

    object UpdateSequenceFlags {

        const val ACTIVES = 0x1
        const val DEADS = 0x2
    }

    constructor(pWrapper: iBattleWrapper) {
        m_LastSide =
            iBattleMember.Side.Defender  // setup last side to defender (to provide high priority for assaulter in turn sequence)
        m_pCurUnit = null
//        m_pSpellCaster = null  // todo
        m_pWrapper = pWrapper
        m_bHasCatapult = false
    }

    fun NewRoundSequence(aa: iBattleArmy, da: iBattleArmy/*, pFort: iCastleFort*/) {
        check(m_turnSeq.isEmpty())
        check(m_pCurUnit == null)
        check(m_active0.Size() == 0)
        check(m_active1.Size() == 0)

        //check(!m_dead.GetSize());  // commented in sources
        m_dead.clear()

        // Add assaulter army creatures
        for (xx in 0 until aa.Count()) {
            val item = aa[xx]
            if (item.IsAlive() && item.CanAct()) {
                m_active0.Insert(item, item.Speed() * 20 - item.Pos().y)
            } else {
                m_dead.add(item)
            }
        }

        // Add defender army creatures
        for (xx in 0 until da.Count()) {
            val item = da[xx]
            if (item.IsAlive() && item.CanAct()) {
                m_active1.Insert(item, item.Speed() * 20 - item.Pos().y)
            } else {
                m_dead.add(item)
            }
        }

        // Add turrets and catapult
        // todo

        UpdateSequence(0)
        StepSequence()
    }

    fun CurUnit(): iBattleUnit? {
        // todo
        return m_pCurUnit
    }

    fun NextGroup(): iBattleUnit? {
        checkNotNull(m_pCurUnit)

        // Last unit is hero (spell casted)
        // todo

        // Check for good morale case
        val pCurCreatGroup = m_pCurUnit as? iBattleUnit_CreatGroup
        if (pCurCreatGroup != null) {
            val pCurGroup = pCurCreatGroup.GetCreatGroup()
            if (pCurGroup.IsAlive() && pCurGroup.RoundMorale() == iBattleGroup.MORLUCK_MOD.POSITIVE) {
                // Good morale (reset round morale and return current unit)
                pCurGroup.ResetRoundMorale()
                return m_pCurUnit
            }
        }

        // Sequence is empty, so, reset current unit and return NULL
        if (m_turnSeq.isEmpty()) {
            m_pCurUnit = null
            return m_pCurUnit
        }

        StepSequence()
        return m_pCurUnit
    }

    fun Wait(): iBattleUnit? {
        val pCurCreatGroup = m_pCurUnit as iBattleUnit_CreatGroup

        val pGroup = pCurCreatGroup.GetCreatGroup()
        when (pGroup.Owner().GetSide()) {
            iBattleMember.Side.Assaulter -> m_waiting0
            iBattleMember.Side.Defender -> m_waiting1
        }.Insert(pGroup, -(pGroup.Speed() * 20 - pGroup.Pos().y))
        UpdateSequence(0)
        StepSequence()
        return m_pCurUnit
    }

    fun Defend(): iBattleUnit? {
        check(m_pCurUnit is iBattleUnit_CreatGroup)
        UpdateSequence(0)
        StepSequence()
        return m_pCurUnit
    }

//    fun SetSpellCaster(pSpellCaster: iBattleUnit_Hero)  // todo

//    fun GetSpellCaster(): iBattleUnit_Hero = m_pSpellCaster  // todo

    fun UpdateSequence(flags: SizeT) {
        // Cleanup current sequence
        Cleanup()

        // update active creatures
        if ((flags and ACTIVES) != 0) {
            fun update(active: iSortArray<iBattleGroup>, waiting: iSortArray<iBattleGroup>) {
                // Sort active units
                val group = mutableListOf<iBattleGroup>()
                while (active.Size() != 0) {
                    group.add(active.Pop())
                }
                while (group.isNotEmpty()) {
                    val pGroup = group.removeLast()
                    if (pGroup.IsAlive() && pGroup.CanAct()) {
                        //OutputDebugString(iFormat(_T("Add active %s to normals\n"), gTextMgr[TRID_CREATURE_PEASANT_F2+pGroup->Type()*3]).CStr());  // commented in sources
                        active.Insert(pGroup, pGroup.Speed() * 20 - pGroup.Pos().y)
                    } else {
                        //OutputDebugString(iFormat(_T("Add active %s to deads\n"), gTextMgr[TRID_CREATURE_PEASANT_F2+pGroup->Type()*3]).CStr());  // commented in sources
                        m_dead.add(pGroup)
                    }
                }
                // Sort waiting units
                while (waiting.Size() != 0) {
                    group.add(waiting.Pop())
                }
                while (group.isNotEmpty()) {
                    val pGroup = group.removeLast()
                    if (pGroup.IsAlive() && pGroup.CanAct()) {
                        //OutputDebugString(iFormat(_T("Add waiting %s to normals\n"), gTextMgr[TRID_CREATURE_PEASANT_F2+pGroup->Type()*3]).CStr());  // commented in sources
                        waiting.Insert(pGroup, -(pGroup.Speed() * 20 - pGroup.Pos().y))
                    } else {
                        //OutputDebugString(iFormat(_T("Add waiting %s to deads\n"), gTextMgr[TRID_CREATURE_PEASANT_F2+pGroup->Type()*3]).CStr());  // commented in sources
                        m_dead.add(pGroup)
                    }
                }
            }

            update(active = m_active0, waiting = m_waiting0)
            update(active = m_active1, waiting = m_waiting1)
        }

        // parse dead creatures
        if ((flags and DEADS) != 0) {
            m_dead.removeAll { pGroup ->
                if (pGroup.IsAlive() && pGroup.CanAct()) {
                    logger.info { "%s Restored from dead/blind state".format(gTextMgr[TextResId.TRID_CREATURE_PEASANT_F2.v + pGroup.Type().v * 3]) }

                    if (pGroup.CanWait()) {
                        pGroup.actives().Insert(pGroup, pGroup.Speed() * 20 - pGroup.Pos().y)
                    } else {
                        pGroup.waiters().Insert(pGroup, -(pGroup.Speed() * 20 - pGroup.Pos().y))
                    }

                    true
                } else {
                    false
                }
            }
        }

        // Add active units to current sequence
        val bNeedCatapult = m_bHasCatapult
        var pr = 1 - m_LastSide.ordinal
        val pos = arrayOf(m_active0.Size() - 1, m_active1.Size() - 1)
        while (pos[0] >= 0 || pos[1] >= 0) {
            if (pos[0] >= 0 && pos[1] < 0) {
                m_turnSeq.add(iBattleUnit_CreatGroup(m_active0.Value(pos[0]--)))
                pr = 1
            } else if (pos[0] < 0 && pos[1] >= 0) {
                m_turnSeq.add(iBattleUnit_CreatGroup(m_active1.Value(pos[1]--)))
                pr = 0
            } else if (select(pr, m_active0, m_active1).Value(pos[pr]).Speed() < select(
                    1 - pr,
                    m_active0,
                    m_active1
                ).Value(pos[1 - pr]).Speed()
            ) {
                m_turnSeq.add(iBattleUnit_CreatGroup(select(1 - pr, m_active0, m_active1).Value(pos[1 - pr]--)))
            } else {
                m_turnSeq.add(iBattleUnit_CreatGroup(select(pr, m_active0, m_active1).Value(pos[pr]--)))
                pr = 1 - pr
            }
        }

        // Add waiting units to current sequence
        // todo

        // Add turrets and catapult
        // todo

        // debug logging (commented in sources)
    }

    private fun iBattleGroup.actives(): iSortArray<iBattleGroup> = when (this@actives.Owner().GetSide()) {
        iBattleMember.Side.Assaulter -> m_active0
        iBattleMember.Side.Defender -> m_active1
    }

    private fun iBattleGroup.waiters(): iSortArray<iBattleGroup> = when (this@waiters.Owner().GetSide()) {
        iBattleMember.Side.Assaulter -> m_waiting0
        iBattleMember.Side.Defender -> m_waiting1
    }

    fun AddNewGroup(pNewGroup: iBattleGroup) {
        pNewGroup.actives().Insert(pNewGroup, (pNewGroup.Speed() * 20 - pNewGroup.Pos().y))
        UpdateSequence(0)
    }

    fun GetTurnSeqSize(): SizeT = m_turnSeq.size
    fun GetTurnSeqItem(idx: SizeT): iBattleUnit = m_turnSeq[idx]

    private fun StepSequence(): Boolean {
        logger.info { "TurnSequence::StepSequence {" }
        if (m_pCurUnit != null) {
            m_pCurUnit = null
        }
        if (m_turnSeq.isEmpty()) {
            logger.info { "} TurnSequence::StepSequence (false)" }
            return false
        }

        val pCurUnit = m_turnSeq.removeFirst()
        m_pCurUnit = pCurUnit
        m_LastSide = pCurUnit.Owner().GetSide()

        logger.info { "Select new unit:" }
//        DumpBattleUnit(m_pCurUnit)  // todo

        when (pCurUnit) {
            is iBattleUnit_CreatGroup -> {
                val pGroup = pCurUnit.GetCreatGroup()
                if (pGroup.CanWait()) {
                    val pActGroup = when (pGroup.Owner().GetSide()) {
                        iBattleMember.Side.Assaulter -> m_active0
                        iBattleMember.Side.Defender -> m_active1
                    }.Pop()
                    check(pGroup == pActGroup)
                } else {
                    val pWaitGroup = when (pGroup.Owner().GetSide()) {
                        iBattleMember.Side.Assaulter -> m_waiting0
                        iBattleMember.Side.Defender -> m_waiting1
                    }.Pop()
                    check(pGroup == pWaitGroup)
                }
            }
//            is iBattleUnit_Catapult  // todo
//            is iBattleUnit_Turret  // todo
            else -> check(false)
        }

        logger.info { "} TurnSequence::StepSequence (true)" }
        return true
    }

    private fun Cleanup() {
        m_turnSeq.clear()
    }

    private companion object {

        private val logger = Logger<iTurnSeq>()

        private val comparator =
            Comparator<Pair<iBattleGroup, Int>> { a, b -> a.second.compareTo(b.second) }
    }
}

class iBattleEngine {

    private val m_bFakeMode: Boolean
    private var m_bAutoBattle: Boolean

    //    private val m_pCastleFort: iCastleFort  // todo
    private var m_bInited: Boolean
    private lateinit var m_bi: iBattleInfo

    private var m_iRound: SizeT
    private val m_turnSeq: iTurnSeq
    private val m_ActList: iBattleActList = iBattleActList()
    private var m_pCurAct: iBattleAct?

    private val m_aArmy: iBattleArmy
    private val m_dArmy: iBattleArmy

    //    private val m_obsMap: iBattleMap  // todo
    private val m_pWrapper: iBattleWrapper

    constructor(pWrapper: iBattleWrapper, bFakeMode: Boolean) {
        m_pWrapper = pWrapper
        m_bFakeMode = bFakeMode
        m_aArmy = iBattleArmy(pWrapper)
        m_dArmy = iBattleArmy(pWrapper)
        m_turnSeq = iTurnSeq(pWrapper)
        m_bInited = false
        m_iRound = 0
//        m_obsMap = 13,11  // todo
//        m_pCastleFort = null  // todo
        m_pCurAct = null
        m_bAutoBattle = false
//        m_obsMap.FillMem()  // todo
    }

    fun BeginBattle(bi: iBattleInfo) {
        m_bi = bi

        val aArmy = m_bi.m_pAssaulter.army()
        val dArmy = m_bi.m_pDefender.army()

        m_aArmy.Init(aArmy, m_bi.m_pAssaulter)
        m_dArmy.Init(dArmy, m_bi.m_pDefender)

        // Init castle's fortification
        // todo

        // Place creatures to battlefield
        fun placeCreatures(army: iBattleArmy, a: Int, b: Int) {
            repeat(army.Count()) { xx ->
//                army[xx].InitPassMap(m_obsMap, m_pCastleFort)  // todo
                val p = IPointInt(if (army[xx].Size() == 2) a else b, armyArrangement[army.Count() - 1][xx])
                army[xx].Place(p)
            }
        }

        placeCreatures(m_aArmy, 1, 0)
        placeCreatures(m_dArmy, 11, 12)

        m_bInited = true
    }

    fun Start() {
        NextRound()
        PrepareDistMap()
    }

    fun EndBattle(br: iBattleResult) {
        m_aArmy.Synchronize(m_bi.m_pAssaulter.army())
        m_dArmy.Synchronize(m_bi.m_pDefender.army())

        br.m_pWinner!!.OnWin(br)
        br.m_pLoser!!.OnLose(br)

        check(!br.m_pWinner!!.army().empty) { "at least one group should be in winner's army" }

//        if (m_pCastleFort != null)  // todo

        m_bInited = false
    }

    fun NextGroup() {
        if (m_turnSeq.NextGroup() == null) {
            NextRound()
        }
        PrepareDistMap()
    }

    fun IsFakeMode(): Boolean = m_bFakeMode

//    fun SelectSpellTargets()  // todo
//    fun GetValidSpellTargers()  // todo
//    fun GetSpellCovers()  // todo

    fun GetSurrenderCost(pActor: iBattleMember_Hero): SizeT {
        TODO()
    }

    fun CheckBattleState(): BATTLE_RESULT {
        return when {
            m_dArmy.AliveGroups() == 0 -> BATTLE_RESULT.ASSAULTER_WIN
            m_aArmy.AliveGroups() == 0 -> BATTLE_RESULT.DEFENDER_WIN
            else -> BATTLE_RESULT.NA
        }
        // commented in sources:
/*	uint32 bState = m_dArmy.AliveGroups() | (m_aArmy.AliveGroups() << 1);
// Is anybody alive on battlefield ?
if (!bState) {
    //if ()
}

if ( (bState & 1) == 0) return BR_ASSAULTER_WIN;
else if ( (bState & 2) == 0 ) return BR_DEFENDER_WIN;
*/
    }

    fun PrepareBattleResult(br: iBattleResult) {
        if (br.m_result == BATTLE_RESULT.ASSAULTER_WIN) {
            br.m_pWinner = m_bi.m_pAssaulter
            br.m_pLoser = m_bi.m_pDefender
            br.m_experience = m_bi.m_pAssaulter.GetExperience()
            br.m_winCas = m_aArmy.Casualties()
            br.m_losCas = m_dArmy.Casualties()
        } else if (br.m_result == BATTLE_RESULT.DEFENDER_WIN) {
            br.m_pWinner = m_bi.m_pDefender
            br.m_pLoser = m_bi.m_pAssaulter
            br.m_experience = m_bi.m_pDefender.GetExperience()
            br.m_winCas = m_dArmy.Casualties()
            br.m_losCas = m_aArmy.Casualties()
        } else {
            check(false)
        }

        if (br.m_defCause == DEFEAT_CAUSE.DEFEAT) {
            val pHeroMember = br.m_pLoser as? iBattleMember_Hero
            if (pHeroMember != null) {
                br.m_experience += pHeroMember.GetHero().Level() * 500
            }
        } else if (br.m_defCause == DEFEAT_CAUSE.RETREAT) {
            val pHeroMember = br.m_pLoser as? iBattleMember_Hero
            if (pHeroMember != null) {
                br.m_experience += pHeroMember.GetHero().Level() * 250
            }
        }

        // todo: exp for castle

        val pHeroMember = br.m_pWinner as? iBattleMember_Hero
        if (pHeroMember != null) {
            br.m_experience = pHeroMember.GetHero().ConvExpPts(br.m_experience)
        }
    }

    fun FindAllGroups(pos: IPointInt, owner: iBattleMember? = null, bIncDead: Boolean = false): List<iBattleGroup> {
        TODO()
    }

    fun FindGroup(pos: IPointInt, bIncDead: Boolean = false): iBattleGroup? {
        if (bIncDead) {
            // For resurrect spells (try to find alive unit, then dead)
            TODO()
        } else {
            // General case (return only alive unit)
            repeat(m_aArmy.Count()) { xx ->
                if (m_aArmy[xx].IsGroupCell(pos) && m_aArmy[xx].IsAlive()) {
                    return m_aArmy[xx]
                }
            }
            repeat(m_dArmy.Count()) { xx ->
                if (m_dArmy[xx].IsGroupCell(pos) && m_dArmy[xx].IsAlive()) {
                    return m_dArmy[xx]
                }
            }
            return null
        }
    }

    fun StepAction(): iBattleAct? {
        var pRes: iBattleAct? = null
        if (m_pCurAct != null) {
            pRes = m_ActList.StepAction()
            check(pRes == m_pCurAct)
            m_pCurAct!!.EndAct()

            // check for end of battle
            if (CheckBattleState() != BATTLE_RESULT.NA) {
                m_ActList.Reset()
                return m_pCurAct
            }

            // remove dead actors from action sequence and normalize current action
            while (m_ActList.Count() != 0 && (!m_ActList.First()!!.IsValid() || !m_ActList.First()!!.Normalize())) {
                val pAct = m_ActList.StepAction()
                checkNotNull(pAct)
            }
        }

        if (m_ActList.Count() != 0) {
            if (m_pCurAct == null) {
                // _LOG(_T("{ //Start action\r\n")); // todo
            }

            // Process gate open/close
            // todo

            m_pCurAct = m_ActList.First()
            m_pCurAct!!.BeginAct()
        } else {
            checkNotNull(m_pCurAct)
            m_pCurAct = null
            // _LOG(_T("} // End action\r\n"));  // todo
        }

        // remove all dead groups from sequence
        m_turnSeq.UpdateSequence(ACTIVES or DEADS)

        return pRes
    }

    fun CurAction(): iBattleAct? = m_pCurAct

    fun Summon(side: iBattleMember.Side, cg: CreatGroupC, pos: IPointInt): Int {
        val fa = when (side) {
            iBattleMember.Side.Assaulter -> m_aArmy
            iBattleMember.Side.Defender -> m_dArmy
        }
        val pSumGroup = fa.Summon(cg, pos)
        m_turnSeq.AddNewGroup(pSumGroup)
        return 0
    }

//    fun CastSpell(pSpell: iCombatSpell, pos: IPointInt, pSpellTarget: iBattleGroup)  // todo

    fun Move(pos: IPointInt, orient: iBattleGroup.ORIENT) {
        TODO()
    }

    fun Melee(pos: IPointInt, mdir: UShort) {
        TODO()
    }

    fun Shot(pos: IPointInt, penalty: Int) {
        val pCurCreatGroup = m_turnSeq.CurUnit()
        if (pCurCreatGroup is iBattleUnit_CreatGroup) {
            val pCurGroup = pCurCreatGroup.GetCreatGroup()

            // rotate to target
            // todo

            // Find target unit
            // check: todo
            val pTarget = FindGroup(pos)
            checkNotNull(pTarget)

            // shoot
            MakeShootSequence(pCurGroup, pTarget, pos, penalty, m_ActList)

            // [CPERK_DOUBLESHOT] shoot again
            // FIXED: SiGMan (Check we actually have enough shots to DOUBLESHOOT)
            if (pCurGroup.HasPerk(Perk.DOUBLESHOT) && pCurGroup.Shots() > 1) {
                m_ActList.AddAction(iBattleAct_Delay())
                MakeShootSequence(pCurGroup, pTarget, pos, penalty, m_ActList)
            }

            // assaulter rotates to its normal orientation (if required)
            // todo

            // Always delay after range attack
            m_ActList.AddAction(iBattleAct_Delay())

            // In Moat?
            // todo

            // Add good morale action if required
            if (pCurGroup.RoundMorale() == iBattleGroup.MORLUCK_MOD.POSITIVE) {
                m_ActList.AddAction(iBattleAct_GoodMorale(pCurGroup))
            }
        }  // else: todo

        // Start Action
        check(m_ActList.Count() != 0)
        if (m_ActList.Count() != 0) {
            check(m_pCurAct == null) { m_pCurAct.toString() }
            StepAction()
        }
    }

    fun BadMorale() {
        TODO()
    }

    fun CanWait(): Boolean {
        //check(m_turnSeq.GetSize() && m_pCurUnit == m_turnSeq[0]);  // commented in sources
        return m_turnSeq.CurUnit()!!.CanWait()
    }

    fun Wait() {
        TODO()
    }

    fun Defend(): Boolean {
        TODO()
    }

//    fun CatapultShot()  // todo
//    fun TurretShot()  // todo

    fun Inited(): Boolean = m_bInited
    fun GetBattleInfo(): iBattleInfo = m_bi
//    fun ObstaclesMap(): iBattleMap = m_obsMap  // todo

    fun AArmy(): iBattleArmy = m_aArmy
    fun DArmy(): iBattleArmy = m_dArmy
    fun ActionCount(): SizeT = m_ActList.Count()
    fun TurnSeq(): iTurnSeq = m_turnSeq

//    fun CastleFort()  // todo

    fun CalcShotPenalty(pRanger: iBattleGroup, ptTarget: IPointInt): Int {
        val obstR = 3
        val maxRange = 7

        var result = 1
        check(pRanger.CanShoot())
        check(pRanger.Shots() > 0)

        // Check for "No range penalty" flag
        // todo

        // Check distance
        if (BattleCellsDelta(pRanger.Pos().x, pRanger.Pos().y, ptTarget.x, ptTarget.y) > maxRange) {
            result *= 2
        }

        // Check fortifications
        // todo

        // 1 - no penalty, 2 - 2x penalty, 4 - 4x penalty
        return result
    }

    fun SetAutoBattle(bAutoBattle: Boolean) {
        m_bAutoBattle = bAutoBattle
    }

    fun IsAutoBattle(): Boolean = m_bAutoBattle

//    fun MakePassMap()  // todo

//    private fun ProcessMoat(pCurGroup: iBattleGroup)  // todo

//    private fun ProcessGate(pCurGroup: iBattleGroup)  // todo

    private fun NextRound() {
        ++m_iRound

        repeat(m_aArmy.Count()) {
            m_aArmy[it].NewRound()
        }
        repeat(m_dArmy.Count()) {
            m_dArmy[it].NewRound()
        }
        m_turnSeq.NewRoundSequence(m_aArmy, m_dArmy)  // todo
        m_pWrapper.AddLogEvent("#S1#FFFF${gTextMgr[TextResId.TRID_MSG_BAT_NEXTROUND].format(m_iRound)}")
        m_bi.m_pAssaulter.SetCastFlag(false)
        m_bi.m_pDefender.SetCastFlag(false)
    }

    private fun PreparePassMap() {
        for (xx in 0 until m_aArmy.Count()) {
            if (m_aArmy[xx].IsAlive()) {
                m_aArmy[xx].UpdatePassMap(/* todo */)
            }
        }
        for (xx in 0 until m_dArmy.Count()) {
            if (m_dArmy[xx].IsAlive()) {
                m_dArmy[xx].UpdatePassMap(/* todo */)
            }
        }
    }

    private fun PrepareDistMap() {
        val pCurCreatGroup = m_turnSeq.CurUnit() as? iBattleUnit_CreatGroup
        if (pCurCreatGroup != null) {
            PreparePassMap()
            val pCurGroup = pCurCreatGroup.GetCreatGroup()
            val groups = mutableListOf<iBattleGroup>()
            var bFull = false

            val pid = m_turnSeq.CurUnit()!!.Owner().Owner()
            if (pid == PlayerId.NEUTRAL /* || todo*/ || IsAutoBattle()) {
                TODO()
            } else {
                groups.add(pCurGroup)
            }

            groups.forEach { pg ->
                // todo

                val eg = when (pg.Owner().GetSide()) {
                    iBattleMember.Side.Assaulter -> m_dArmy
                    iBattleMember.Side.Defender -> m_aArmy
                }

                // melee list
                // todo

                // shot list
                if (pg.Shots() != 0) {
                    val bHinder = false
                    val sv = pg.Pos().y and 1
                    // check all neighbor cells
                    // todo

                    // Add all alive enemy groups in range attack target list
                    if (!bHinder) {
                        for (xx in 0 until eg.Count()) {
                            if (eg[xx].IsAlive()) {
                                pg.AddShootEntry(eg[xx].Pos(), CalcShotPenalty(pg, eg[xx].Pos()))
                                if (eg[xx].Size() == 2) {
                                    val tail =
                                        IPointInt(eg[xx].Pos().x + TAIL_OFFSET[eg[xx].Orient().v], eg[xx].Pos().y)
                                    pg.AddShootEntry(tail, CalcShotPenalty(pg, tail))
                                }
                            }
                        }
                    }
                }
            }

            // commented in sources:
            /*
            FILE* pfile = _tfopen(iFormat(_T("C:\\!!!\\%s_info.txt"), gTextMgr[TRID_CREATURE_PEASANT_F2+(pCurGroup->Type()*3)]).CStr(),_T("wt"));
            _ftprintf(pfile,_T("Melee targets: %d:\n"), pCurGroup->MeleeListCount());
            _ftprintf(pfile,_T("Shoot targets: %d:\n"), pCurGroup->ShootListCount());
            fclose(pfile);
            */
        }
        // TODO
    }

    private fun MakeMeleeSequence(
        pActor: iBattleGroup,
        pTarget: iBattleGroup,
        pos: IPointInt,
        penalty: Int,
        mdir: UShort,
        bRetail: Boolean,
        arcList: iBattleActList
    ) {
        // TODO
    }

    private fun MakeShootSequence(
        pActor: iBattleGroup,
        pTarget: iBattleGroup,
        pos: IPointInt,
        penalty: Int,
        actList: iBattleActList
    ) {
        // Check luck status
        val luck = pActor.CalcLuck()
        when (luck) {
            iBattleGroup.MORLUCK_MOD.POSITIVE -> m_ActList.AddAction(iBattleAct_GoodLuck(pActor))
            iBattleGroup.MORLUCK_MOD.NEGATIVE -> m_ActList.AddAction(iBattleAct_BadLuck(pActor))
        }

        val pShootAct = iBattleAct_Shoot(pActor, pos, pTarget, penalty, luck)
        m_ActList.AddAction(pShootAct)
        // [CPERK_LICHSHOOT] Range attack affects adjacent hexes except undeads (Liches)
        if (pActor.HasPerk(Perk.LICHSHOOT)) {
            // todo
        }
    }
}

abstract class iBattleWrapper {

    protected lateinit var m_result: iBattleResult
    protected var m_engine: iBattleEngine

    constructor(bFakeMode: Boolean) {
        m_engine = iBattleEngine(this, bFakeMode)
    }

    suspend fun BeginBattle(bi: iBattleInfo) {
        m_engine.BeginBattle(bi)
        OnBeginBattle()
        m_engine.Start()
        OnStart()
    }

    suspend fun EndBattle(br: iBattleResult) {
        m_result = br
        val bi = m_engine.GetBattleInfo()
        OnEndBattle()
        m_engine.EndBattle(br)
//        bi.Cleanup()  // removed, seems not needed
        gGame.EndBattle()
    }

    fun Engine(): iBattleEngine = m_engine
    fun SetEngine(new: iBattleEngine) {
        m_engine = new
    }

    fun BattleResult(): iBattleResult = m_result

    abstract suspend fun OnBeginBattle()
    abstract fun OnStart()
    abstract suspend fun OnEndBattle()
    abstract fun AddLogEvent(msg: String)
    abstract fun AddCellEvent(msg: String, pos: IPointInt)
}
