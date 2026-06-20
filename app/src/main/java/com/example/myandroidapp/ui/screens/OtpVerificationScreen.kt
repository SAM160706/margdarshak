package com.example.myandroidapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationScreen(
    mobileNumber: String,
    verificationId: String,
    onVerificationSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var resendTimer by remember { mutableStateOf(60) }

    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    LaunchedEffect(key1 = resendTimer) {
        if (resendTimer > 0) {
            delay(1000L)
            resendTimer -= 1
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9F2),
                        Color(0xFFFFE0B2),
                        Color(0xFFFFCC80)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Verify OTP",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = Color(0xFFE65100),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Enter the 6-digit code sent to +91 $mobileNumber",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // OTP Input Field
            OutlinedTextField(
                value = otpCode,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        otpCode = it
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Verification Code") },
                placeholder = { Text("6-digit OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE65100),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFFE65100)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button
            Button(
                onClick = {
                    if (otpCode.length != 6) {
                        errorMessage = "Please enter a valid 6-digit OTP code"
                        return
                    }
                    isLoading = true
                    errorMessage = ""

                    // If instant verification succeeded and verificationId is empty, route directly
                    if (verificationId.isEmpty()) {
                        isLoading = false
                        onVerificationSuccess()
                        return
                    }

                    val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                onVerificationSuccess()
                            } else {
                                errorMessage = task.exception?.message ?: "Incorrect OTP code. Try again."
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
                    Text("Verify & Login", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBackToLogin) {
                    Text("Back to Login", color = Color(0xFFE65100))
                }

                if (resendTimer > 0) {
                    Text(
                        text = "Resend OTP in ${resendTimer}s",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                } else {
                    TextButton(onClick = {
                        resendTimer = 60
                        Toast.makeText(context, "OTP Resent", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Resend OTP", color = Color(0xFFE65100), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
