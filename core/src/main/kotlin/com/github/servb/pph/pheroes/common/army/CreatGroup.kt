package com.github.servb.pph.pheroes.common.army

import com.github.servb.pph.pheroes.common.common.RANDOM_QUANTITY
import com.github.servb.pph.pheroes.common.common.RAND_VAL
import com.github.servb.pph.pheroes.common.creature.CreatureType

val creatGrowthDivider = listOf(9, 9, 10, 10, 11, 12)

@ExperimentalUnsignedTypes
abstract class CreatGroupC {
    abstract val type: CreatureType
    abstract val count: Int

    val isValid: Boolean get() = type != CreatureType.UNKNOWN
    val groupPower: UInt get() = (count * type.descriptor!!.pidx).toUInt()
}

@ExperimentalUnsignedTypes
data class CreatGroup(
        override var type: CreatureType = CreatureType.UNKNOWN,
        override var count: Int = RANDOM_QUANTITY
) : CreatGroupC() {

    fun reset(type: CreatureType = CreatureType.UNKNOWN, count: Int = RANDOM_QUANTITY) {
        this.type = type
        this.count = count
    }

    fun grow(weeks: UInt = 1u) {
        check(count != RAND_VAL)

        for (i in 1u..weeks) {
            var div = creatGrowthDivider[type.descriptor!!.level - 1]
            if (count < type.descriptor!!.growth * 2) {
                div /= 2
            }

            count += (count + (div - 1)) / 2
        }
    }
}
