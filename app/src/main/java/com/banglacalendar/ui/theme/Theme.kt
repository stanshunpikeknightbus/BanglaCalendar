package com.banglacalendar.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val BanglaRed          = Color(0xFFC0392B)
val BanglaRedDark      = Color(0xFFFFB4AB)
val BanglaRedContainer = Color(0xFFFDECEA)
val BanglaGold         = Color(0xFFC0872F)
val SaturdayBlue       = Color(0xFF1565C0)
val SaturdayBlueDark   = Color(0xFF90CAF9)

private val LightColors = lightColorScheme(
    primary          = BanglaRed,
    onPrimary        = Color.White,
    primaryContainer = BanglaRedContainer,
    onPrimaryContainer = BanglaRed,
    secondary        = BanglaGold,
    onSecondary      = Color.White,
    background       = Color(0xFFFFFBFF),
    onBackground     = Color(0xFF1C1B1F),
    surface          = Color(0xFFFFFBFF),
    onSurface        = Color(0xFF1C1B1F),
    surfaceVariant   = Color(0xFFF3EDEC),
    onSurfaceVariant = Color(0xFF524443),
    outline          = Color(0xFFD8C2BF),
)

private val DarkColors = darkColorScheme(
    primary          = BanglaRedDark,
    onPrimary        = Color(0xFF690005),
    primaryContainer = Color(0xFF93000A),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary        = Color(0xFFFFB951),
    onSecondary      = Color(0xFF452B00),
    background       = Color(0xFF1C1B1F),
    onBackground     = Color(0xFFE6E1E5),
    surface          = Color(0xFF1C1B1F),
    onSurface        = Color(0xFFE6E1E5),
    surfaceVariant   = Color(0xFF2B2020),
    onSurfaceVariant = Color(0xFFD8C2BF),
    outline          = Color(0xFFA08C8A),
)

@Composable
fun BanglaCalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when {
        darkTheme -> DarkColors
        else      -> LightColors
    }
    MaterialTheme(colorScheme = colors, content = content)
}
