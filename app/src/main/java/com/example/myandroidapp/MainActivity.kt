package com.example.myandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myandroidapp.data.Building
import com.example.myandroidapp.data.Language
import com.example.myandroidapp.data.Service
import com.example.myandroidapp.ui.screens.BuildingDetailScreen
import com.example.myandroidapp.ui.screens.MainHubScreen
import com.example.myandroidapp.ui.screens.NavigationScreen
import com.example.myandroidapp.ui.theme.MyAndroidAppTheme

sealed interface Screen {
    object MainHub : Screen
    data class BuildingDetail(val building: Building) : Screen
    data class Navigation(val service: Service, val building: Building) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAndroidAppTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.MainHub) }
                var currentLanguage by remember { mutableStateOf(Language.ENGLISH) }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (val screen = currentScreen) {
                        is Screen.MainHub -> {
                            MainHubScreen(
                                currentLanguage = currentLanguage,
                                onLanguageChange = { currentLanguage = it },
                                onBuildingSelect = { currentScreen = Screen.BuildingDetail(it) }
                            )
                        }
                        is Screen.BuildingDetail -> {
                            BackHandler {
                                currentScreen = Screen.MainHub
                            }
                            BuildingDetailScreen(
                                building = screen.building,
                                currentLanguage = currentLanguage,
                                onBackClick = { currentScreen = Screen.MainHub },
                                onServiceSelect = { currentScreen = Screen.Navigation(it, screen.building) }
                            )
                        }
                        is Screen.Navigation -> {
                            BackHandler {
                                currentScreen = Screen.BuildingDetail(screen.building)
                            }
                            NavigationScreen(
                                service = screen.service,
                                currentLanguage = currentLanguage,
                                onBackClick = { currentScreen = Screen.BuildingDetail(screen.building) }
                            )
                        }
                    }
                }
            }
        }
    }
}