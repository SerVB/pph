package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.army.Army
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.common.skill.FurtherSkills
import com.github.servb.pph.pheroes.common.common.skill.FurtherSkillsC
import com.github.servb.pph.util.SizeT

enum class BATTLE_RESULT {
    NA, ASSAULTER_WIN, DEFENDER_WIN
}

enum class DEFEAT_CAUSE {
    DEFEAT, RETREAT, SURRENDER
}

class iBattleResult {

    val m_result: BATTLE_RESULT
    val m_defCause: DEFEAT_CAUSE

    //    val m_pDest: iCastle  // todo
    val m_cost: SizeT
    var m_pLoser: iBattleMember?
    var m_pWinner: iBattleMember?
    var m_winCas: Int = 0
    var m_losCas: Int = 0
    var m_experience: Int

    constructor() {
        m_result = BATTLE_RESULT.NA
        m_defCause = DEFEAT_CAUSE.DEFEAT
//        m_pDest = null  // todo
        m_cost = 0
        m_pLoser = null
        m_pWinner = null
        m_experience = 0
    }

    constructor(result: BATTLE_RESULT, defCause: DEFEAT_CAUSE,/* pDest: iCastle = null,*/ cost: SizeT = 0) {
        m_result = result
        m_defCause = defCause
//        m_pDest = pDest
        m_cost = cost
        m_pLoser = null
        m_pWinner = null
        m_experience = 0
    }
}

// Battle side
abstract class iBattleMember {

    enum class Side {
        Assaulter, Defender
    }

    enum class Type {
        Undefined,
        Hero,
        Castle,
        OwnCnst,
        VisCnst,
        MapItem,
        MapEvent,
        MapGuard
    }

    protected var m_bCastFlag: Boolean
    protected var m_Experience: Int
    protected val m_Type: Type
    protected val m_Side: Side
    protected val m_furtSkills: FurtherSkills

    constructor(type: Type, side: Side, furtSkills: FurtherSkillsC) {
        m_Type = type
        m_Side = side
        m_furtSkills = FurtherSkills(furtSkills)
        m_Experience = 0
        m_bCastFlag = false
    }

    fun GetSide(): Side = m_Side
    fun MemberType(): Type = m_Type
    fun FurtSkills(): FurtherSkillsC = m_furtSkills
    fun GetExperience(): Int = m_Experience

    fun AddExperience(exp: Int) {
        m_Experience += exp
    }

    fun CanCastSpell(): Boolean = SpellCaster() != null && !m_bCastFlag  // todo

    fun SetCastFlag(bCastFlag: Boolean = true) {
        m_bCastFlag = bCastFlag
    }

    open fun Owner(): PlayerId = PlayerId.NEUTRAL
    open fun OnPrepare(pEnemy: iBattleMember) {}
    open fun OnWin(br: iBattleResult) {}
    open fun OnLose(br: iBattleResult) {}

    abstract fun army(): Army

    //    abstract fun BaseMapObject(): iBaseMapObject  // todo
    abstract fun SpellCaster(): iHero?
}

class iBattleMember_Hero : iBattleMember {

    private val m_pHero: iHero

    constructor(pHero: iHero, side: Side) : super(Type.Hero, side, pHero.GetFullFurtSkills()) {
        m_pHero = pHero
    }

    override fun OnWin(br: iBattleResult) {
        // transfer artifacts from loser
        // todo

        // Raise skeletons
        // todo

        // receive experience
        // todo

        m_pHero.OnEndBattle(true)
    }

    override fun OnLose(br: iBattleResult) {
        m_pHero.OnEndBattle(true)

        // todo
    }

    override fun Owner(): PlayerId = m_pHero.Owner().PlayerId()
    override fun army(): Army = m_pHero.army()
    fun GetHero(): iHero = m_pHero
    override fun SpellCaster(): iHero = m_pHero
}

class iBattleMember_Castle  // todo
class iBattleMember_OwnCnst  // todo
class iBattleMember_VisCnst  // todo
class iBattleMember_MapItem  // todo
class iBattleMember_MapEvent  // todo
class iBattleMember_MapGuard  // todo

class iBattleInfo {

    val m_pAssaulter: iBattleMember
    val m_pDefender: iBattleMember

    constructor(pAssaulter: iHero, pDefender: iHero) {
        m_pAssaulter = iBattleMember_Hero(pAssaulter, iBattleMember.Side.Assaulter)
        m_pDefender = iBattleMember_Hero(pDefender, iBattleMember.Side.Defender)
        m_pAssaulter.OnPrepare(m_pDefender)
        m_pDefender.OnPrepare(m_pAssaulter)
    }

    // todo
//    constructor(pAssaulter: iHero, pDefender: iCastle)
//
//    constructor(pAssaulter: iHero, pDefender: iOwnCnst)
//
//    constructor(pAssaulter: iHero, pDefender: iVisCnst)
//
//    constructor(pAssaulter: iHero, pDefender: iMapItem)
//
//    constructor(pAssaulter: iHero, pDefender: iMapEvent)
//
//    constructor(pAssaulter: iHero, pDefender: iMapGuard)

    fun Member(side: iBattleMember.Side): iBattleMember = when (side) {
        iBattleMember.Side.Assaulter -> m_pAssaulter
        iBattleMember.Side.Defender -> m_pDefender
    }

    fun InteractorSide(): iBattleMember.Side {
        // todo
        return iBattleMember.Side.Assaulter
    }

    fun InteractorPlayer(): PlayerId {
        val side = InteractorSide()
        return Member(side).Owner()
    }

//    fun Defender(): iBaseMapObject = m_pDefender.BaseMapObject()  // todo

//    fun HasArtInBattle(artt: ArtifactType): iHero  // todo
}
