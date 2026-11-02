package com.example.cryptotracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.data.trendsOf
import com.example.cryptotracker.network.Network
import com.example.cryptotracker.network.toCoin
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

private const val REFRESH_MS = 30_000L // CoinGecko без ключа: около 5 запросов в минуту, нам хватает 2

@Composable
fun CryptoApp(modifier: Modifier = Modifier) {
    var state by remember { mutableStateOf<UiState>(UiState.Loading) }
    var reloadKey by remember { mutableIntStateOf(0) }

    // React:
    // useEffect(() => {
    //   load(); const id = setInterval(load, 30000)
    //   return () => clearInterval(id)
    // }, [reloadKey])
    // Здесь «clearInterval» не нужен: ушли с экрана — корутина отменится сама.
    LaunchedEffect(reloadKey) {
        while (true) {
            state = loadCoins(current = state)
            delay(REFRESH_MS)
        }
    }

    when (val s = state) {
        UiState.Loading -> LoadingScreen(modifier)
        is UiState.Error -> ErrorScreen(
            message = s.message,
            onRetry = {
                state = UiState.Loading
                reloadKey++ // меняем ключ → LaunchedEffect перезапускается, как deps в useEffect
            },
            modifier = modifier
        )
        is UiState.Success -> DashboardScreen(
            coins = s.coins,
            trends = s.trends,
            modifier = modifier
        )
    }
}

// async function loadCoins(): Promise<UiState>
private suspend fun loadCoins(current: UiState): UiState {
    val previous = (current as? UiState.Success)?.coins.orEmpty()
    return try {
        val coins = Network.api.getMarkets().map { it.toCoin() }
        UiState.Success(coins = coins, trends = trendsOf(previous, coins))
    } catch (e: CancellationException) {
        throw e // отмену корутины не глотаем: это не ошибка, а уход с экрана
    } catch (e: Exception) {
        // Если данные уже на экране, не прячем их из-за одного неудачного обновления
        if (current is UiState.Success) current else UiState.Error(errorMessage(e))
    }
}

private fun errorMessage(e: Exception): String = when {
    e is HttpException && e.code() == 429 ->
        "Слишком много запросов к CoinGecko. Подождите минуту и нажмите «Повторить»."
    e is HttpException -> "Сервер ответил ошибкой ${e.code()}."
    e is IOException -> "Нет соединения с интернетом."
    else -> "Что-то пошло не так: ${e.message}"
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

// Решение UI-задания урока 3: сообщение об ошибке и кнопка «Повторить»
@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onRetry) { Text("Повторить") }
        }
    }
}
