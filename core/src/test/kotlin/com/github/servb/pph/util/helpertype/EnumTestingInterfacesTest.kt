package com.github.servb.pph.util.helpertype

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.reflections.Reflections

class UniqueValueEnumTest : StringSpec() {
    private val appPackage = "com.github.servb.pph"

    init {
        "values in each enum should be unique" {
            val subjects = Reflections(appPackage).getSubTypesOf(UniqueValueEnum::class.java)

            println("UniqueValueEnums found: ${subjects.size}")

            subjects.size shouldNotBe 0  // When get rid of classes -> remove the test

            subjects.forEach { enumClass ->
                val originalSize = enumClass.enumConstants.size
                val distinctSize = enumClass.enumConstants.map { it.v }.toSet().size

                originalSize shouldBe distinctSize
            }
        }
    }
}

class CountValueEnumTest : StringSpec() {
    private val appPackage = "com.github.servb.pph"

    private val passingMembers = setOf("undefined", "invalid")

    init {
        "COUNT value in each enum should be the number of the previous members except passing" {
            val subjects = Reflections(appPackage).getSubTypesOf(CountValueEnum::class.java)

            println("CountValueEnums found: ${subjects.size}")

            subjects.size shouldNotBe 0  // When get rid of classes -> remove the test

            subjects.forEach { enumClass ->
                var hasCount = false
                var count = 0

                for (member in enumClass.enumConstants) {
                    val name = member.toString().toLowerCase()

                    if (passingMembers.any { name.contains(it) }) {
                        continue
                    } else if (name.contains("count")) {
                        member.v shouldBe count
                        hasCount = true
                    } else {
                        ++count
                    }
                }

                hasCount shouldBe true
            }
        }
    }
}
