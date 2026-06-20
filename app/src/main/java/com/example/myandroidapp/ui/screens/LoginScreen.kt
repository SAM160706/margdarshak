package com.example.myandroidapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myandroidapp.R
import com.example.myandroidapp.data.Language
import com.google.firebase.auth.FirebaseAuth

enum class LoginStringKey {
    TITLE,
    SUBTITLE_LOGIN,
    SUBTITLE_SIGNUP,
    EMAIL_LABEL,
    EMAIL_PLACEHOLDER,
    PASSWORD_LABEL,
    PASSWORD_PLACEHOLDER,
    CONFIRM_PASSWORD_LABEL,
    CONFIRM_PASSWORD_PLACEHOLDER,
    LOGIN_BUTTON,
    SIGNUP_BUTTON,
    SWITCH_TO_SIGNUP,
    SWITCH_TO_LOGIN,
    ERROR_INVALID_EMAIL,
    ERROR_SHORT_PASSWORD,
    ERROR_MATCH_PASSWORD,
    SUCCESS_LOGIN,
    SUCCESS_REGISTER,
    FORGOT_PASSWORD,
    RESET_TITLE,
    RESET_DESC,
    RESET_LINK_SENT,
    CANCEL,
    SEND_LINK
}

fun getLoginString(key: LoginStringKey, lang: Language): String {
    return when (lang) {
        Language.ENGLISH -> when (key) {
            LoginStringKey.TITLE -> "Margdarshak Login"
            LoginStringKey.SUBTITLE_LOGIN -> "Enter your credentials to access Margdarshak"
            LoginStringKey.SUBTITLE_SIGNUP -> "Create an account to access Margdarshak"
            LoginStringKey.EMAIL_LABEL -> "Email Address"
            LoginStringKey.EMAIL_PLACEHOLDER -> "yourname@email.com"
            LoginStringKey.PASSWORD_LABEL -> "Password"
            LoginStringKey.PASSWORD_PLACEHOLDER -> "Enter password"
            LoginStringKey.CONFIRM_PASSWORD_LABEL -> "Confirm Password"
            LoginStringKey.CONFIRM_PASSWORD_PLACEHOLDER -> "Confirm your password"
            LoginStringKey.LOGIN_BUTTON -> "Login"
            LoginStringKey.SIGNUP_BUTTON -> "Sign Up"
            LoginStringKey.SWITCH_TO_SIGNUP -> "Don't have an account? Sign Up"
            LoginStringKey.SWITCH_TO_LOGIN -> "Already have an account? Login"
            LoginStringKey.ERROR_INVALID_EMAIL -> "Please enter a valid email address"
            LoginStringKey.ERROR_SHORT_PASSWORD -> "Password must be at least 6 characters"
            LoginStringKey.ERROR_MATCH_PASSWORD -> "Passwords do not match"
            LoginStringKey.SUCCESS_LOGIN -> "Login Successful!"
            LoginStringKey.SUCCESS_REGISTER -> "Account Created Successfully!"
            LoginStringKey.FORGOT_PASSWORD -> "Forgot Password?"
            LoginStringKey.RESET_TITLE -> "Reset Password"
            LoginStringKey.RESET_DESC -> "Enter your email address to receive a password reset link."
            LoginStringKey.RESET_LINK_SENT -> "Reset link sent to your email!"
            LoginStringKey.CANCEL -> "Cancel"
            LoginStringKey.SEND_LINK -> "Send Link"
        }
        Language.HINDI -> when (key) {
            LoginStringKey.TITLE -> "मार्गदर्शक लॉगिन"
            LoginStringKey.SUBTITLE_LOGIN -> "मार्गदर्शक का उपयोग करने के लिए अपने क्रेडेंशियल दर्ज करें"
            LoginStringKey.SUBTITLE_SIGNUP -> "मार्गदर्शक का उपयोग करने के लिए एक खाता बनाएं"
            LoginStringKey.EMAIL_LABEL -> "ईमेल पता"
            LoginStringKey.EMAIL_PLACEHOLDER -> "yourname@email.com"
            LoginStringKey.PASSWORD_LABEL -> "पासवर्ड"
            LoginStringKey.PASSWORD_PLACEHOLDER -> "पासवर्ड दर्ज करें"
            LoginStringKey.CONFIRM_PASSWORD_LABEL -> "पासवर्ड की पुष्टि करें"
            LoginStringKey.CONFIRM_PASSWORD_PLACEHOLDER -> "अपने पासवर्ड की पुष्टि करें"
            LoginStringKey.LOGIN_BUTTON -> "लॉगिन"
            LoginStringKey.SIGNUP_BUTTON -> "साइन अप करें"
            LoginStringKey.SWITCH_TO_SIGNUP -> "खाता नहीं है? साइन अप करें"
            LoginStringKey.SWITCH_TO_LOGIN -> "पहले से ही खाता है? लॉगिन करें"
            LoginStringKey.ERROR_INVALID_EMAIL -> "कृपया एक वैध ईमेल पता दर्ज करें"
            LoginStringKey.ERROR_SHORT_PASSWORD -> "पासवर्ड कम से कम 6 अक्षरों का होना चाहिए"
            LoginStringKey.ERROR_MATCH_PASSWORD -> "पासवर्ड मेल नहीं खाते"
            LoginStringKey.SUCCESS_LOGIN -> "लॉगिन सफल रहा!"
            LoginStringKey.SUCCESS_REGISTER -> "खाता सफलतापूर्वक बनाया गया!"
            LoginStringKey.FORGOT_PASSWORD -> "पासवर्ड भूल गए?"
            LoginStringKey.RESET_TITLE -> "पासवर्ड रीसेट करें"
            LoginStringKey.RESET_DESC -> "पासवर्ड रीसेट लिंक प्राप्त करने के लिए अपना ईमेल पता दर्ज करें।"
            LoginStringKey.RESET_LINK_SENT -> "आपके ईमेल पर रीसेट लिंक भेजा गया है!"
            LoginStringKey.CANCEL -> "रद्द करें"
            LoginStringKey.SEND_LINK -> "लिंक भेजें"
        }
        Language.MARATHI -> when (key) {
            LoginStringKey.TITLE -> "मार्गदर्शक लॉगिन"
            LoginStringKey.SUBTITLE_LOGIN -> "मार्गदर्शकामध्ये प्रवेश करण्यासाठी तुमचे क्रेडेंशियल प्रविष्ट करा"
            LoginStringKey.SUBTITLE_SIGNUP -> "मार्गदर्शकामध्ये प्रवेश करण्यासाठी एक खाते तयार करा"
            LoginStringKey.EMAIL_LABEL -> "ईमेल पत्ता"
            LoginStringKey.EMAIL_PLACEHOLDER -> "yourname@email.com"
            LoginStringKey.PASSWORD_LABEL -> "पासवर्ड"
            LoginStringKey.PASSWORD_PLACEHOLDER -> "पासवर्ड प्रविष्ट करा"
            LoginStringKey.CONFIRM_PASSWORD_LABEL -> "पासवर्डची पुष्टी करा"
            LoginStringKey.CONFIRM_PASSWORD_PLACEHOLDER -> "तुमच्या पासवर्डची पुष्टी करा"
            LoginStringKey.LOGIN_BUTTON -> "लॉगिन"
            LoginStringKey.SIGNUP_BUTTON -> "साइन अप करा"
            LoginStringKey.SWITCH_TO_SIGNUP -> "खाते नाही? साइन अप करा"
            LoginStringKey.SWITCH_TO_LOGIN -> "आधीच खाते आहे? लॉगिन करा"
            LoginStringKey.ERROR_INVALID_EMAIL -> "कृपया वैध ईमेल पत्ता प्रविष्ट करा"
            LoginStringKey.ERROR_SHORT_PASSWORD -> "पासवर्ड किमान 6 अक्षरांचा असावा"
            LoginStringKey.ERROR_MATCH_PASSWORD -> "पासवर्ड जुळत नाहीत"
            LoginStringKey.SUCCESS_LOGIN -> "लॉगिन यशस्वी झाले!"
            LoginStringKey.SUCCESS_REGISTER -> "खाते यशस्वीरित्या तयार केले गेले!"
            LoginStringKey.FORGOT_PASSWORD -> "पासवर्ड विसरलात?"
            LoginStringKey.RESET_TITLE -> "पासवर्ड रीसेट करा"
            LoginStringKey.RESET_DESC -> "पासवर्ड रीसेट लिंक प्राप्त करण्यासाठी तुमचा ईमेल पत्ता प्रविष्ट करा."
            LoginStringKey.RESET_LINK_SENT -> "तुमच्या ईमेलवर रीसेट लिंक पाठवली आहे!"
            LoginStringKey.CANCEL -> "रद्द करा"
            LoginStringKey.SEND_LINK -> "लिंक पाठवा"
        }
    }
}

@Composable
fun LoginScreen(
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit,
    onLoginSuccess: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    var isSignUpMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Forgot Password State
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    // Dynamic Saffron Gradient: Soft Peach in Light mode, Dark Warm Charcoal in Dark mode
    val gradientColors = if (isDark) {
        listOf(
            Color(0xFF1E140C), // Dark Saffron Charcoal
            Color(0xFF2C190B), // Saffron Shadow Dark
            Color(0xFF3E2211)  // Dark Cocoa
        )
    } else {
        listOf(
            Color(0xFFFFF9F2), // Light Peach
            Color(0xFFFFE0B2), // Warm Peach Saffron
            Color(0xFFFFCC80)  // Golden Saffron
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = gradientColors))
    ) {
        // Neatly Aligned Top Bar with status bar padding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoginLanguageToggle(
                currentLanguage = currentLanguage,
                onLanguageChange = onLanguageChange,
                isDark = isDark
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp, bottom = 24.dp)
        ) {
            // App Logo as a beautiful rounded card badge
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2C2520) else Color.White
                ),
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_margdarshak),
                        contentDescription = "Margdarshak Logo",
                        modifier = Modifier.size(76.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = getLoginString(LoginStringKey.TITLE, currentLanguage),
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = Color(0xFFE65100),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = getLoginString(
                    if (isSignUpMode) LoginStringKey.SUBTITLE_SIGNUP else LoginStringKey.SUBTITLE_LOGIN,
                    currentLanguage
                ),
                fontSize = 13.sp,
                color = if (isDark) Color.LightGray else Color.DarkGray,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(getLoginString(LoginStringKey.EMAIL_LABEL, currentLanguage)) },
                placeholder = { Text(getLoginString(LoginStringKey.EMAIL_PLACEHOLDER, currentLanguage)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE65100),
                    unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFFE65100),
                    unfocusedLabelColor = if (isDark) Color.LightGray else Color.Gray,
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(getLoginString(LoginStringKey.PASSWORD_LABEL, currentLanguage)) },
                placeholder = { Text(getLoginString(LoginStringKey.PASSWORD_PLACEHOLDER, currentLanguage)) },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE65100),
                    unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFFE65100),
                    unfocusedLabelColor = if (isDark) Color.LightGray else Color.Gray,
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                )
            )

            // Forgot Password (only in login mode)
            if (!isSignUpMode) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = getLoginString(LoginStringKey.FORGOT_PASSWORD, currentLanguage),
                        color = Color(0xFFE65100),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .clickable { showForgotPasswordDialog = true }
                            .padding(4.dp)
                    )
                }
            }

            if (isSignUpMode) {
                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(getLoginString(LoginStringKey.CONFIRM_PASSWORD_LABEL, currentLanguage)) },
                    placeholder = { Text(getLoginString(LoginStringKey.CONFIRM_PASSWORD_PLACEHOLDER, currentLanguage)) },
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            Icon(
                                imageVector = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "Toggle password visibility",
                                tint = Color.Gray
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE65100),
                        unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.5f),
                        focusedLabelColor = Color(0xFFE65100),
                        unfocusedLabelColor = if (isDark) Color.LightGray else Color.Gray,
                        focusedTextColor = if (isDark) Color.White else Color.Black,
                        unfocusedTextColor = if (isDark) Color.White else Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button
            Button(
                onClick = {
                    val emailTrimmed = email.trim()
                    if (emailTrimmed.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches()) {
                        errorMessage = getLoginString(LoginStringKey.ERROR_INVALID_EMAIL, currentLanguage)
                    } else if (password.length < 6) {
                        errorMessage = getLoginString(LoginStringKey.ERROR_SHORT_PASSWORD, currentLanguage)
                    } else if (isSignUpMode && password != confirmPassword) {
                        errorMessage = getLoginString(LoginStringKey.ERROR_MATCH_PASSWORD, currentLanguage)
                    } else {
                        isLoading = true
                        errorMessage = ""
                        if (isSignUpMode) {
                            // Firebase Create User
                            auth.createUserWithEmailAndPassword(emailTrimmed, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            getLoginString(LoginStringKey.SUCCESS_REGISTER, currentLanguage),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onLoginSuccess()
                                    } else {
                                        errorMessage = task.exception?.localizedMessage ?: "Registration failed."
                                    }
                                }
                        } else {
                            // Firebase Sign In
                            auth.signInWithEmailAndPassword(emailTrimmed, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            getLoginString(LoginStringKey.SUCCESS_LOGIN, currentLanguage),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onLoginSuccess()
                                    } else {
                                        errorMessage = task.exception?.localizedMessage ?: "Login failed."
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = getLoginString(
                            if (isSignUpMode) LoginStringKey.SIGNUP_BUTTON else LoginStringKey.LOGIN_BUTTON,
                            currentLanguage
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Switch between Login and SignUp
            Text(
                text = getLoginString(
                    if (isSignUpMode) LoginStringKey.SWITCH_TO_LOGIN else LoginStringKey.SWITCH_TO_SIGNUP,
                    currentLanguage
                ),
                color = Color(0xFFE65100),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable {
                        isSignUpMode = !isSignUpMode
                        errorMessage = ""
                    }
                    .padding(8.dp)
            )
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        var resetEmail by remember { mutableStateOf(email) }
        var dialogError by remember { mutableStateOf("") }
        var isSendingReset by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Text(
                    text = getLoginString(LoginStringKey.RESET_TITLE, currentLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = getLoginString(LoginStringKey.RESET_DESC, currentLanguage),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    if (dialogError.isNotEmpty()) {
                        Text(
                            text = dialogError,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = {
                            resetEmail = it
                            dialogError = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(getLoginString(LoginStringKey.EMAIL_LABEL, currentLanguage)) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val emailTrimmed = resetEmail.trim()
                        if (emailTrimmed.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches()) {
                            dialogError = getLoginString(LoginStringKey.ERROR_INVALID_EMAIL, currentLanguage)
                        } else {
                            isSendingReset = true
                            auth.sendPasswordResetEmail(emailTrimmed)
                                .addOnCompleteListener { task ->
                                    isSendingReset = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            getLoginString(LoginStringKey.RESET_LINK_SENT, currentLanguage),
                                            Toast.LENGTH_LONG
                                        ).show()
                                        showForgotPasswordDialog = false
                                    } else {
                                        dialogError = task.exception?.localizedMessage ?: "Error sending reset email."
                                    }
                                }
                        }
                    },
                    enabled = !isSendingReset
                ) {
                    if (isSendingReset) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text(
                            text = getLoginString(LoginStringKey.SEND_LINK, currentLanguage),
                            color = Color(0xFFE65100),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showForgotPasswordDialog = false },
                    enabled = !isSendingReset
                ) {
                    Text(text = getLoginString(LoginStringKey.CANCEL, currentLanguage))
                }
            }
        )
    }
}

// Custom LanguageToggle that matches design and provides high visibility in light/dark system themes
@Composable
fun LoginLanguageToggle(
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val options = listOf(Language.ENGLISH, Language.HINDI, Language.MARATHI)
    val selectedIndex = options.indexOf(currentLanguage)

    val transition = updateTransition(targetState = selectedIndex, label = "langToggle")
    val slideOffset by transition.animateDp(
        transitionSpec = { spring(dampingRatio = 0.8f, stiffness = 300f) },
        label = "pillOffset"
    ) { index ->
        when (index) {
            0 -> 0.dp
            1 -> 56.dp
            else -> 112.dp
        }
    }

    // Toggle container color - high contrast to stay clearly visible
    val containerBg = if (isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.08f)
    val pillBg = if (isDark) Color(0xFF3E2D1C) else Color.White
    val activeText = Color(0xFFE65100)
    val inactiveText = if (isDark) Color.LightGray else Color.DarkGray

    Box(
        modifier = modifier
            .width(176.dp)
            .height(38.dp)
            .background(containerBg, RoundedCornerShape(19.dp))
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Highlight Pill
        Box(
            modifier = Modifier
                .offset(x = slideOffset)
                .width(56.dp)
                .fillMaxHeight()
                .background(pillBg, RoundedCornerShape(16.dp))
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
                    color = if (currentLanguage == Language.ENGLISH) activeText else inactiveText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
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
                    color = if (currentLanguage == Language.HINDI) activeText else inactiveText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
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
                    color = if (currentLanguage == Language.MARATHI) activeText else inactiveText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}
