package com.example.atromitosplagiariouapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.atromitosplagiariouapp.ui.composables.AppTopBar
import com.example.atromitosplagiariouapp.ui.items.AnnouncementsScreen
import com.example.atromitosplagiariouapp.ui.items.ChampionshipScreen
import com.example.atromitosplagiariouapp.ui.items.HomeScreen
import com.example.atromitosplagiariouapp.ui.items.LoginScreen
import com.example.atromitosplagiariouapp.ui.items.ProgramsRoute
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
    Home,
    Announcements,
    Programs,
    Championship
}

@Composable
fun AtromitosPlagiariouApp() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val canNavigateBack = navController.previousBackStackEntry != null

    val screenTitle = when (currentDestination) {
        AppScreen.Home.name -> "Ατρόμητος Πλαγιαρίου App"
        AppScreen.Announcements.name -> "Aνακοινώσεις"
        AppScreen.Programs.name -> "Προγράμματα"
        AppScreen.Championship.name -> "Πρωτάθλημα"
        else -> "Ατρόμητος Πλαγιαρίου App"
    }

    Scaffold(
        topBar = {
            if (currentDestination != AppScreen.Login.name && currentDestination != AppScreen.SignUp.name) {
                AppTopBar(
                    title = screenTitle,
                    canNavigateBack = canNavigateBack,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Login.name) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppScreen.Home.name) {
                            popUpTo(AppScreen.Login.name) { inclusive = true }
                        }
                    },
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
                HomeScreen(
                    onNavigateToAnnouncements = { navController.navigate(AppScreen.Announcements.name) },
                    onNavigateToPrograms = { navController.navigate(AppScreen.Programs.name) },
                    onNavigateToChampionship = { navController.navigate(AppScreen.Championship.name) }
                )
            }
            composable(AppScreen.Announcements.name) {
                AnnouncementsScreen()
            }
            composable(AppScreen.Programs.name) {
                ProgramsRoute()
            }
            composable(AppScreen.Championship.name) {
                ChampionshipScreen()
            }
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