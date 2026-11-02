package com.example.cryptotracker.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.data.Coin
import com.example.cryptotracker.data.Trend
import com.example.cryptotracker.data.portfolioBalance
import com.example.cryptotracker.data.sampleCoins
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

// Период для процента изменения. TypeScript: type Period = "24h" | "7d"
enum class Period(val label: String) { DAY("24ч"), WEEK("7д") }

@Composable
fun DashboardScreen(
    coins: List<Coin>,
    modifier: Modifier = Modifier,
    trends: Map<String, Trend> = emptyMap()
) {
    var period by remember { mutableStateOf(Period.DAY) }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    // React: <ul>{coins.map(c => <CoinCard key={c.id} />)}</ul>,
    // но с виртуализацией из коробки, как react-window
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)   // gap: 12px
    ) {
        item {
            PortfolioHeader(
                balance = portfolioBalance(coins),
                period = period,
                onPeriodChange = { period = it }
            )
        }
        // Условие бесплатного API CoinGecko: заметная подпись-ссылка рядом с данными
        item {
            Text(
                "Данные предоставлены CoinGecko",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { uriHandler.openUri("https://www.coingecko.com/") }
            )
        }
        items(coins, key = { it.id }) { coin ->
            CoinCard(
                coin = coin,
                trend = trends[coin.id] ?: Trend.FLAT,
                change = if (period == Period.DAY) coin.change24h else coin.change7d,
                onClick = {
                    Toast.makeText(context, coin.name, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

// Бонус-задание урока 2: шапка с балансом и переключателем периода
@Composable
fun PortfolioHeader(
    balance: Double,
    period: Period,
    onPeriodChange: (Period) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Баланс портфеля",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            formatPrice(balance),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        PeriodToggle(selected = period, onSelect = onPeriodChange)
    }
}

// Переключатель-«таблетка»: две кнопки в одном ряду, активная подсвечена
@Composable
fun PeriodToggle(selected: Period, onSelect: (Period) -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Period.entries.forEach { p ->
            val isActive = p == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onSelect(p) }               // тоже даёт Ripple
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    p.label,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    CryptoTrackerTheme {
        DashboardScreen(coins = sampleCoins)
    }
}
