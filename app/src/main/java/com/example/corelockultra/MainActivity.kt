package com.example.corelockultra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.corelockultra.theme.CorelockUltraTheme
import com.example.corelockultra.ui.main.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CorelockUltraTheme {
                MainScreen()
            }
        }
    }
}
