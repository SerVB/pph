package com.github.servb.pph.pheroes.common.army

import com.github.servb.pph.pheroes.common.common.NationType
import com.github.servb.pph.pheroes.common.creature.CreatureType

abstract class ArmyC {
    /** size = 7. */
    abstract val creatGroups: List<CreatGroupC>

    fun canAddGroup(type: CreatureType): Boolean {
        check(type != CreatureType.UNKNOWN)

        return creatGroups.any { !it.isValid || it.type == type }
    }

    val slowestSpeed: Int
        get() = creatGroups
            .filter { it.isValid }
            .minByOrNull { it.type.descriptor!!.speed }
            ?.type
            ?.descriptor
            ?.speed ?: throw IllegalStateException("No valid groups")

    val strongestCreature: CreatureType
        get() = creatGroups
            .filter { it.isValid }
            .maxByOrNull { it.type.descriptor!!.pidx }
            ?.type ?: throw IllegalStateException("No valid groups")

    val moraleModifier: Int
        get() {
            var undeads = false

            var alignments = 0
            val nations = mutableSetOf<NationType>()

            for (group in creatGroups) {
                if (group.isValid && group.type.descriptor!!.nation !in nations) {
                    ++alignments

                    group.type.descriptor!!.nation.let {
                        nations.add(it)

                        if (it == NationType.UNDEADS) {
                            undeads = true
                        }
                    }
                }
            }

            var res = 0
            when {
                // Troops of >=5 alignment -3
                alignments >= 5 -> res -= 3

                // Troops of 4 alignment -2
                alignments == 4 -> res -= 2

                // Troops of 3 alignment -1
                alignments == 3 -> res -= 1

                // All troops of one alignment +1
                alignments <= 1 -> res += 1
            }

            // Undead in army -1
            if (undeads) {
                res -= 1
            }

            return res
        }

    val empty: Boolean get() = creatGroups.all { it.type == CreatureType.UNKNOWN }

    fun hasCreatures(type: CreatureType): Boolean = creatGroups.any { it.type == type }

    val groupCount: Int get() = creatGroups.count { it.type != CreatureType.UNKNOWN }

    val armyPower: UInt
        get() = creatGroups
                .filter { it.type != CreatureType.UNKNOWN }
                .map(CreatGroupC::groupPower)
                .reduce { a, b -> a + b }

    open operator fun get(idx: Int): CreatGroupC = creatGroups[idx]
}

data class Army private constructor(override val creatGroups: MutableList<CreatGroup>) : ArmyC() {

    constructor() : this((1..7).map { CreatGroup() }.toMutableList())

    fun addGroup(type: CreatureType, count: Int): Boolean {
        check(type != CreatureType.UNKNOWN)

        val noEmpty = -1
        var firstEmpty = noEmpty

        // first try to find similar type group
        for (xx in creatGroups.indices) {
            if (creatGroups[xx].type == type) {
                creatGroups[xx].count += count
                if (creatGroups[xx].count <= 0) {
                    creatGroups[xx].type = CreatureType.UNKNOWN
                }
                return true
            } else if (firstEmpty == noEmpty && !creatGroups[xx].isValid) {
                firstEmpty = xx
            }
        }

        if (count > 0) {
            if (firstEmpty != noEmpty) {
                creatGroups[firstEmpty].type = type
                creatGroups[firstEmpty].count = count
                return true
            }
        } else {
            return true  // no need to select a place for negative
        }

        // there is no free cell
        return false
    }

    fun creatureCount(type: CreatureType): Int = creatGroups.filter { it.type == type }.sumOf { it.count }

    val weakestCreatures: CreatGroup
        get() = creatGroups
            .filter { it.isValid }
            .minByOrNull { it.type.descriptor!!.pidx } ?: throw IllegalStateException("No valid groups")

    val weakestGroup: CreatGroup
        get() = creatGroups
            .filter { it.isValid }
            .minByOrNull { it.groupPower } ?: throw IllegalStateException("No valid groups")

    fun joinGroups(): Boolean {
        var joined = false

        for (xx in creatGroups.indices) {
            for (yy in (xx + 1) until creatGroups.size) {
                if (creatGroups[xx].isValid && creatGroups[xx].type == creatGroups[yy].type) {
                    creatGroups[xx].count += creatGroups[yy].count
                    creatGroups[yy].reset()

                    joined = true
                }
            }
        }

        return joined
    }

    fun resetGroup(idx: Int) = creatGroups[idx].reset()

    fun resetArmy() = creatGroups.forEach { it.reset() }

    fun setGroup(idx: Int, type: CreatureType, count: Int) {
        check(idx in creatGroups.indices)

        creatGroups[idx].reset(type = type, count = count)
    }

    val firstGroup: CreatGroup get() = creatGroups.first { it.type != CreatureType.UNKNOWN }

    override operator fun get(idx: Int): CreatGroup = creatGroups[idx]

    fun setTo(other: ArmyC) {
        for ((idx, group) in other.creatGroups.withIndex()) {
            this.creatGroups[idx] = CreatGroup(type = group.type, count = group.count)
        }
    }
}
