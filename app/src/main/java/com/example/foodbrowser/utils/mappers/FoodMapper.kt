package com.example.foodbrowser.utils.mappers

import com.example.foodbrowser.data.models.FoodJO
import com.example.foodbrowser.domain.models.Food

object FoodMapper : IMapper<FoodJO,Food> {
    override fun map(input: FoodJO) = Food (
        id = input.id,
        brand = input.brand,
        name = input.name,
        calories = input.calories,
        portion = input.portion,
    )
}