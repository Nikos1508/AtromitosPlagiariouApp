package com.example.atromitosplagiariouapp.ui.items

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atromitosplagiariouapp.R
import com.example.atromitosplagiariouapp.SupabaseAuthViewModel
import com.example.atromitosplagiariouapp.data.model.UserState
import com.example.atromitosplagiariouapp.ui.composables.InputField
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    viewModel: SupabaseAuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val userState = viewModel.userState.value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailFocused by remember { mutableStateOf(false) }
    var passwordFocused by remember { mutableStateOf(false) }
    var loginAttemptMade by remember { mutableStateOf(false) }

    val loginSuccessMsg = "You're logged in!"
    val emptyFieldsMessage = "Please enter both email and password."

    val loginFailedWithMessage = when (userState) {
        is UserState.Error -> "Login failed: ${userState.message}"
        else -> ""
    }

    LaunchedEffect(userState, loginAttemptMade) {
        if (loginAttemptMade) {
            when (userState) {
                is UserState.Success -> {
                    if (userState.message == loginSuccessMsg) {
                        Toast.makeText(context, loginSuccessMsg, Toast.LENGTH_SHORT).show()
                        loginAttemptMade = false
                        onLoginSuccess()
                    }
                }

                is UserState.Error -> {
                    Toast.makeText(context, loginFailedWithMessage, Toast.LENGTH_LONG).show()
                    loginAttemptMade = false
                }

                UserState.Loading -> { }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.atromitos_plagiariou),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Σύνδεση",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                label = "Email",
                placeholder = "Email",
                value = email,
                onValueChange = { email = it },
                isFocused = emailFocused,
                onFocusChanged = { emailFocused = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                label = "Κωδικός",
                placeholder = "Κωδικός",
                value = password,
                onValueChange = { password = it },
                isFocused = passwordFocused,
                onFocusChanged = { passwordFocused = it },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        loginAttemptMade = true
                        viewModel.login(context, email.trim(), password.trim())
                    } else {
                        Toast.makeText(context, emptyFieldsMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                enabled = userState != UserState.Loading || !loginAttemptMade,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (userState == UserState.Loading && loginAttemptMade) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Δημιουργία λογαριασμού")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onSignUpClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Δεν έχετε λογαριασμό? Εγγραφείτε")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AtromitosPlagiariouLoginScreenPreviewLight() {
    AtromitosPlagiariouAppTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AtromitosPlagiariouLoginpScreenPreviewDark() {
    AtromitosPlagiariouAppTheme(darkTheme = true) {
        LoginScreen()
    }
}