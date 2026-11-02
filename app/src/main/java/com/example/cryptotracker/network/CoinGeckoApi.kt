package com.example.cryptotracker.network

import com.example.cryptotracker.data.Coin
import com.example.cryptotracker.data.portfolioAmounts
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Как zod-схема или TS-тип ответа: описываем только нужные поля.
// Поля, помеченные в документации CoinGecko как nullable, — с "? = null".
@Serializable
data class CoinDto(
    val id: String,
    val symbol: String,
    val name: String,
    @SerialName("current_price") val currentPrice: Double? = null,
    @SerialName("price_change_percentage_24h_in_currency") val change24h: Double? = null,
    @SerialName("price_change_percentage_7d_in_currency") val change7d: Double? = null
)

// Retrofit-интерфейс ≈ API-слой на axios:
// const getMarkets = (params) => api.get("coins/markets", { params })
interface CoinGeckoApi {
    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids: String = portfolioAmounts.keys.joinToString(","),
        @Query("price_change_percentage") changes: String = "24h,7d"
    ): List<CoinDto>
}

// ≈ const api = axios.create({ baseURL: "https://api.coingecko.com/api/v3/" })
object Network {
    private val json = Json { ignoreUnknownKeys = true } // лишние поля ответа пропускаем

    val api: CoinGeckoApi = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/") // слэш в конце обязателен
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(CoinGeckoApi::class.java)
}

// Ответ сервера → наша модель для экрана
fun CoinDto.toCoin(): Coin = Coin(
    id = id,
    name = name,
    symbol = symbol.uppercase(),
    price = currentPrice ?: 0.0,
    change24h = change24h ?: 0.0,
    change7d = change7d ?: 0.0,
    amount = portfolioAmounts[id] ?: 0.0
)
