package com.iqbalwork.robithoh.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.*

/**
 * Represents a clickable action row inside [ContentItemOptionsSheet].
 */
data class ContentItemOption(
    val icon: String,
    val label: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit
)

/**
 * Reusable Bottom Sheet modal for quick actions (Copy, Share, Play Audio, Tasbih, etc.)
 * for items in LazyColumn lists, matching the style of [AyahOptionsSheet].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItemOptionsSheet(
    title: String,
    onDismiss: () -> Unit,
    subtitle: String? = null,
    onCopy: (() -> Unit)? = null,
    copyLabel: String = "Salin Teks",
    onShare: (() -> Unit)? = null,
    shareLabel: String = "Bagikan",
    customOptions: List<ContentItemOption> = emptyList()
) {
    val isDark = RabithohTheme.colors.isDark
    val textColor = if (isDark) PutihBersih else SlateCharcoalText
    val dividerColor = if (isDark) DarkBorder else BorderSubtle

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) DarkSurface else PutihBersih,
        shape = RabithohTheme.shapes.bottomSheetShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Surface(
                    color = MerahMerdeka,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, EmasKhidmat.copy(alpha = 0.7f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = title,
                            color = PutihBersih,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                color = EmasMuda,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Custom Options (e.g. Play Murotal, Open Tasbih)
            customOptions.forEach { option ->
                ContentOptionRow(
                    icon = option.icon,
                    label = option.label,
                    enabled = option.enabled,
                    onClick = {
                        onDismiss()
                        option.onClick()
                    }
                )
            }

            // Share Action
            if (onShare != null) {
                ContentOptionRow(
                    icon = "📤",
                    label = shareLabel,
                    onClick = {
                        onDismiss()
                        onShare()
                    }
                )
            }

            // Copy Action
            if (onCopy != null) {
                ContentOptionRow(
                    icon = "📋",
                    label = copyLabel,
                    onClick = {
                        onDismiss()
                        onCopy()
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = dividerColor)
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tutup", color = textColor, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun ContentOptionRow(
    icon: String,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val isDark = RabithohTheme.colors.isDark
    val textColor = if (enabled) (if (isDark) PutihBersih else SlateCharcoalText) else (if (isDark) DarkMuted else SlateMuted)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MerahMerdeka.copy(alpha = if (enabled) 0.12f else 0.06f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 14.sp)
        }
        Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}
