package com.example.foodbrowser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodbrowser.domain.models.Food
import com.example.foodbrowser.domain.repository.IFoodRepository
import com.example.foodbrowser.utils.Resource
import kotlinx.coroutines.launch

class FoodViewModel(
    private val repository: IFoodRepository
) : ViewModel() {

    private val _foods = MutableLiveData<Resource<List<Food>>>().apply {
        value = Resource.Success(listOf())
    }
    val foods: LiveData<Resource<List<Food>>> = _foods

    fun searchFoodByName(query: String) {
        if (query.length < 3) return
        viewModelScope.launch {
            _foods.postValue(Resource.Loading)
            try {
                val result = repository.searchFoodByName(query)
                _foods.postValue(Resource.Success(result.getOrThrow()))
            } catch (e: Exception) {
                _foods.postValue(Resource.Error(e))
            }
        }
    }
}