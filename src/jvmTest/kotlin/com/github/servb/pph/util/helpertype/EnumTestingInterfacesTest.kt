package com.github.servb.pph.util.helpertype

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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

                if (originalSize != distinctSize) {
                    println("Not unique values in $enumClass")
                    print("Not unique values: ")
                    println(enumClass.enumConstants.map { it.v }.groupBy { it }.filter { (_, v) -> v.size > 1 })
                }

                originalSize shouldBe distinctSize
            }
        }
    }
}

class CountValueEnumTest : StringSpec() {
    private val appPackage = "com.github.servb.pph"

    init {
        "COUNT value in each enum should be the number of the previous members" {
            val subjects = Reflections(appPackage).getSubTypesOf(CountValueEnum::class.java)

            println("CountValueEnums found: ${subjects.size}")

            subjects.size shouldNotBe 0  // When get rid of classes -> remove the test

            subjects.forEach { enumClass ->
                var hasCount = false
                var count = 0

                for (member in enumClass.enumConstants) {
                    val name = member.toString().toLowerCase()

                    if ("count" == name) {
                        if (member.v != count) {
                            println("Incorrect count in $enumClass")
                        }

                        member.v shouldBe count
                        hasCount = true
                    } else {
                        ++count
                    }
                }

                if (!hasCount) {
                    println("No count value in $enumClass")
                }

                hasCount shouldBe true
            }
        }
    }
}

class UndefinedCountValueEnumTest : StringSpec() {
    private val appPackage = "com.github.servb.pph"

    private val passingMembers = setOf("undefined", "invalid", "none", "neutral", "unknown")

    init {
        "COUNT value in each enum should be the number of the previous members except passing" {
            val subjects = Reflections(appPackage).getSubTypesOf(UndefinedCountValueEnum::class.java)

            println("UndefinedCountValueEnums found: ${subjects.size}")

            subjects.size shouldNotBe 0  // When get rid of classes -> remove the test

            subjects.forEach { enumClass ->
                var hasCount = false
                var count = 0

                var hasPassing = false

                val list = mutableListOf<UndefinedCountValueEnum>()

                for (member in enumClass.enumConstants) {
                    val name = member.toString().toLowerCase()

                    if (!hasPassing && passingMembers.any { it == name }) {
                        hasPassing = true
                        continue
                    } else if ("count" == name) {
                        if (member.v != count) {
                            println("Incorrect count in $enumClass")
                            println(list.withIndex().joinToString("\n"))
                        }

                        member.v shouldBe count
                        hasCount = true
                    } else {
                        ++count
                        list.add(member)
                    }
                }

                if (!hasPassing) {
                    println("No passing in $enumClass")
                }

                hasPassing shouldBe true

                if (!hasCount) {
                    println("No count value in $enumClass")
                }

                hasCount shouldBe true
            }
        }
    }
}
