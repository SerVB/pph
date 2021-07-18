package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.army.Army
import com.github.servb.pph.pheroes.common.common.skill.FurtherSkill
import com.github.servb.pph.pheroes.common.common.skill.FurtherSkills
import com.github.servb.pph.pheroes.common.creature.CreatureType
import com.github.servb.pph.util.SizeT
import com.soywiz.kmem.ByteArrayBuilder
import com.soywiz.korio.stream.AsyncInputStream

class iHero {

//    constructor(pProto: iHeroT, fake: Int)  // todo

    fun Serialize(dbuff: ByteArrayBuilder): Unit = TODO()
    suspend fun Deserialize(dbuff: AsyncInputStream, bInit: Boolean): Unit = TODO()

    fun Deinit(bResetArmy: Boolean): Unit = TODO()
//    fun Init()  // todo

    fun army(): Army = Army().apply {
        // todo: write actual implementation
        addGroup(CreatureType.YOUNG_MAGE, 1)
//        addGroup(CreatureType.ARCHER, 200)
//        addGroup(CreatureType.MONK, 100)
//        addGroup(CreatureType.ORC, 200)
//        addGroup(CreatureType.TROLL, 50)
//        addGroup(CreatureType.YOUNG_MAGE, 300)
//        addGroup(CreatureType.MAGE, 50)
//        addGroup(CreatureType.THOR, 25)
    }

    fun Owner(): iPlayer = iPlayer()  // todo

    fun Level(): SizeT = 2  // todo

    fun GetFullFurtSkills(): FurtherSkills = FurtherSkills().apply {  // todo
        SetValue(FurtherSkill.ATTACK, 20)
        SetValue(FurtherSkill.DEFENCE, 0)
        SetValue(FurtherSkill.POWER, 1)
        SetValue(FurtherSkill.KNOWLEDGE, 1)
    }

    fun ConvExpPts(exppts: Int): Int = exppts + exppts //* FurtSkill(FurtherSkill.LEARNING) / 100  // todo

    fun OnEndBattle(bWin: Boolean) {
        println("OnEndBattle($bWin)")  // todo
    }
}