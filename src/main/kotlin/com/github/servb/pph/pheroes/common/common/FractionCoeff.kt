package com.github.servb.pph.pheroes.common.common

data class FractionCoeff(var num: Int, var denum: Int) {
    constructor() : this(-1, -1)

    fun IsValid(): Boolean {
        return num != -1 && denum != -1
    }

    fun GetNormalized(): FractionCoeff {
        return when {
            num > denum -> FractionCoeff(num / denum, 1)
            denum > num -> FractionCoeff(1, (denum + num - 1) / num)
            else -> FractionCoeff(1, 1)
        }
    }
}
