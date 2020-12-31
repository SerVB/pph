package com.github.servb.pph.pheroes.common.common.skill

data class SecondarySkillEntry(
        val skill: SecondarySkillType = SecondarySkillType.NONE,
        val level: SecondarySkillLevel = SecondarySkillLevel.NONE
)
