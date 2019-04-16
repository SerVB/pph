package com.github.servb.pph.pheroes.common

import com.github.servb.pph.gxlib.memory.DynamicBuffer
import com.github.servb.pph.pheroes.common.army.Army
import com.github.servb.pph.pheroes.common.common.RewardsCtr

@ExperimentalUnsignedTypes
class TreasuryVariantsContainer {
    class Item(val probability: UInt) {
        var guards: Army? = null
        var rewards: RewardsCtr? = null
    }

    private val items = mutableListOf<Item>()

    fun addVariant(probability: UInt) {
        items.add(Item(probability))
    }

    val lastVariant: Item get() = items.last()

    fun getVariant(idx: Int) = items[idx]

    val variantsCount: Int get() = items.size

    fun serialize(buff: DynamicBuffer) {
        val quantity = items.size.toUShort()
        buff.write(quantity)
        for (item in items) {
            buff.write(item.probability.toUByte())
            serialize(buff, item.guards!!)
            serializeRewardsCtr(buff, item.rewards!!)
        }
    }

    fun unserialize(buff: DynamicBuffer) {
        items.clear()
        val quantity = buff.readShort().valueOrError.toInt()

        repeat(quantity) {
            val probability = buff.readUByte().valueOrError.toUInt()
            items.add(Item(probability))
            items.last().guards = unserializeArmy(buff)
            items.last().rewards = unserializeRewardsCtr(buff)
        }
    }
}
