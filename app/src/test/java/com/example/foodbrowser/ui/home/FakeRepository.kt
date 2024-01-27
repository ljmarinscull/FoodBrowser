package com.example.foodbrowser.ui.home

import com.example.foodbrowser.domain.models.Food
import com.example.foodbrowser.domain.repository.IFoodRepository

class FakeRepository : IFoodRepository {
    val foods = listOf(
        Food(1,"","Chicken",1000,1000),
        Food(2,"","Chicken BBQ",1000,1000)
    )
    override suspend fun searchFoodByName(query: String) = Result.success(foods)

}