package com.github.servb.pph.pheroes.common.creature

import com.github.servb.pph.pheroes.common.common.MineralSetC
import com.github.servb.pph.pheroes.common.common.NationType

data class CreatureDescriptor(
        val level: Int,  // unit level (1-6)
        val nation: NationType,  // unit alignment

        val attack: Int,  // attack skill
        val defence: Int,  // deffence skill
        val speed: Int,  // unit speed (1-20)
        val size: Int,  // unit size (1 or 2 - used in battle)

        val transType: TransportationType,  // transportation method
        val shots: Int,  // 0 - means no range attack
        val hits: Int,  // hit points (health)
        val damage_min: Int,  // minimum damage
        val damage_max: Int,  // maximum damage

        val cost: MineralSetC,  // cost per unit
        val growth: Int,  // growth rate
        val pidx: Int,  // power index (used for AI)
        val perks: Int  // creature perks
)
