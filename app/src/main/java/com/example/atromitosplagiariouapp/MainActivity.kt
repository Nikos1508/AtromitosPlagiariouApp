package com.example.atromitosplagiariouapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atromitosplagiariouapp.ui.items.HomeScreen
import com.example.atromitosplagiariouapp.ui.items.LoginScreen
import com.example.atromitosplagiariouapp.ui.items.SignUpScreen
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AtromitosPlagiariouAppTheme {
                AtromitosPlagiariouApp()
            }
        }
    }
}

enum class AppScreen {
    Login,
    SignUp,
    Home
}

@Composable
fun AtromitosPlagiariouApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.name
    ) {
        composable(AppScreen.Login.name) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(AppScreen.Home.name) },
                onSignUpClick = { navController.navigate(AppScreen.SignUp.name) }
            )
        }
        composable(AppScreen.SignUp.name) {
            SignUpScreen(
                onNavigateToLogin = { navController.navigate(AppScreen.Login.name) },
                onSignUpSuccess = { navController.navigate(AppScreen.Login.name) }
            )
        }
        composable(AppScreen.Home.name) {
            HomeScreen()
        }
    }
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