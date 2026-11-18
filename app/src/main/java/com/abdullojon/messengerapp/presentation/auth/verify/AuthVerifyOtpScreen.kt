package com.abdullojon.messengerapp.presentation.auth.verify

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

data class AuthVerifyOtpScreen(
    val phone: String
) : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<AuthVerifyOtpViewModel>()
        val state by viewModel.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(phone) {
            viewModel.init(phone)
        }

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is AuthVerifyOtpSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        AuthVerifyOtpContent(
            state = state,
            onCodeChange = { viewModel.onCodeChange(it) },
            onVerify = { viewModel.onVerify() },
            onResendCode = { viewModel.onResendCode() },
            onBack = { viewModel.onBack() }
        )
    }
}

@Composable
fun AuthVerifyOtpContent(
    state: AuthVerifyOtpState,
    onCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResendCode: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Center Phone Illustration Badge
                OtpPhoneIllustration()

                Spacer(modifier = Modifier.height(20.dp))

                // Header Title
                Text(
                    text = "Enter code",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subtitle
                Text(
                    text = "We’ve sent an SMS with an activation code to your phone\n${state.phone}.",
                    fontSize = 15.sp,
                    color = Color(0xFF757575),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 6-digit OTP Code Input Boxes
                OtpCodeInput(
                    code = state.code,
                    onCodeChange = onCodeChange
                )

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Resend Timer / Voice call text
                if (state.timerSeconds > 0) {
                    Text(
                        text = "You can request a voice call in ${state.formattedTimer}",
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                } else {
                    Text(
                        text = "Resend code",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B92E4),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .clickable { onResendCode() }
                    )
                }
            }

            // Next FAB Button at bottom right
            val isButtonEnabled = state.isCodeValid && !state.isLoading
            val buttonColor = if (isButtonEnabled) Color(0xFF2B92E4) else Color(0xFF2B92E4).copy(alpha = 0.5f)

            FloatingActionButton(
                onClick = {
                    if (isButtonEnabled) {
                        onVerify()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .size(56.dp),
                shape = RoundedCornerShape(50),
                containerColor = buttonColor,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Verify"
                    )
                }
            }
        }
    }
}

@Composable
private fun OtpPhoneIllustration() {
    Box(
        modifier = Modifier
            .size(68.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer Phone outline
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(56.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // Overlay Speech Bubble with ***
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp)
                .background(Color(0xFF2B92E4), shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Text(
                text = "***",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun OtpCodeInput(
    code: String,
    onCodeChange: (String) -> Unit
) {
    BasicTextField(
        value = code,
        onValueChange = onCodeChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(6) { index ->
                    val char = code.getOrNull(index)?.toString() ?: ""
                    val isFocused = code.length == index

                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(54.dp)
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = if (isFocused) Color(0xFF2B92E4) else Color(0xFFD1D5DB),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(Color.White, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1E1E)
                        )
                    }

                    if (index < 5) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AuthVerifyOtpScreenPreview() {
    AuthVerifyOtpContent(
        state = AuthVerifyOtpState(
            phone = "+998 938888331",
            code = "111"
        ),
        onCodeChange = {},
        onVerify = {},
        onResendCode = {},
        onBack = {}
    )
}
