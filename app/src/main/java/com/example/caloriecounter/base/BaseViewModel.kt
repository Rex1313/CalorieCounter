package com.example.caloriecounter.base

import androidx.lifecycle.ViewModel
import com.example.caloriecounter.repository.CalorieCounterRepository

open class BaseViewModel: ViewModel() {
    val repository = CalorieCounterRepository


}