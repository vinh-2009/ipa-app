package com.example.corelockultra.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.corelockultra.theme.BackgroundDark
import com.example.corelockultra.theme.SecondaryBlue
import com.example.corelockultra.theme.TextGray
import com.example.corelockultra.theme.TextWhite
import com.example.corelockultra.ui.screens.HomeScreen
import com.example.corelockultra.ui.screens.LoginScreen
import com.example.corelockultra.ui.screens.OptimizerMenuScreen

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val isLoading by viewModel.isLoadingAuth.collectAsState()
    
    if (isLoading) {
        // Simple loading screen
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator(color = SecondaryBlue)
        }
        return
    }

    if (!isAuthenticated) {
        LoginScreen(viewModel = viewModel)
        return
    }

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = BackgroundDark) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Trang chủ") },
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SecondaryBlue,
                        unselectedIconColor = TextGray,
                        selectedTextColor = SecondaryBlue,
                        unselectedTextColor = TextGray,
                        indicatorColor = BackgroundDark
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = currentRoute == "chat",
                    onClick = { /* TODO */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SecondaryBlue,
                        unselectedIconColor = TextGray,
                        selectedTextColor = SecondaryBlue,
                        unselectedTextColor = TextGray,
                        indicatorColor = BackgroundDark
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Admin") },
                    label = { Text("Admin") },
                    selected = currentRoute == "admin",
                    onClick = { /* TODO */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SecondaryBlue,
                        unselectedIconColor = TextGray,
                        selectedTextColor = SecondaryBlue,
                        unselectedTextColor = TextGray,
                        indicatorColor = BackgroundDark
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding).background(BackgroundDark)) {
            composable("home") { 
                HomeScreen(viewModel = viewModel, onNavigateToOptimizer = { navController.navigate("optimizer") }) 
            }
            composable("optimizer") { 
                OptimizerMenuScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() }) 
            }
            composable("chat") { 
                Text("Chat Member Screen", color = TextWhite)
            }
            composable("admin") { 
                Text("Admin Screen", color = TextWhite)
            }
        }
    }
}
