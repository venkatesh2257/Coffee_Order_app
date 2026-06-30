package com.example.test_1_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test_1_project.ui.theme.Test_1_projectTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val cartViewModel: CartViewModel = viewModel()
            val favoritesViewModel: FavoritesViewModel = viewModel()
            Test_1_projectTheme {
                CoffeeApp(cartViewModel, favoritesViewModel)
            }
        }
    }
}

@Composable
fun CoffeeApp(cartViewModel: CartViewModel, favoritesViewModel: FavoritesViewModel) {
    // Session Management: Check if user is already logged in
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

    // Listen for auth changes (like logout)
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    if (currentUser == null) {
        AuthScreen(onLoginSuccess = {
            currentUser = Firebase.auth.currentUser
        })
    } else {
        CoffeeScreen(cartViewModel, favoritesViewModel)
    }
}
