package com.example.cryptotracker.data

// TypeScript: type Trend = "UP" | "DOWN" | "FLAT"
enum class Trend { UP, DOWN, FLAT }

// Решение задачи урока 3: куда сдвинулась цена с прошлого обновления
fun trendOf(oldPrice: Double, newPrice: Double): Trend = when {
    newPrice > oldPrice -> Trend.UP
    newPrice < oldPrice -> Trend.DOWN
    else -> Trend.FLAT
}

// Для каждой монеты сравниваем новую цену со старой.
// Монеты, которой раньше не было (первая загрузка), считаем FLAT.
fun trendsOf(old: List<Coin>, new: List<Coin>): Map<String, Trend> {
    val oldPrices = old.associate { it.id to it.price }
    return new.associate { coin ->
        val before = oldPrices[coin.id]
        coin.id to if (before == null) Trend.FLAT else trendOf(before, coin.price)
    }
}
