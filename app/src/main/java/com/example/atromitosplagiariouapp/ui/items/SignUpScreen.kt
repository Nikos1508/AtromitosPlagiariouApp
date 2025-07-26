package com.example.atromitosplagiariouapp.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

@Composable
fun SignUpScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Εγγραφή", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        //Email Field
        BasicTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                .padding(12.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (email.isEmpty()) Text("Email", color = MaterialTheme.colorScheme.onSurfaceVariant)
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Password Field
        BasicTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                .padding(12.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                if (password.isEmpty()) Text("Κωδικός", color = MaterialTheme.colorScheme.onSurfaceVariant)
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* TO DO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Δημιουργεία λογαριασμού")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* TO DO */ }) {
            Text("Ήδη εγγεγραμένος? Συνδέσου")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    AtromitosPlagiariouAppTheme {
        SignUpScreen()
    }
}