package com.example.foodbrowser.domain.repository

import com.example.foodbrowser.domain.models.Food

fun interface IFoodRepository {
    suspend fun searchFoodByName(query: String): Result<List<Food>>
}