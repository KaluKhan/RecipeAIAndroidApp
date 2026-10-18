package com.sankhlatraders.recipeai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    primaryContainer = BrandOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = BrandGold,
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF3D2B00),
    onSecondaryContainer = BrandGoldLight,
    tertiary = AccentBlue,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF444444),
    error = AccentRed,
    onError = Color.White,
    inverseSurface = Color.White,
    inverseOnSurface = DarkBackground,
    inversePrimary = BrandOrangeDark,
    scrim = Color(0x99000000)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0D0),
    onPrimaryContainer = Color(0xFF3D1100),
    secondary = Color(0xFFB85C00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDCC2),
    onSecondaryContainer = Color(0xFF2D1600),
    tertiary = AccentBlue,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFCCCCCC),
    error = AccentRed,
    onError = Color.White
)

@Composable
fun RecipeAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}