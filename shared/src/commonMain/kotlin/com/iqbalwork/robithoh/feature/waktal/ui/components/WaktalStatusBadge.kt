package com.iqbalwork.robithoh.feature.waktal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.feature.waktal.domain.WaktalStatus

@Composable
fun WaktalStatusBadge(
    status: WaktalStatus,
    tahunWafat: Int? = null,
    modifier: Modifier = Modifier
) {
    val isDark = RabithohTheme.colors.isDark

    val (bgColor, textColor, labelText) = when (status) {
        WaktalStatus.AKTIF -> Triple(
            if (isDark) Color(0xFF14532D) else Color(0xFFDCFCE7),
            if (isDark) Color(0xFF86EFAC) else Color(0xFF15803D),
            "Aktif"
        )
        WaktalStatus.WAFAT -> {
            val label = if (tahunWafat != null) "Almarhum (w. $tahunWafat)" else "Almarhum"
            Triple(
                if (isDark) Color(0xFF334155) else Color(0xFFF1F5F9),
                if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                label
            )
        }
        WaktalStatus.SEMUA -> Triple(
            if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0),
            if (isDark) Color(0xFFE2E8F0) else Color(0xFF334155),
            "Semua"
        )
    }

    Box(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = labelText,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
