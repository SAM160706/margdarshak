package com.example.myandroidapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myandroidapp.data.Language
import com.example.myandroidapp.data.Service
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    service: Service,
    currentLanguage: Language,
    onBackClick: () -> Unit
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    // Auto-play steps simulation
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                delay(4000)
                if (currentStepIndex < service.routeSteps.lastIndex) {
                    currentStepIndex++
                } else {
                    currentStepIndex = 0
                }
            }
        }
    }

    val currentStep = service.routeSteps[currentStepIndex]

    Scaffold(
        topBar = {
            // Saffron-to-Gold Gradient Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE65100), // Deep Saffron
                                Color(0xFFFF8F00)  // Warm Gold
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = service.name.get(currentLanguage),
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            
            // Redesigned Light Map Visualizer Area (White/Slate Blueprint Theme)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color(0xFFF8FAFC)) // Clean off-white background
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                // Interactive Route Path Drawing with Radar Pulse Dot
                RouteVisualizerMap(
                    currentStepIndex = currentStepIndex,
                    totalSteps = service.routeSteps.size,
                    modifier = Modifier.fillMaxSize()
                )

                // Direction Indicator HUD Overlay (Light Theme Style)
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getDirectionArrow(currentStep.directionIcon),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (currentLanguage) {
                                Language.ENGLISH -> "Step ${currentStepIndex + 1}/${service.routeSteps.size}"
                                Language.HINDI -> "चरण ${currentStepIndex + 1}/${service.routeSteps.size}"
                                Language.MARATHI -> "टप्पा ${currentStepIndex + 1}/${service.routeSteps.size}"
                            },
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Control Bar Overlay (Light Glassmorphic Style)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Button
                        Button(
                            onClick = {
                                if (currentStepIndex > 0) currentStepIndex--
                                isPlaying = false
                            },
                            enabled = currentStepIndex > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledContentColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = when (currentLanguage) {
                                    Language.ENGLISH -> "Prev"
                                    Language.HINDI -> "पीछे"
                                    Language.MARATHI -> "मागे"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        // Play/Pause Button
                        Button(
                            onClick = { isPlaying = !isPlaying },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (isPlaying) {
                                    when (currentLanguage) {
                                        Language.ENGLISH -> "Pause ⏸"
                                        Language.HINDI -> "रोकें ⏸"
                                        Language.MARATHI -> "थांबा ⏸"
                                    }
                                } else {
                                    when (currentLanguage) {
                                        Language.ENGLISH -> "Play ▶"
                                        Language.HINDI -> "चलाएं ▶"
                                        Language.MARATHI -> "सुरू ▶"
                                    }
                                },
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }

                        // Next Button
                        Button(
                            onClick = {
                                if (currentStepIndex < service.routeSteps.lastIndex) {
                                    currentStepIndex++
                                }
                                isPlaying = false
                            },
                            enabled = currentStepIndex < service.routeSteps.lastIndex,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledContentColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = when (currentLanguage) {
                                    Language.ENGLISH -> "Next"
                                    Language.HINDI -> "आगे"
                                    Language.MARATHI -> "पुढे"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Detail Card (Frosted Glassmorphic layout)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFE65100), Color(0xFFFF8F00))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getDirectionArrow(currentStep.directionIcon),
                            fontSize = 26.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = currentStep.title.get(currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentStep.description.get(currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable List of Route Steps with spring-scale bounces
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(service.routeSteps) { index, step ->
                    val isSelected = index == currentStepIndex
                    val itemBgColor by animateColorAsState(
                        targetValue = if (isSelected)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        else
                            MaterialTheme.colorScheme.surface,
                        label = "cardBgColor"
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick {
                                currentStepIndex = index
                                isPlaying = false
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = itemBgColor),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.05f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 2.dp else 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${step.stepNumber}",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.width(28.dp)
                            )

                            Text(
                                text = getDirectionArrow(step.directionIcon),
                                fontSize = 20.sp,
                                modifier = Modifier.width(36.dp),
                                textAlign = TextAlign.Center
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = step.title.get(currentLanguage),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RouteVisualizerMap(
    currentStepIndex: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = Color.White
    
    // Infinite Animation for Sonar radar pulses around location pin
    val infiniteTransition = rememberInfiniteTransition(label = "radarPulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 10.dp.value,
        targetValue = 32.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Blueprint theme layout lines (crisp light-grey coordinates)
        val blueprintLineColor = Color(0xFFF1F5F9)
        val roomOutlineColor = Color(0xFFCBD5E1)
        val roomFillColor = Color(0xFFF8FAFC)

        // Draw blueprint grid lines
        val gridSpacing = 24.dp.toPx()
        var x = 0f
        while (x < width) {
            drawLine(
                color = blueprintLineColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1.dp.toPx()
            )
            x += gridSpacing
        }
        var y = 0f
        while (y < height) {
            drawLine(
                color = blueprintLineColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
            y += gridSpacing
        }

        // Draw corridor outlines
        drawRect(
            color = roomOutlineColor.copy(alpha = 0.15f),
            topLeft = Offset(width * 0.15f, height * 0.38f),
            size = Size(width * 0.7f, height * 0.24f)
        )
        drawLine(
            color = roomOutlineColor,
            start = Offset(width * 0.15f, height * 0.38f),
            end = Offset(width * 0.85f, height * 0.38f),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = roomOutlineColor,
            start = Offset(width * 0.15f, height * 0.62f),
            end = Offset(width * 0.85f, height * 0.62f),
            strokeWidth = 2.dp.toPx()
        )

        // Draw individual rooms
        for (i in 0..3) {
            // Room Fill
            drawRect(
                color = roomFillColor,
                topLeft = Offset(width * (0.22f + i * 0.15f), height * 0.16f),
                size = Size(width * 0.08f, height * 0.22f)
            )
            // Room Border
            drawRect(
                color = roomOutlineColor,
                topLeft = Offset(width * (0.22f + i * 0.15f), height * 0.16f),
                size = Size(width * 0.08f, height * 0.22f),
                style = Stroke(width = 1.5.dp.toPx())
            )
            
            // Bottom Rooms Fill
            drawRect(
                color = roomFillColor,
                topLeft = Offset(width * (0.22f + i * 0.15f), height * 0.62f),
                size = Size(width * 0.08f, height * 0.22f)
            )
            // Bottom Rooms Border
            drawRect(
                color = roomOutlineColor,
                topLeft = Offset(width * (0.22f + i * 0.15f), height * 0.62f),
                size = Size(width * 0.08f, height * 0.22f),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
        
        // Checkpoints Coordinates
        val checkpoints = listOf(
            Offset(width * 0.1f, height * 0.5f),    // Entrance
            Offset(width * 0.35f, height * 0.5f),   // Lobby
            Offset(width * 0.55f, height * 0.5f),   // Corridor center
            Offset(width * 0.55f, height * 0.27f),  // Room inside
            Offset(width * 0.75f, height * 0.5f),   // End of corridor
            Offset(width * 0.75f, height * 0.73f)   // Ground Floor Counter
        )

        // Draw dotted route path line (static background)
        for (j in 0 until checkpoints.lastIndex) {
            drawLine(
                color = Color(0xFFCBD5E1),
                start = checkpoints[j],
                end = checkpoints[j + 1],
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Draw active traversed route path (colored Saffron gradient)
        val maxSegmentIndex = minOf(currentStepIndex + 1, checkpoints.lastIndex)
        for (k in 0 until maxSegmentIndex) {
            drawLine(
                color = primaryColor,
                start = checkpoints[k],
                end = checkpoints[k + 1],
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        
        // Draw static path nodes
        for (j in 0 until minOf(totalSteps + 1, checkpoints.size)) {
            drawCircle(
                color = if (j <= currentStepIndex + 1) primaryColor else Color(0xFF94A3B8),
                radius = 5.dp.toPx(),
                center = checkpoints[j]
            )
        }

        // Draw the location pin and animated sonar pulse radar ripple
        if (currentStepIndex + 1 < checkpoints.size) {
            val currentPos = checkpoints[currentStepIndex + 1]

            // 1. Radar Sonar pulse ripple
            drawCircle(
                color = primaryColor.copy(alpha = pulseAlpha),
                radius = pulseRadius,
                center = currentPos
            )

            // 2. Solid core dot
            drawCircle(
                color = primaryColor,
                radius = 9.dp.toPx(),
                center = currentPos
            )
            drawCircle(
                color = secondaryColor,
                radius = 4.dp.toPx(),
                center = currentPos
            )
        }
    }
}

private fun getDirectionArrow(icon: String): String {
    return when (icon) {
        "straight" -> "⬆️"
        "right" -> "➡️"
        "left" -> "⬅️"
        "up_stairs" -> "↗️"
        "elevator" -> "🛗"
        else -> "📍"
    }
}
