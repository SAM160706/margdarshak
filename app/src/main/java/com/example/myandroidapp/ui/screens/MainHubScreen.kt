package com.example.myandroidapp.ui.screens

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myandroidapp.data.Building
import com.example.myandroidapp.data.Language
import com.example.myandroidapp.data.SampleData

// Custom Spring-Scale Bounce modifier for premium tactile clicks
fun Modifier.bounceClick(onClick: () -> Unit = {}): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 350f),
        label = "bounceScale"
    )
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null, // removes the basic grey ripple
            onClick = onClick
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHubScreen(
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit,
    onBuildingSelect: (Building) -> Unit,
    onLogout: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredBuildings = remember(searchQuery, currentLanguage) {
        SampleData.buildings.filter { building ->
            building.name.get(currentLanguage).contains(searchQuery, ignoreCase = true) ||
            building.description.get(currentLanguage).contains(searchQuery, ignoreCase = true) ||
            building.departments.any { dept ->
                dept.name.get(currentLanguage).contains(searchQuery, ignoreCase = true) ||
                dept.services.any { service ->
                    service.name.get(currentLanguage).contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            // Premium Saffron-to-Gold Gradient Header
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
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (currentLanguage) {
                                Language.ENGLISH -> "Margdarshak"
                                Language.HINDI -> "मार्गदर्शक"
                                Language.MARATHI -> "मार्गदर्शक"
                            },
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = when (currentLanguage) {
                                Language.ENGLISH -> "Guiding you inside, step by step"
                                Language.HINDI -> "हर इमारत के अंदर आसान राह"
                                Language.MARATHI -> "प्रत्येक इमारतीत सोपी वाट"
                            },
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Logout button on top (no default M3 touch target padding to avoid boundary overlaps)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onLogout() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Compact custom Language Toggle below
                        LanguageToggle(
                            currentLanguage = currentLanguage,
                            onLanguageChange = onLanguageChange,
                            compact = true
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar with Glassmorphic visual outline
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = when (currentLanguage) {
                            Language.ENGLISH -> "Search buildings, services..."
                            Language.HINDI -> "भवन, सेवा खोजें..."
                            Language.MARATHI -> "इमारती, सेवा शोधा..."
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = when (currentLanguage) {
                    Language.ENGLISH -> "Select Building"
                    Language.HINDI -> "भवन का चयन करें"
                    Language.MARATHI -> "इमारत निवडा"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredBuildings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.ENGLISH -> "No buildings found."
                            Language.HINDI -> "कोई भवन नहीं मिला।"
                            Language.MARATHI -> "कोणतीही इमारत आढळली नाही."
                        },
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredBuildings) { building ->
                        BuildingCard(
                            building = building,
                            lang = currentLanguage,
                            onClick = { onBuildingSelect(building) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageToggle(
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val options = listOf(Language.ENGLISH, Language.HINDI, Language.MARATHI)
    val selectedIndex = options.indexOf(currentLanguage)

    val width = if (compact) 138.dp else 176.dp
    val height = if (compact) 30.dp else 38.dp
    val pillWidth = if (compact) 44.dp else 56.dp
    val cornerRadius = if (compact) 15.dp else 19.dp
    val pillCornerRadius = if (compact) 12.dp else 16.dp
    val fontSize = if (compact) 9.sp else 11.sp

    val transition = updateTransition(targetState = selectedIndex, label = "langToggle")
    val slideOffset by transition.animateDp(
        transitionSpec = { spring(dampingRatio = 0.8f, stiffness = 300f) },
        label = "pillOffset"
    ) { index ->
        if (compact) {
            when (index) {
                0 -> 0.dp
                1 -> 44.dp
                else -> 88.dp
            }
        } else {
            when (index) {
                0 -> 0.dp
                1 -> 56.dp
                else -> 112.dp
            }
        }
    }

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(cornerRadius))
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Sliding Highlight Pill
        Box(
            modifier = Modifier
                .offset(x = slideOffset)
                .width(pillWidth)
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(pillCornerRadius))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onLanguageChange(Language.ENGLISH) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "EN",
                    color = if (currentLanguage == Language.ENGLISH) Color(0xFFE65100) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onLanguageChange(Language.HINDI) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "हिंदी",
                    color = if (currentLanguage == Language.HINDI) Color(0xFFE65100) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onLanguageChange(Language.MARATHI) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "मराठी",
                    color = if (currentLanguage == Language.MARATHI) Color(0xFFE65100) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )
            }
        }
    }
}

@Composable
fun BuildingCard(
    building: Building,
    lang: Language,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val deptCount = building.departments.size
    val serviceCount = building.departments.sumOf { it.services.size }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(onClick = onClick), // spring bouncing micro-interaction
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            if (building.drawableName.isNotEmpty()) {
                val resourceId = remember(building.drawableName) {
                    context.resources.getIdentifier(building.drawableName, "drawable", context.packageName)
                }
                if (resourceId != 0) {
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = building.name.get(lang),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = building.name.get(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Proceed",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = building.description.get(lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    modifier = Modifier.height(32.dp) // Fixed height to keep grids aligned
                )

                Spacer(modifier = Modifier.height(8.dp))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Address",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(12.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(2.dp))
                    
                    Text(
                        text = building.address.get(lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (lang) {
                            Language.ENGLISH -> "$deptCount Depts • $serviceCount Services"
                            Language.HINDI -> "$deptCount विभाग • $serviceCount सेवाएं"
                            Language.MARATHI -> "$deptCount विभाग • $serviceCount सेवा"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
