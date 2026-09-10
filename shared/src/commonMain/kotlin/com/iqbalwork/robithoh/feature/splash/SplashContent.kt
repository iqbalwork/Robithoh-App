package com.iqbalwork.robithoh.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.iqbalwork.robithoh.core.designsystem.theme.EmasKhidmat
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMarunGelap
import com.iqbalwork.robithoh.core.designsystem.theme.MerahMerdeka
import com.iqbalwork.robithoh.core.designsystem.theme.PaperBackgroundLight
import com.iqbalwork.robithoh.core.designsystem.theme.RabithohTheme
import com.iqbalwork.robithoh.core.designsystem.theme.TextCharcoal
import com.iqbalwork.robithoh.core.designsystem.theme.getPlusJakartaSansFontFamily
import robithohapp.shared.generated.resources.Res
import robithohapp.shared.generated.resources.bg_splash
import robithohapp.shared.generated.resources.ic_app_launcher

@Composable
fun SplashContent(
    alpha: Float = 1f,
    scale: Float = 1f,
    modifier: Modifier = Modifier
) {
    val fontJakarta = getPlusJakartaSansFontFamily()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBackgroundLight)
    ) {
        // 1. Background Pattern
        Image(
            painter = painterResource(Res.drawable.bg_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.85f)
        )

        // Soft gradient overlay for smooth visual hierarchy
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            PaperBackgroundLight.copy(alpha = 0.4f),
                            PaperBackgroundLight.copy(alpha = 0.2f),
                            PaperBackgroundLight.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // 2. Center Content (Logo, App Title & Tagline)
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .alpha(alpha)
                .scale(scale),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Launcher Icon
            Surface(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                shape = CircleShape,
                color = Color.Transparent,
                shadowElevation = 8.dp
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_app_launcher),
                    contentDescription = "Logo Robithoh",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main App Name
            Text(
                text = "ROBITHOH",
                fontFamily = fontJakarta,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                color = MerahMarunGelap,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = "AMALIYAH MURSYID",
                fontFamily = fontJakarta,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = EmasKhidmat,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tagline
            Text(
                text = "Berkah Semuanya Segalanya Selamanya",
                fontFamily = fontJakarta,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = TextCharcoal.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Subtle loading indicator
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MerahMerdeka,
                strokeWidth = 2.5.dp
            )
        }

        // 3. Bottom Footer (Sirnarasa identifier)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Roudloh Merah Putih",
                fontFamily = fontJakarta,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MerahMerdeka,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "MTQN Suryalaya Sirnarasa PPKN III",
                fontFamily = fontJakarta,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextCharcoal.copy(alpha = 0.75f),
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Ciceuri - Panjalu - Ciamis",
                fontFamily = fontJakarta,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = TextCharcoal.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview
@Composable
private fun SplashContentPreview() {
    RabithohTheme(darkTheme = false) {
        SplashContent()
    }
}
