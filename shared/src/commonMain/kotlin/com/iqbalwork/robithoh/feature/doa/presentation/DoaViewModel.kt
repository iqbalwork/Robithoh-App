package com.iqbalwork.robithoh.feature.doa.presentation

import com.iqbalwork.robithoh.core.database.RobithohDatabase
import com.iqbalwork.robithoh.core.presentation.MviViewModel
import com.iqbalwork.robithoh.feature.reader.data.MarkdownDocumentRepository
import com.iqbalwork.robithoh.feature.reader.model.LiturgyDocument

class DoaViewModel(
    repository: MarkdownDocumentRepository? = null,
    database: RobithohDatabase? = null
) : MviViewModel<DoaUiState, DoaUiIntent, DoaUiEffect>(DoaUiState()) {

    private val docRepo = repository ?: MarkdownDocumentRepository(database = database)

    init {
        loadDoaDocuments()
    }

    override fun onIntent(intent: DoaUiIntent) {
        when (intent) {
            is DoaUiIntent.SearchDoa -> searchDoa(intent.query)
            is DoaUiIntent.SelectDocument -> sendEffect(DoaUiEffect.NavigateToDocument(intent.documentId))
        }
    }

    private fun loadDoaDocuments() {
        val allDocs = docRepo.allDocuments.filter { doc ->
            doc.category == "Doa & Ziarah" || doc.id.contains("doa")
        }
        updateState {
            copy(
                documents = allDocs,
                filteredDocuments = filterList(allDocs, searchQuery),
                isLoading = false
            )
        }
    }

    private fun searchDoa(query: String) {
        updateState {
            copy(
                searchQuery = query,
                filteredDocuments = filterList(documents, query)
            )
        }
    }

    private fun filterList(list: List<LiturgyDocument>, query: String): List<LiturgyDocument> {
        if (query.isBlank()) return list
        val q = query.trim().lowercase()
        return list.filter { doc ->
            doc.title.lowercase().contains(q) ||
                doc.subtitle.lowercase().contains(q) ||
                doc.arabicTitle.contains(q) ||
                (doc.languageBadge?.lowercase()?.contains(q) == true)
        }
    }
}
