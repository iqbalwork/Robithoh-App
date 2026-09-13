package com.iqbalwork.robithoh.feature.waktal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.component.ImageViewerManager
import com.iqbalwork.robithoh.core.designsystem.theme.DarkBorder
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.network.createKtorHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.decodeToImageBitmap

object WaktalImageLoaderCache {
    private val cache = mutableMapOf<String, ImageBitmap>()
    private val mutex = Mutex()
    private val httpClient by lazy { createKtorHttpClient() }

    suspend fun loadImage(url: String): ImageBitmap? {
        mutex.withLock {
            cache[url]?.let { return it }
        }

        return try {
            val encodedUrl = url.replace(" ", "%20")
            val bytes: ByteArray = httpClient.get(encodedUrl).body()
            if (bytes.isNotEmpty()) {
                val bitmap = bytes.decodeToImageBitmap()
                mutex.withLock {
                    cache[url] = bitmap
                }
                bitmap
            } else null
        } catch (_: Exception) {
            null
        }
    }
}

@Composable
fun WaktalAvatar(
    url: String?,
    modifier: Modifier = Modifier,
    sizeDp: Int = 52,
    emojiSizeSp: Int = 26,
    enableViewerOnTap: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val isDark = RabithohTheme.colors.isDark
    var bitmap by remember(url) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(url) { mutableStateOf(false) }

    LaunchedEffect(url) {
        if (!url.isNullOrBlank()) {
            isLoading = true
            bitmap = WaktalImageLoaderCache.loadImage(url)
            isLoading = false
        } else {
            bitmap = null
            isLoading = false
        }
    }

    val currentBitmap = bitmap
    val canTap = enableViewerOnTap && (!url.isNullOrBlank() || currentBitmap != null)
    val clickableModifier = if (canTap) {
        Modifier.clickable {
            if (onClick != null) {
                onClick()
            } else {
                ImageViewerManager.show(currentBitmap ?: url)
            }
        }
    } else Modifier

    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(if (isDark) DarkBorder else Color(0xFFFFF1F2))
            .then(clickableModifier),
        contentAlignment = Alignment.Center
    ) {
        val currentBitmap = bitmap
        if (currentBitmap != null) {
            Image(
                bitmap = currentBitmap,
                contentDescription = "Foto Wakil Talqin",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size((sizeDp / 2.5).dp),
                strokeWidth = 2.dp,
                color = MerahMerdeka
            )
        } else {
            Text(
                text = "👳‍♂️",
                fontSize = emojiSizeSp.sp
            )
        }
    }
}
