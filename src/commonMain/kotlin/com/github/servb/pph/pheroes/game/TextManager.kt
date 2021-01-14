package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.LANG_DATA
import com.github.servb.pph.pheroes.common.TextResId
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.properties.Delegates

enum class Language(val displayName: String) {

    ENGLISH("English"),
    FRENCH("Français (French)"),
    ITALIAN("Italiana (Italian)"),
    GERMAN("Deutsche (German)"),
    POLISH("Polskie (Polish)"),
    RUSSIAN("Русский (Russian)"),
    SLOVAK("Slovák (Slovak)"),
}

private const val CONSTANT_PREFIX = "TRID_"
private const val LANGUAGE_FILE_REGEX = """\{(.+)}.*,.*\{.*}.*,.*\{(.*)}"""

class iTextManager {

    private var m_bHasLngFile: Boolean by Delegates.notNull()
    private val m_lngData: MutableMap<TextResId, String> = mutableMapOf()

    fun Init(): Boolean {
        m_bHasLngFile = false
        return true
    }

    suspend fun SetLanguage(language: Language) {
        m_lngData.clear()

        if (language == Language.ENGLISH) {
            m_bHasLngFile = false
            return
        }

        m_bHasLngFile = true

        val regex = LANGUAGE_FILE_REGEX.toRegex()

        val resourceFilePath = "pheroes/bin/Resources/hmm/LNG/${language.name.toLowerCase()}.txt"
        resourcesVfs[resourceFilePath].readLines().forEach { line ->
            regex.matchEntire(line.trim())?.let { match ->
                val (constant, text) = match.destructured
                val fqn = CONSTANT_PREFIX + constant
                try {
                    val trid = TextResId.valueOf(fqn)
                    m_lngData[trid] = text
                } catch (e: Exception) {  // replace with IAE after https://youtrack.jetbrains.com/issue/KT-35116
                    println("Missing $fqn for $language, using English one")
                    // todo: add proper logging and find missing translations via tests
                }
            }
        }
    }

    operator fun get(resId: TextResId): String {
        return if (m_bHasLngFile) {
            m_lngData[resId] ?: LANG_DATA[resId.v]
        } else {
            LANG_DATA[resId.v]
        }
    }

    operator fun get(resId: Int): String {
        return if (m_bHasLngFile) {
            m_lngData[TextResId.values()[resId]] ?: LANG_DATA[resId]
        } else {
            LANG_DATA[resId]
        }
    }
}
