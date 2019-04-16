package com.github.servb.pph.pheroes.common

import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.gxlmetrics.Pointc
import com.github.servb.pph.gxlib.memory.DynamicBuffer
import com.github.servb.pph.pheroes.common.army.Army
import com.github.servb.pph.pheroes.common.army.ArmyC
import com.github.servb.pph.pheroes.common.army.CreatGroup
import com.github.servb.pph.pheroes.common.army.CreatGroupC
import com.github.servb.pph.pheroes.common.common.*
import com.github.servb.pph.pheroes.common.common.artifact.ArtifactList
import com.github.servb.pph.pheroes.common.common.artifact.ArtifactListC
import com.github.servb.pph.pheroes.common.common.artifact.HeroArtifactCell
import com.github.servb.pph.pheroes.common.common.artifact.iItem
import com.github.servb.pph.pheroes.common.common.skill.*
import com.github.servb.pph.pheroes.common.creature.CreatureType
import com.github.servb.pph.pheroes.common.magic.MagicSpell
import com.github.servb.pph.pheroes.common.magic.SpellList
import com.github.servb.pph.util.helpertype.readInt
import com.github.servb.pph.util.helpertype.readShort
import java.io.FileInputStream

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, point: Pointc) {
    buff.write(point.x.toShort())
    buff.write(point.y.toShort())
}

@ExperimentalUnsignedTypes
fun unserializePoint(buff: DynamicBuffer): Point {
    val x = buff.readShort().valueOrError.toInt()
    val y = buff.readShort().valueOrError.toInt()

    return Point(x, y)
}

@ExperimentalUnsignedTypes
fun unserializePoint(stream: FileInputStream): Point {
    val x = stream.readShort().toInt()
    val y = stream.readShort().toInt()

    return Point(x, y)
}

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, group: CreatGroupC) {
    buff.write(group.type.v.toShort())
    buff.write(group.count)
}

@ExperimentalUnsignedTypes
fun unserializeCreatureGroup(buff: DynamicBuffer): CreatGroup {
    val typeValue = buff.readShort().valueOrError.toInt()
    val count = buff.readInt().valueOrError

    val type = CreatureType.values().first { it.v == typeValue }

    return CreatGroup(type, count)
}

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, army: ArmyC) {
    army.creatGroups.forEach { serialize(buff, it) }
}

@ExperimentalUnsignedTypes
fun unserializeArmy(buff: DynamicBuffer): Army {
    val groups = mutableListOf<CreatGroup>()

    repeat(7) {
        // TODO: remove constant
        groups.add(unserializeCreatureGroup(buff))
    }

    return Army(groups)
}

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, string: String) {
    buff.write(string)
}

@ExperimentalUnsignedTypes
fun unserializeString(buff: DynamicBuffer): String {
    return buff.readString().valueOrError
}

fun unserializeString(stream: FileInputStream): String {
    val length = stream.readInt()
    if (length == 0) {
        return ""
    }

    val bytes = ByteArray(length)
    stream.read(bytes, 0, length)
    return String(bytes)
}

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, furtherSkills: FurtherSkillsC) {
    var count: UShort = 0u

    for (skill in FurtherSkill.values()) {
        if (furtherSkills.Value(skill) != 0) {
            ++count
        }
    }
    buff.write(count)

    for (skill in FurtherSkill.values()) {
        if (furtherSkills.Value(skill) != 0) {
            buff.write(skill.v)
            buff.write(furtherSkills.Value(skill).toShort())
        }
    }
}

@ExperimentalUnsignedTypes
fun unserializeFurtherSkills(buff: DynamicBuffer): FurtherSkills {
    val count = buff.readUShort().valueOrError.toInt()

    if (count == 0) {
        return FurtherSkills()
    }

    val skills = FurtherSkills()

    repeat(count) {
        val skillValue = buff.readShort().valueOrError.toInt()
        val modValue = buff.readShort().valueOrError.toInt()

        val skill = FurtherSkill.values().first { it.v == skillValue }

        skills.SetValue(skill, modValue)
    }

    return skills
}

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, skills: PrimarySkillsC) {
    skills.values.forEach { buff.write(it) }
}

@ExperimentalUnsignedTypes
fun unserializePrimarySkills(buff: DynamicBuffer): PrimarySkills {
    val skills = PrimarySkills()

    repeat(PrimarySkillType.COUNT.v) {
        val skillValue = buff.readInt().valueOrError
        skills.values.add(skillValue)
    }

    return skills
}

// Mineral Set
// NOTE (TODO?): Minerals now must be encrypted to:
// a) prevent modification
// b) checking real values to detect protection

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, mineralSetC: MineralSetC) {
    mineralSetC.quant.forEach { buff.write(it) }
}

@ExperimentalUnsignedTypes
fun unserializeMineralSet(buff: DynamicBuffer): MineralSet {
    return MineralSet(IntArray(MineralType.COUNT.v) { buff.readInt().valueOrError })
}

fun unserializeMineralSet(stream: FileInputStream): MineralSet {
    return MineralSet(IntArray(MineralType.COUNT.v) { stream.readInt() })
}

@ExperimentalUnsignedTypes
fun serialize(buff: DynamicBuffer, secondarySkills: SecondarySkillsC) {
    buff.write(secondarySkills.size.toUByte())
    secondarySkills.forEach { skill ->
        buff.write(skill.skill.v.toByte())
        buff.write(skill.level.v.toByte())
    }
}

@ExperimentalUnsignedTypes
fun unserializeSecondarySkills(buff: DynamicBuffer): SecondarySkills {
    val size = buff.readUByte().valueOrError.toInt()
    val skills = mutableListOf<SecondarySkillEntry>()

    repeat(size) {
        val typeValue = buff.readByte().valueOrError.toInt()
        val levelValue = buff.readByte().valueOrError.toInt()

        val type = SecondarySkillType.values().first { it.v == typeValue }
        val level = SecondarySkillLevel.values().first { it.v == levelValue }

        skills.add(SecondarySkillEntry(type, level))
    }

    return skills
}

@ExperimentalUnsignedTypes
fun serializeArtifactList(buff: DynamicBuffer, artifacts: ArtifactListC) {
    buff.write(artifacts.size.toUShort())

    for (artifact in artifacts) {
        buff.write(artifact.id)
        buff.write(artifact.assign.v.toShort())
    }
}

@ExperimentalUnsignedTypes
fun unserializeArtifactList(buff: DynamicBuffer): ArtifactList {
    val count = buff.readUShort().valueOrError.toInt()
    val artifacts = mutableListOf<iItem>()

    repeat(count) {
        val idValue = buff.readShort().valueOrError
        val assignValue = buff.readShort().valueOrError.toInt()

        val assign = HeroArtifactCell.values().first { it.v == assignValue }

        artifacts.add(iItem(idValue, assign))
    }

    return artifacts
}

@ExperimentalUnsignedTypes
fun serializeSpellList(buff: DynamicBuffer, spells: SpellList) {
    buff.write(spells.size.toUShort())
    spells.forEach { buff.write(it) }
}

@ExperimentalUnsignedTypes
fun unserializeSpellList(buff: DynamicBuffer): SpellList {
    val count = buff.readUShort().valueOrError.toInt()
    val list = mutableListOf<UShort>()

    repeat(count) {
        val idValue = buff.readUShort().valueOrError
        if (idValue.toInt() < MagicSpell.COUNT.v) {
            list.add(idValue)
        }
    }

    return list
}

@ExperimentalUnsignedTypes
fun serializeRewardsCtr(buff: DynamicBuffer, rewards: RewardsCtrC) {
    buff.write(rewards.size.toUShort())
    rewards.forEach { item ->
        buff.write(item.type.v.toUByte())
        buff.write(item.fParam)
        buff.write(item.sParam)
    }
}

@ExperimentalUnsignedTypes
fun unserializeRewardsCtr(buff: DynamicBuffer): RewardsCtr {
    val quantity = buff.readUShort().valueOrError.toInt()
    val rewards = mutableListOf<RewardItem>()

    repeat(quantity) {
        val typeValue = buff.readUByte().valueOrError.toInt()
        val fParamValue = buff.readInt().valueOrError
        val sParamValue = buff.readInt().valueOrError

        val type = RewardItemType.values().first { it.v == typeValue }

        rewards.add(RewardItem(type, fParamValue, sParamValue))
    }

    return rewards
}
