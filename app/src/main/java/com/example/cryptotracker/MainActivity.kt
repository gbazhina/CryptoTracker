package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.cryptotracker.data.sampleCoins
import com.example.cryptotracker.ui.DashboardScreen
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

// Урок 2: вместо счётчика из урока 1 показываем Dashboard.
// Аналог: createRoot(...).render(<Dashboard coins={mockCoins} />)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DashboardScreen(
                        coins = sampleCoins,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
