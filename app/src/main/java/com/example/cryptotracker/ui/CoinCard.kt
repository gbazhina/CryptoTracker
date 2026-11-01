package com.example.cryptotracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.data.Coin
import com.example.cryptotracker.data.sampleCoins
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

val TrendUp = Color(0xFF16A34A)   // зелёный
val TrendDown = Color(0xFFDC2626) // красный

// React: function CoinCard({ coin, change, onClick }) { ... }
@Composable
fun CoinCard(
    coin: Coin,
    change: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Card(onClick = ...) сам даёт Ripple — волну от точки касания. CSS не нужен.
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),           // border-radius: 16px
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // display: flex; align-items: center; gap: 12px; padding: 16px
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CoinAvatar(symbol = coin.symbol)

            // flex: 1 — колонка с названием занимает всё свободное место
            Column(modifier = Modifier.weight(1f)) {
                Text(coin.name, fontWeight = FontWeight.SemiBold)
                Text(
                    coin.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // text-align: right
            Column(horizontalAlignment = Alignment.End) {
                Text(formatPrice(coin.price), fontWeight = FontWeight.SemiBold)
                Text(
                    formatChange(change),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (change >= 0) TrendUp else TrendDown
                )
            }
        }
    }
}

// Кружок с первой буквой тикера вместо логотипа: логотипы монет — чужие знаки
@Composable
fun CoinAvatar(symbol: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)                       // border-radius: 50%
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            symbol.first().toString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CoinCardPreview() {
    CryptoTrackerTheme {
        CoinCard(coin = sampleCoins.first(), change = 2.35, onClick = {})
    }
}
