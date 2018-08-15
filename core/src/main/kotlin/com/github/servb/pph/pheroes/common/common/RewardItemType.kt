package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class RewardItemType(
        override val v: Int,
        private val fParam: String? = null,
        private val sParam: String? = null
) : UniqueValueEnum, UndefinedCountValueEnum {
    INVALID(-1),
    MINERAL(0, "Type", "Quantity"),
    EXPERIENCE(1, "None", "Quantity"),
    MANAPTS(2, "None", "Quantity"),
    TRAVELPTS(3, "None", "Quantity"),
    MORALE(4, "None", "Modifier"),
    LUCK(5, "None", "Modifier"),
    FURTSKILL(6, "Type", "Modifier (Primary skill only)"),
    ARTIFACT(7, "ArtifactType level or idx", "None (can be defined as random)"),
    MAGICSPELL(8, "Spell level or idx", "None (can be defined as random)"),
    CREATGROUP(9, "Type", "Quantity"),
    COUNT(10);
}
