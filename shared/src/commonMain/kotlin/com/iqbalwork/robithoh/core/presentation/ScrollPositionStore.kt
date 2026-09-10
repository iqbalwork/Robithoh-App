package com.iqbalwork.robithoh.core.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest

@Stable
data class LazyListScrollPosition(
    val index: Int = 0,
    val offset: Int = 0
)

@Stable
class ScrollPositionStore {
    val lazyListPositions = mutableStateMapOf<String, LazyListScrollPosition>()
    val scrollPositions = mutableStateMapOf<String, Int>()

    fun getLazyListPosition(key: String): LazyListScrollPosition {
        return lazyListPositions[key] ?: LazyListScrollPosition()
    }

    fun saveLazyListPosition(key: String, index: Int, offset: Int) {
        lazyListPositions[key] = LazyListScrollPosition(index, offset)
    }

    fun getScrollPosition(key: String): Int {
        return scrollPositions[key] ?: 0
    }

    fun saveScrollPosition(key: String, value: Int) {
        scrollPositions[key] = value
    }

    fun clear(key: String) {
        lazyListPositions.remove(key)
        scrollPositions.remove(key)
    }

    companion object {
        val Saver: Saver<ScrollPositionStore, Map<String, Any>> = Saver(
            save = { store ->
                val lazyListMap = store.lazyListPositions.mapValues { listOf(it.value.index, it.value.offset) }
                val scrollMap = store.scrollPositions.toMap()
                mapOf("lazy" to lazyListMap, "scroll" to scrollMap)
            },
            restore = { map ->
                val store = ScrollPositionStore()
                @Suppress("UNCHECKED_CAST")
                val lazyListMap = map["lazy"] as? Map<String, List<Int>>
                lazyListMap?.forEach { (k, list) ->
                    if (list.size >= 2) {
                        store.lazyListPositions[k] = LazyListScrollPosition(list[0], list[1])
                    }
                }
                @Suppress("UNCHECKED_CAST")
                val scrollMap = map["scroll"] as? Map<String, Int>
                scrollMap?.forEach { (k, v) ->
                    store.scrollPositions[k] = v
                }
                store
            }
        )
    }
}

val LocalScrollPositionStore = compositionLocalOf { ScrollPositionStore() }

@Composable
fun rememberPersistedLazyListState(
    key: String,
    store: ScrollPositionStore = LocalScrollPositionStore.current
): LazyListState {
    val initialPos = store.getLazyListPosition(key)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialPos.index,
        initialFirstVisibleItemScrollOffset = initialPos.offset
    )

    LaunchedEffect(key, listState) {
        snapshotFlow {
            LazyListScrollPosition(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collectLatest { pos ->
            store.saveLazyListPosition(key, pos.index, pos.offset)
        }
    }

    return listState
}

@Composable
fun rememberPersistedScrollState(
    key: String,
    store: ScrollPositionStore = LocalScrollPositionStore.current
): ScrollState {
    val initialVal = store.getScrollPosition(key)
    val scrollState = rememberScrollState(initial = initialVal)

    LaunchedEffect(key, scrollState) {
        snapshotFlow { scrollState.value }.collectLatest { valScroll ->
            store.saveScrollPosition(key, valScroll)
        }
    }

    return scrollState
}
