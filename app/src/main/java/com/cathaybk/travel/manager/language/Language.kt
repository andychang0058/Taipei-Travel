package com.cathaybk.travel.manager.language

sealed class Language {

    abstract val tag: String

    companion object {
        fun getAllSupportLanguage(): List<Language> {
            return listOf(
                English,
                ChineseTraditional,
                ChineseSimplified,
                Japanese,
                Korean,
                Spanish,
                Indonesian,
                Thai,
                Vietnamese
            )
        }

        fun fromTag(tag: String?): Language? {
            return when (tag) {
                English.tag -> English
                ChineseTraditional.tag -> ChineseTraditional
                ChineseSimplified.tag -> ChineseSimplified
                Japanese.tag -> Japanese
                Korean.tag -> Korean
                Spanish.tag -> Spanish
                Indonesian.tag -> Indonesian
                Thai.tag -> Thai
                Vietnamese.tag -> Vietnamese
                else -> null
            }
        }

        val default: Language get() = English
    }

    data object English : Language() {
        override val tag: String = "en"
    }

    data object ChineseTraditional : Language() {
        override val tag: String = "zh-TW"
    }

    data object ChineseSimplified : Language() {
        override val tag: String = "zh-CN"
    }

    data object Japanese : Language() {
        override val tag: String = "ja"
    }

    data object Korean : Language() {
        override val tag: String = "ko"
    }

    data object Spanish : Language() {
        override val tag: String = "es"
    }

    data object Indonesian : Language() {
        override val tag: String = "in"
    }

    data object Thai : Language() {
        override val tag: String = "th"
    }

    data object Vietnamese : Language() {
        override val tag: String = "vi"
    }
}