package com.galeriku.app.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColors = darkColorScheme(
    primary = AccentBlue,
    onPrimary = PhotosBlack,
    background = PhotosBlack,
    onBackground = androidx.compose.ui.graphics.Color.White,
    surface = PhotosSurfaceDark,
    onSurface = androidx.compose.ui.graphics.Color.White,
    surfaceVariant = PhotosSurfaceDark2,
    outline = PhotosBorderDark,
    error = AccentRed
)

private val LightColors = lightColorScheme(
    primary = AccentBlue,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    background = PhotosBackgroundLight,
    onBackground = androidx.compose.ui.graphics.Color.Black,
    surface = PhotosSurfaceLight,
    onSurface = androidx.compose.ui.graphics.Color.Black,
    surfaceVariant = PhotosSurfaceLight2,
    outline = PhotosBorderLight,
    error = AccentRed
)

@Composable
fun GaleriKuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GaleriTypography,
        content = content
    )
}
