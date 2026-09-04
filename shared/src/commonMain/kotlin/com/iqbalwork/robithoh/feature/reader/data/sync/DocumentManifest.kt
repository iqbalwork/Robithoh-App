package com.iqbalwork.robithoh.feature.reader.data.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentManifest(
    @SerialName("version")
    val version: Int = 1,
    @SerialName("updatedAt")
    val updatedAt: String = "",
    @SerialName("totalDocuments")
    val totalDocuments: Int = 0,
    @SerialName("documents")
    val documents: List<ManifestDocumentItem> = emptyList()
)

@Serializable
data class ManifestDocumentItem(
    @SerialName("fileName")
    val fileName: String,
    @SerialName("sha256")
    val sha256: String,
    @SerialName("size")
    val size: Long = 0L,
    @SerialName("url")
    val url: String = "",
    @SerialName("rawUrl")
    val rawUrl: String? = null,
    @SerialName("pagesUrl")
    val pagesUrl: String? = null
)
