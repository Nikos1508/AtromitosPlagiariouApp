package com.example.atromitosplagiariouapp.ui.items

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
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
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit,
    viewModel: SupabaseAuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val userState = viewModel.userState.value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailFocused by remember { mutableStateOf(false) }
    var passwordFocused by remember { mutableStateOf(false) }
    var signUpAttemptMade by remember { mutableStateOf(false) }

    val successMsg = "Δημιουργία λογαριασμού έγινε με επιτυχεία"
    val emptyFieldsMessage = "Παρακαλώ εισάγεται email και κωδικό"

    val signUpFailedWithMessage = when (userState) {
        is UserState.Error -> "Αποτυχεία εγγραφής: ${userState.message}"
        else -> ""
    }

    LaunchedEffect(userState, signUpAttemptMade) {
        if (signUpAttemptMade) {
            when (userState) {
                is UserState.Success -> {
                    if (userState.message == successMsg) {
                        Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                        signUpAttemptMade = false
                        onSignUpSuccess()
                    }
                }

                is UserState.Error -> {
                    Toast.makeText(context, signUpFailedWithMessage, Toast.LENGTH_LONG).show()
                    signUpAttemptMade = false
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
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Εγγραφή",
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
                        signUpAttemptMade = true
                        viewModel.signUp(context, email.trim(), password.trim(), onNavigateToLogin)
                    } else {
                        Toast.makeText(context, emptyFieldsMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                enabled = userState != UserState.Loading || !signUpAttemptMade,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (userState == UserState.Loading && signUpAttemptMade) {
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
                onClick = onNavigateToLogin,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Έχετε ήδη λογαριασμό? Συνδεθείτε")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AtromitosPlagiariouSignUpScreenPreviewLight() {
    AtromitosPlagiariouAppTheme(darkTheme = false) {
        SignUpScreen(
            onNavigateToLogin = {},
            onSignUpSuccess = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AtromitosPlagiariouSignUpScreenPreviewDark() {
    AtromitosPlagiariouAppTheme(darkTheme = true) {
        SignUpScreen(
            onNavigateToLogin = {},
            onSignUpSuccess = {}
        )
    }
}