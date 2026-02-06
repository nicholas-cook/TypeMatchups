package com.souvenotes.typematchups

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvenotes.typematchups.ui.theme.TypeMatchupsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ) { false })
        setContent {
            val viewModel: TypeMatchupViewModel = hiltViewModel()
            val screenState by viewModel.typeMatchupScreenState.collectAsStateWithLifecycle()
            TypeMatchupsTheme {
                val cutoutInsets = WindowInsets.displayCutout.asPaddingValues()
                TypeMatchupScreen(
                    screenState = screenState,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.padding(
                        start = cutoutInsets.calculateStartPadding(LocalLayoutDirection.current),
                        end = cutoutInsets.calculateEndPadding(LocalLayoutDirection.current)
                    )
                )
            }
        }
    }
}