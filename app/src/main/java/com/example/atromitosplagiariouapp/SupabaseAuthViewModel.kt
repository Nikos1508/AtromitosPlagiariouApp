package com.example.atromitosplagiariouapp

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atromitosplagiariouapp.data.model.UserState
import com.example.atromitosplagiariouapp.data.network.SupabaseClient.client
import com.example.atromitosplagiariouapp.utils.SharedPreferenceHelper
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.github.jan.supabase.auth.providers.builtin.Email

class SupabaseAuthViewModel : ViewModel() {

    private val _userState = mutableStateOf<UserState>(UserState.Success(""))
    val userState: State<UserState> = _userState

    private val _currentUserEmail = mutableStateOf<String?>(null)
    val currentUserEmail: State<String?> = _currentUserEmail


    private suspend fun saveTokens(context: Context) = withContext(Dispatchers.IO) {
        val session = client.auth.currentSessionOrNull()
        val sharedPref = SharedPreferenceHelper(context)
        sharedPref.saveStringData("accessToken", session?.accessToken)
        sharedPref.saveStringData("refreshToken", session?.refreshToken)
        Log.d("AuthVM", "Tokens saved.")
    }


    private suspend fun clearTokens(context: Context) = withContext(Dispatchers.IO) {
        val sharedPref = SharedPreferenceHelper(context)
        sharedPref.clearPreferences()
        Log.d("AuthVM", "Tokens cleared.")
    }

    fun loadCurrentUserEmail() {
        viewModelScope.launch {
            val user = client.auth.currentUserOrNull()
            _currentUserEmail.value = user?.email
        }
    }

    suspend fun refreshSessionManually(context: Context): Boolean = withContext(Dispatchers.IO) {
        val sharedPref = SharedPreferenceHelper(context)
        val refreshToken = sharedPref.getStringData("refreshToken")

        if (refreshToken.isNullOrEmpty()) {
            _userState.value = UserState.Success("User not logged in!")
            return@withContext false
        }

        return@withContext try {
            client.auth.refreshCurrentSession()
            val currentUser = client.auth.currentUserOrNull()
            if (currentUser != null) {
                saveTokens(context)
                withContext(Dispatchers.Main) { loadCurrentUserEmail() }
                _userState.value = UserState.Success("User already logged in!")
                true
            } else {
                clearTokens(context)
                withContext(Dispatchers.Main) { _currentUserEmail.value = null }
                _userState.value = UserState.Success("User not logged in!")
                false
            }
        } catch (e: Exception) {
            clearTokens(context)
            withContext(Dispatchers.Main) { _currentUserEmail.value = null }
            _userState.value = UserState.Error("Session expired. Please log in.")
            false
        }
    }


    fun login(context: Context, userEmail: String, userPassword: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                saveTokens(context)
                loadCurrentUserEmail()
                _userState.value = UserState.Success("Logged in successfully!")

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is RestException -> e.error ?: "Login failed. Invalid credentials or server error."
                    is HttpRequestException -> "Network error during login. Please check your connection."
                    else -> e.message ?: "Unknown login error."
                }
                _userState.value = UserState.Error(errorMessage)
            }
        }
    }

    fun signUp(context: Context, userEmail: String, userPassword: String, onNavigateToLogin: () -> Unit) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val result = client.auth.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                saveTokens(context)
                loadCurrentUserEmail()
                _userState.value = UserState.Success("Registered successfully!")
                onNavigateToLogin()

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is RestException -> e.error ?: "Registration failed. Server error."
                    is HttpRequestException -> "Network error during registration. Please check connection."
                    else -> e.message ?: "Unknown registration error."
                }
                _userState.value = UserState.Error(errorMessage)
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.auth.signOut()
                clearTokens(context)
                _currentUserEmail.value = null
                _userState.value = UserState.Success("Logged out successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Logout failed.")
            }
        }
    }

    /* For later, but it was easy so I wrote the code from now */
    fun isUserLoggedIn(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            val success = refreshSessionManually(context)
            if (success) {
                _userState.value = UserState.Success("User logged in!")
            } else {
                clearTokens(context)
                _currentUserEmail.value = null
                _userState.value = UserState.Success("User not logged in!")
            }
        }
    }
}