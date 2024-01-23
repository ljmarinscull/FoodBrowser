package com.example.foodbrowser.domain.models

data class Food (
    val id: Long,
    val brand: String,
    val name: String,
    val calories: Long,
    val portion: Long
)