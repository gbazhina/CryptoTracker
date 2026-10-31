package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

// Аналог в вебе: createRoot(document.getElementById("root")).render(<App />)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// <div style="display:flex; justify-content:center; align-items:center"> ... </div>
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // <div style="display:flex; flex-direction:column; gap:16px; align-items:center">
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Привет, крипто-трекер!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Ваш первый экран на Jetpack Compose")
            Spacer(modifier = Modifier.height(8.dp))
            Counter()
            FavoriteButton()
        }
    }
}

// React:
// function Counter() {
//   const [count, setCount] = useState(0)
//   return <button onClick={() => setCount(count + 1)}>Нажато: {count}</button>
// }
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Нажато: $count")
    }
}

// Решение UI-задания урока 1: кнопка «В избранное»
@Composable
fun FavoriteButton() {
    var isFavorite by remember { mutableStateOf(false) }
    // Звёзды — обычные символы: так не нужна отдельная библиотека иконок
    OutlinedButton(onClick = { isFavorite = !isFavorite }) {
        Text(if (isFavorite) "★ В избранном" else "☆ В избранное")
    }
}

// Аналог Storybook: превью без запуска эмулятора
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CryptoTrackerTheme {
        HomeScreen()
    }
}
