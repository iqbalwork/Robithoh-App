package com.iqbalwork.robithoh.feature.reader.model

import kotlinx.serialization.Serializable

@Serializable
data class LiturgyDocument(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val category: String,
    val fileName: String,
    val arabicTitle: String = "",
    val languageBadge: String? = null,
    val iconName: String? = null,
    val isSingleDocumentView: Boolean = false,
    val alternateLanguageDocId: String? = null
)

@Serializable
data class LiturgyVerse(
    val index: Int,
    val title: String = "",
    val arabic: String = "",
    val latin: String = "",
    val translation: String = "",
    val note: String = "",
    val repeatCount: Int = 1
)

@Serializable
data class ParsedDocument(
    val info: LiturgyDocument,
    val rawContent: String,
    val verses: List<LiturgyVerse>
)
