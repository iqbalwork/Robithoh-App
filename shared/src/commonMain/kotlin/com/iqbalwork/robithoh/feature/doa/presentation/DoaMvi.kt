package com.iqbalwork.robithoh.feature.doa.presentation

import com.iqbalwork.robithoh.core.presentation.UiEffect
import com.iqbalwork.robithoh.core.presentation.UiIntent
import com.iqbalwork.robithoh.core.presentation.UiState
import com.iqbalwork.robithoh.feature.reader.model.LiturgyDocument

data class DoaUiState(
    val searchQuery: String = "",
    val documents: List<LiturgyDocument> = emptyList(),
    val filteredDocuments: List<LiturgyDocument> = emptyList(),
    val isLoading: Boolean = false
) : UiState

sealed interface DoaUiIntent : UiIntent {
    data class SearchDoa(val query: String) : DoaUiIntent
    data class SelectDocument(val documentId: String) : DoaUiIntent
}

sealed interface DoaUiEffect : UiEffect {
    data class NavigateToDocument(val documentId: String) : DoaUiEffect
}
