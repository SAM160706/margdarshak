package com.example.myandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myandroidapp.data.Building
import com.example.myandroidapp.data.Language
import com.example.myandroidapp.data.Service
import com.example.myandroidapp.ui.screens.BuildingDetailScreen
import com.example.myandroidapp.ui.screens.MainHubScreen
import com.example.myandroidapp.ui.screens.NavigationScreen
import com.example.myandroidapp.ui.theme.MyAndroidAppTheme
import kotlinx.coroutines.delay

sealed interface Screen {
    object Splash : Screen
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
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
                var currentLanguage by remember { mutableStateOf(Language.ENGLISH) }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (val screen = currentScreen) {
                        is Screen.Splash -> {
                            SplashScreen(onTimeout = { currentScreen = Screen.MainHub })
                        }
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

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onTimeout()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9F2), // Light Saffron-Peach bg
                        Color(0xFFFFE0B2), // Slightly darker warm peach
                        Color(0xFFFFCC80)  // Saffron gold
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_margdarshak),
                contentDescription = "Margdarshak Logo",
                modifier = Modifier
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .alpha(alpha)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Margdarshak",
                fontWeight = FontWeight.Black,
                fontSize = 36.sp,
                color = Color(0xFFE65100),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "मार्गदर्शक",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFFFF8F00),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Guiding you inside, step by step",
                fontSize = 14.sp,
                color = Color.DarkGray.copy(alpha = 0.8f),
                fontStyle = FontStyle.Italic
            )
        }
    }
}