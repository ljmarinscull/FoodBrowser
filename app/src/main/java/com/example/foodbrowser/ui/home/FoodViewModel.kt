package com.example.foodbrowser.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodbrowser.domain.models.Food
import com.example.foodbrowser.domain.repository.IFoodRepository
import com.example.foodbrowser.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class FoodViewModel(
    private val repository: IFoodRepository
) : ViewModel() {

    private val _foods = MutableLiveData<Resource<List<Food>>>().apply {
        value = Resource.Success(listOf())
    }
    val foods: LiveData<Resource<List<Food>>> = _foods

    var job: Job? = null

    private val _eatToday = hashMapOf<String,Food>()
    val eatToday  = _eatToday

    fun searchFoodByName(query: String) {
        job?.cancel()
        if (query.length >= 3) {
            job = viewModelScope.launch {
                _foods.postValue(Resource.Loading)
                try {
                    val result = repository.searchFoodByName(query)
                    _foods.postValue(Resource.Success(result.getOrThrow()))
                } catch (e: Exception) {
                    _foods.postValue(Resource.Error(e))
                }
            }
        } else {
            _foods.postValue(Resource.Success(emptyList()))
        }
    }

    fun addEatFood(food: Food){
        _eatToday[getCurrentDate()] = food
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(time)
    }
}