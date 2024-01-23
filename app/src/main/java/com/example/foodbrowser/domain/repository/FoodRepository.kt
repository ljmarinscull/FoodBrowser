package com.example.foodbrowser.domain.repository

import com.example.foodbrowser.data.remote.ApiService
import com.example.foodbrowser.domain.models.Food
import com.example.foodbrowser.utils.mappers.FoodMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException

class FoodRepository(
    private val api: ApiService,
    private val foodMapper: FoodMapper
) : IFoodRepository {

    private val UNKNOWN_ERROR = "Unknown error"

    override suspend fun searchFoodByName(query: String): Result<List<Food>> {
        return coroutineScope {
            val searchResult = async {
                try {
                    Result.success(api.searchFoodByName(query))
                } catch (e: retrofit2.HttpException) {
                    Result.failure(e)
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }

            val foundFood = searchResult.await().getOrNull()
            if (foundFood != null) {
                Result.success(
                    foundFood.map(foodMapper::map)
                )
            } else {
                Result.failure(
                    searchResult.await().exceptionOrNull() ?: Exception(UNKNOWN_ERROR)
                )
            }
        }
    }
}