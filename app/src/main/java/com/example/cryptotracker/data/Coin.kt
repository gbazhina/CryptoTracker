package com.example.cryptotracker.data

// TypeScript: type Coin = { id: string; name: string; symbol: string; price: number; ... }
data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val price: Double,
    val change24h: Double,
    val change7d: Double,
    val amount: Double // сколько монет в портфеле
)

// Аналог в вебе: mock-данные в файле coins.json. В уроке 3 они придут из сети.
// Цены условные, только для вёрстки.
val sampleCoins = listOf(
    Coin("bitcoin", "Bitcoin", "BTC", 67250.40, 2.35, -1.20, 0.05),
    Coin("ethereum", "Ethereum", "ETH", 3480.15, -0.84, 4.10, 0.8),
    Coin("solana", "Solana", "SOL", 162.70, 5.62, 12.45, 10.0),
    Coin("toncoin", "Toncoin", "TON", 5.48, -2.10, -6.30, 150.0),
    Coin("cardano", "Cardano", "ADA", 0.452, 0.0, 1.75, 2000.0),
    Coin("dogecoin", "Dogecoin", "DOGE", 0.158, 8.90, 21.30, 5000.0),
    Coin("polkadot", "Polkadot", "DOT", 6.92, -1.45, -3.80, 120.0),
    Coin("litecoin", "Litecoin", "LTC", 81.30, 1.12, -0.40, 4.0)
)

// Аналог: coins.reduce((sum, c) => sum + c.price * c.amount, 0)
fun portfolioBalance(coins: List<Coin>): Double =
    coins.sumOf { it.price * it.amount }
