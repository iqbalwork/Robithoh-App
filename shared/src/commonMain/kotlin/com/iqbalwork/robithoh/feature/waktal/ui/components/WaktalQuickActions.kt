package com.iqbalwork.robithoh.feature.waktal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WaktalQuickActions(
    hasPhone: Boolean,
    hasWhatsapp: Boolean,
    hasCoordinates: Boolean,
    onDialPhone: () -> Unit,
    onOpenWhatsApp: () -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (hasWhatsapp) {
            Button(
                onClick = onOpenWhatsApp,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25D366),
                    contentColor = Color.White
                )
            ) {
                Text(text = "💬 WhatsApp", fontSize = 12.sp)
            }
        }

        if (hasPhone) {
            OutlinedButton(
                onClick = onDialPhone,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "📞 Telepon", fontSize = 12.sp)
            }
        }

        if (hasCoordinates) {
            OutlinedButton(
                onClick = onOpenMap,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "📍 Peta", fontSize = 12.sp)
            }
        }
    }
}
