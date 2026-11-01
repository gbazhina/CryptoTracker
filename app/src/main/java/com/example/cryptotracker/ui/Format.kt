package com.example.cryptotracker.ui

import java.util.Locale
import kotlin.math.abs
import kotlin.math.round

// Из урока 1: "$1,234.50". Аналог: Intl.NumberFormat("en-US", { style: "currency", currency: "USD" })
fun formatPrice(price: Double): String =
    String.format(Locale.US, "$%,.2f", price)

// Решение задачи урока 2: "+2.35%", "-1.20%", "0.00%"
// Сначала округляем до сотых: -0.004 должно стать "0.00%", а не "-0.00%"
fun formatChange(percent: Double): String {
    val rounded = round(percent * 100) / 100
    return when {
        rounded > 0 -> String.format(Locale.US, "+%.2f%%", rounded)
        rounded < 0 -> String.format(Locale.US, "-%.2f%%", abs(rounded))
        else -> "0.00%"
    }
}
