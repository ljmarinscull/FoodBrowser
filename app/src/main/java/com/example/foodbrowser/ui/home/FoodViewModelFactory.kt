package com.example.foodbrowser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodbrowser.domain.repository.IFoodRepository

class FoodViewModelFactory (
    private val repo : IFoodRepository
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
                return FoodViewModel(repository = repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }