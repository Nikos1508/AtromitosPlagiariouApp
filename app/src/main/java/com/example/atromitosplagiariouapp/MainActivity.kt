package com.example.atromitosplagiariouapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.atromitosplagiariouapp.ui.items.SignUpScreen
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtromitosPlagiariouAppTheme {
                AtromitosPlagiariouApp()
            }
        }
    }
}


@Composable
fun AtromitosPlagiariouApp() {
    SignUpScreen()
}

@Preview(showBackground = true)
@Composable
fun AtromitosPlagiariouAppPreviewLight() {
    AtromitosPlagiariouAppTheme(darkTheme = false){
        AtromitosPlagiariouApp()
    }
}

@Preview(showBackground = true)
@Composable
fun AtromitosPlagiariouAppPreviewDark() {
    AtromitosPlagiariouAppTheme(darkTheme = true){
        AtromitosPlagiariouApp()
    }
}