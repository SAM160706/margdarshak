package com.example.myandroidapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myandroidapp.data.*
import com.example.myandroidapp.ui.theme.AbsentRed
import com.example.myandroidapp.ui.theme.BusyAmber
import com.example.myandroidapp.ui.theme.PresentGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingDetailScreen(
    building: Building,
    currentLanguage: Language,
    onBackClick: () -> Unit,
    onServiceSelect: (Service) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        BilingualText("Services", "सेवाएं", "सेवा"),
        BilingualText("Officers", "अधिकारी", "अधिकारी")
    )

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
                        text = building.name.get(currentLanguage),
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
            if (building.drawableName.isNotEmpty()) {
                val context = LocalContext.current
                val resourceId = remember(building.drawableName) {
                    context.resources.getIdentifier(building.drawableName, "drawable", context.packageName)
                }
                if (resourceId != 0) {
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = building.name.get(currentLanguage),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Outdoor Directions Card (Triggers free turn-by-turn navigation)
            OutdoorMapViewCard(
                building = building,
                lang = currentLanguage
            )

            Spacer(modifier = Modifier.height(14.dp))
            
            // Custom Sliding Pill Tab Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                CustomTabSelector(
                    selectedTab = selectedTab,
                    onTabSelect = { selectedTab = it },
                    tabs = tabs,
                    lang = currentLanguage
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedTab) {
                0 -> ServicesTab(
                    departments = building.departments,
                    lang = currentLanguage,
                    onServiceSelect = onServiceSelect
                )
                1 -> OfficersTab(
                    departments = building.departments,
                    lang = currentLanguage
                )
            }
        }
    }
}

@Composable
fun OutdoorMapViewCard(
    building: Building,
    lang: Language,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Visual Map Indicator Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📍",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (lang) {
                        Language.ENGLISH -> "Get Directions"
                        Language.HINDI -> "दिशानिर्देश प्राप्त करें"
                        Language.MARATHI -> "दिशा-निर्देश मिळवा"
                    },
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = building.address.get(lang),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Button to open Native Google Maps App for Voice Navigation (100% Free)
            Button(
                onClick = {
                    val gmmIntentUri = Uri.parse("google.navigation:q=${building.latitude},${building.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                        setPackage("com.google.android.apps.maps")
                    }
                    try {
                        context.startActivity(mapIntent)
                    } catch (e: Exception) {
                        // Fallback to web browser maps
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=${building.latitude},${building.longitude}")
                        )
                        context.startActivity(webIntent)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = when (lang) {
                        Language.ENGLISH -> "Directions"
                        Language.HINDI -> "दिशानिर्देश"
                        Language.MARATHI -> "दिशा-निर्देश"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun CustomTabSelector(
    selectedTab: Int,
    onTabSelect: (Int) -> Unit,
    tabs: List<BilingualText>,
    lang: Language,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = selectedTab, label = "tabTransition")
    val slideOffset by transition.animateDp(
        transitionSpec = { spring(dampingRatio = 0.85f, stiffness = 300f) },
        label = "tabOffset"
    ) { index ->
        if (index == 0) 0.dp else 164.dp
    }

    Box(
        modifier = modifier
            .width(336.dp)
            .height(46.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(23.dp))
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Sliding Highlight Pill
        Box(
            modifier = Modifier
                .offset(x = slideOffset)
                .width(164.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.get(lang),
                        color = if (selectedTab == index) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ServicesTab(
    departments: List<Department>,
    lang: Language,
    onServiceSelect: (Service) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
    ) {
        items(departments) { dept ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Department Title
                    Text(
                        text = dept.name.get(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    dept.services.forEach { service ->
                        ServiceItem(
                            service = service,
                            lang = lang,
                            onServiceSelect = onServiceSelect
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceItem(
    service: Service,
    lang: Language,
    onServiceSelect: (Service) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.name.get(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when (lang) {
                            Language.ENGLISH -> "Est. Time: ${service.estimatedMinutes} mins"
                            Language.HINDI -> "अनुमानित समय: ${service.estimatedMinutes} मिनट"
                            Language.MARATHI -> "अंदाजे वेळ: ${service.estimatedMinutes} मिनिटे"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 14.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = service.description.get(lang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Glassmorphic Required Documents Panel
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = when (lang) {
                                        Language.ENGLISH -> "Required Documents Checklist"
                                        Language.HINDI -> "आवश्यक दस्तावेजों की सूची"
                                        Language.MARATHI -> "आवश्यक कागदपत्रांची यादी"
                                    },
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            service.documentsRequired.forEach { doc ->
                                Row(
                                    modifier = Modifier.padding(bottom = 6.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "✓ ",
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = doc.get(lang),
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Navigation button
                    Button(
                        onClick = { onServiceSelect(service) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(
                            text = when (lang) {
                                Language.ENGLISH -> "Start Route Navigation"
                                Language.HINDI -> "मार्गदर्शन शुरू करें"
                                Language.MARATHI -> "मार्गदर्शन सुरू करा"
                            },
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OfficersTab(
    departments: List<Department>,
    lang: Language
) {
    var searchQuery by remember { mutableStateOf("") }

    val allOfficers = remember(departments) {
        departments.flatMap { dept -> 
            dept.officers.map { officer -> Pair(dept.name, officer) } 
        }
    }

    // Filter officers dynamically based on query
    val filteredOfficers = remember(allOfficers, searchQuery, lang) {
        allOfficers.filter { (_, officer) ->
            officer.name.get(lang).contains(searchQuery, ignoreCase = true) ||
            officer.designation.get(lang).contains(searchQuery, ignoreCase = true) ||
            officer.room.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar for Officers
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = {
                Text(
                    text = when (lang) {
                        Language.ENGLISH -> "Search officer name, designation, room..."
                        Language.HINDI -> "अधिकारी का नाम, पद, कक्ष खोजें..."
                        Language.MARATHI -> "अधिकारी नाव, पद, खोली शोधा..."
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
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (filteredOfficers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (lang) {
                        Language.ENGLISH -> "No officers found."
                        Language.HINDI -> "कोई अधिकारी नहीं मिला।"
                        Language.MARATHI -> "कोणतेही अधिकारी आढळले नाहीत."
                    },
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredOfficers) { (deptName, officer) ->
                    OfficerCard(
                        deptName = deptName.get(lang),
                        officer = officer,
                        lang = lang
                    )
                }
            }
        }
    }
}

@Composable
fun OfficerCard(
    deptName: String,
    officer: Officer,
    lang: Language
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rounded Profile Image or Letter Avatar Fallback
                val profileResourceId = if (officer.drawableName.isNotEmpty()) {
                    context.resources.getIdentifier(officer.drawableName, "drawable", context.packageName)
                } else 0

                if (profileResourceId != 0) {
                    Image(
                        painter = painterResource(id = profileResourceId),
                        contentDescription = officer.name.get(lang),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val initial = officer.name.english.firstOrNull()?.toString() ?: "O"
                        Text(
                            text = initial,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = officer.name.get(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = officer.designation.get(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = deptName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Presence badge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val badgeColor = when (officer.status) {
                        PresenceStatus.PRESENT -> PresentGreen
                        PresenceStatus.MEETING -> BusyAmber
                        PresenceStatus.ABSENT -> AbsentRed
                    }
                    
                    val statusText = when (officer.status) {
                        PresenceStatus.PRESENT -> when (lang) {
                            Language.ENGLISH -> "Available"
                            Language.HINDI -> "उपलब्ध"
                            Language.MARATHI -> "उपलब्ध"
                        }
                        PresenceStatus.MEETING -> when (lang) {
                            Language.ENGLISH -> "In Meeting"
                            Language.HINDI -> "बैठक में"
                            Language.MARATHI -> "बैठकीत"
                        }
                        PresenceStatus.ABSENT -> when (lang) {
                            Language.ENGLISH -> "Unavailable"
                            Language.HINDI -> "अनुपलब्ध"
                            Language.MARATHI -> "अनुपलब्ध"
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(badgeColor)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = statusText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(12.dp))

            // Location Text (Separate full-width line to prevent button collision)
            Text(
                text = "${when (lang) {
                    Language.ENGLISH -> "Location"
                    Language.HINDI -> "स्थान"
                    Language.MARATHI -> "स्थान"
                }}: ${officer.room}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Row for Contact Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Call Button
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:01123456789") // Mock phone
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Officer",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Email Button
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:officer@example.gov") // Mock email
                            putExtra(Intent.EXTRA_SUBJECT, "Query regarding ${deptName}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Officer",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
