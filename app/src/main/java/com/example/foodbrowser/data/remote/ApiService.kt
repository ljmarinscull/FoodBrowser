package com.example.foodbrowser.data.remote

import com.example.foodbrowser.data.models.FoodJO
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

fun interface ApiService {
    @GET("/dev/search")
    suspend fun searchFoodByName(@Query("kv") query: String): List<FoodJO>
}

class RetrofitClient {

    companion object {
        private var instance: ApiService? = null

        @Synchronized
        fun getInstance(): ApiService {
            if (instance == null) {
                val client = OkHttpClient()
                    .newBuilder()
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://uih0b7slze.execute-api.us-east-1.amazonaws.com")
                    .client(client)
                    .build()
                    .create(ApiService::class.java)
            }
            return instance as ApiService
        }
    }
}