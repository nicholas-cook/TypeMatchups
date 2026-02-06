package com.souvenotes.typematchups.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val EkansDarkColorScheme = darkColorScheme(
    primary = EkansLightPurple, // Light Purple
    onPrimary = Color(0xFF000000), // Black for text/icons on primary
    primaryContainer = Color(0xFF6A0DAD), // Darker shade of Royal Purple
    onPrimaryContainer = Color(0xFFD8BFD8), // Lighter shade of Light Purple

    secondary = Color(0xFFFFE135), // Brighter Golden Yellow
    onSecondary = Color(0xFF000000), // Black for text/icons on secondary
    secondaryContainer = Color(0xFFB8860B), // Darker Golden Yellow
    onSecondaryContainer = EkansLightYellow, // Pale Yellow

    tertiary = Color(0xFF9370DB), // Medium Purple
    onTertiary = Color(0xFF000000), // Black for text/icons on tertiary
    tertiaryContainer = EkansDarkPurple, // Dark Purple
    onTertiaryContainer = Color(0xFFE6E6FA), // Lavender

    error = Color(0xFFCF6679), // Standard dark theme error color
    onError = Color(0xFF000000), // Black for text/icons on error
    errorContainer = Color(0xFFB00020), // Darker error color
    onErrorContainer = Color(0xFFFFFFFF), // White for text on error container

    // Modified background colors
    background = Color(0xFF2A0044), // Dark Purple background
    onBackground = Color(0xFFFFFFFF), // White for text/icons on background

    surface = Color(0xFF2A0044), // Matching surface color
    onSurface = Color(0xFFFFFFFF), // White for text/icons on surface

    outline = Color(0xFF938F99), // Lighter outline for visibility

    surfaceVariant = Color(0xFF3B0066), // Slightly lighter purple for variety
    onSurfaceVariant = Color(0xFFDFD8E3), // Light gray for text on variant surfaces

    scrim = Color(0xFF000000) // Fully opaque black for scrim
)

private val EkansLightColorScheme = lightColorScheme(
    primary = EkansRoyalPurple, // Royal Purple
    onPrimary = Color(0xFFFFFFFF), // White for text/icons on primary
    primaryContainer = EkansLightPurple, // Light Purple
    onPrimaryContainer = EkansDarkPurple, // Dark Purple

    secondary = EkansDarkYellow, // Golden Yellow
    onSecondary = Color(0xFF000000), // Black for text/icons on secondary
    secondaryContainer = EkansLightYellow, // Pale Yellow
    onSecondaryContainer = EkansDarkPurple, // Dark Purple

    tertiary = EkansDarkPurple, // Dark Purple
    onTertiary = Color(0xFFFFFFFF), // White for text/icons on tertiary
    tertiaryContainer = EkansLightPurple, // Light Purple
    onTertiaryContainer = Color(0xFF000000), // Black

    error = Color(0xFFB00020), // Standard error color
    onError = Color(0xFFFFFFFF), // White for text/icons on error
    errorContainer = Color(0xFFFCD7D7), // Light pink for error container
    onErrorContainer = Color(0xFF621B16), // Dark red for text on error container

    background = EkansLightPurple, // Light Purple as main background
    onBackground = Color(0xFF000000), // Black for text/icons on background

    surface = EkansLightPurple, // Light Purple as surface color
    onSurface = Color(0xFF000000), // Black for text/icons on surface

    outline = EkansDarkPurple, // Dark Purple for outlines

    surfaceVariant = Color(0xFFF4F0FF), // Very light purple for variant surfaces
    onSurfaceVariant = EkansDarkPurple, // Dark Purple for text on variant surfaces

    scrim = Color(0x52000000) // Semi-transparent black for scrim
)

@Composable
fun TypeMatchupsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> EkansLightColorScheme
        else -> EkansLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}