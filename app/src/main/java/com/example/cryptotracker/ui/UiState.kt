package com.example.cryptotracker.ui

import com.example.cryptotracker.data.Coin
import com.example.cryptotracker.data.Trend

// Веб-паттерн { data, error, isLoading }, но компилятор заставит в when
// обработать все три варианта — как exhaustive-проверка discriminated union в TS
sealed interface UiState {
    data object Loading : UiState
    data class Success(
        val coins: List<Coin>,
        val trends: Map<String, Trend> = emptyMap()
    ) : UiState
    data class Error(val message: String) : UiState
}
