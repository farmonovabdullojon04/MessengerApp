package com.abdullojon.messengerapp.presentation.auth.phone

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class AuthPhoneScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<AuthPhoneViewModel>()
        val state by viewModel.collectAsState()
        val context = LocalContext.current

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is AuthPhoneSideEffect.OpenTelegramBot -> {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sideEffect.url))
                        context.startActivity(intent)
                    } catch (_: Exception) {
                        Toast.makeText(context, "Could not open Telegram bot", Toast.LENGTH_SHORT).show()
                    }
                }
                is AuthPhoneSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        AuthPhoneContent(
            state = state,
            onPhoneNumberChange = { viewModel.onPhoneNumberChange(it) },
            onSubmit = { viewModel.onSubmit() },
            onCountryClick = {
                Toast.makeText(context, "Uzbekistan selected", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun AuthPhoneContent(
    state: AuthPhoneState,
    onPhoneNumberChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCountryClick: () -> Unit
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
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(64.dp))

                // Title
                Text(
                    text = "Your phone number",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = "Please confirm your country code\nand enter your phone number.",
                    fontSize = 15.sp,
                    color = Color(0xFF757575),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(36.dp))

                // 1. Country Selection Field
                CountrySelectorField(
                    countryFlag = state.countryFlag,
                    countryName = state.countryName,
                    onClick = onCountryClick
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Phone Number Input Field
                PhoneNumberInputField(
                    countryCode = state.countryCode,
                    phoneNumber = state.phoneNumber,
                    onPhoneNumberChange = onPhoneNumberChange,
                    onSubmit = onSubmit
                )

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // 3. Floating Action Button at bottom right
            val isButtonEnabled = state.isPhoneValid && !state.isLoading
            val buttonColor = if (isButtonEnabled) Color(0xFF2B92E4) else Color(0xFF2B92E4).copy(alpha = 0.5f)

            FloatingActionButton(
                onClick = {
                    if (isButtonEnabled) {
                        onSubmit()
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
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
}

@Composable
private fun CountrySelectorField(
    countryFlag: String,
    countryName: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = countryFlag,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = countryName,
                    fontSize = 16.sp,
                    color = Color(0xFF1E1E1E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Select country",
                    tint = Color(0xFF9CA3AF)
                )
            }
        }

        // Floating label "Country" on top edge border
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .background(Color.White)
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = "Country",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun PhoneNumberInputField(
    countryCode: String,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused) Color(0xFF2B92E4) else Color(0xFFD1D5DB)
    val labelColor = if (isFocused) Color(0xFF2B92E4) else Color(0xFF6B7280)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .border(
                    width = if (isFocused) 1.5.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Prefix: "+ 998"
                Text(
                    text = "$countryCode ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1E1E1E)
                )

                // Vertical Divider "|"
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                        .background(Color(0xFF2B92E4))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Number Input field with custom Uzbek visual transformation
                BasicTextField(
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF1E1E1E),
                        letterSpacing = 0.5.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onSubmit() }
                    ),
                    visualTransformation = UzbekPhoneVisualTransformation(),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (phoneNumber.isEmpty()) {
                                Text(
                                    text = "00 000 00 00",
                                    fontSize = 16.sp,
                                    color = Color(0xFF9CA3AF),
                                    letterSpacing = 0.5.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // Floating label "Phone number" on top edge border
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .background(Color.White)
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = "Phone number",
                fontSize = 12.sp,
                color = labelColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthPhoneScreenPreview() {
    AuthPhoneContent(
        state = AuthPhoneState(
            phoneNumber = "900000001"
        ),
        onPhoneNumberChange = {},
        onSubmit = {},
        onCountryClick = {}
    )
}
