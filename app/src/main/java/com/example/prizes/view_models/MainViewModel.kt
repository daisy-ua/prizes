package com.example.prizes.view_models

import android.app.Application
import androidx.lifecycle.*
import com.example.prizes.data.entities.Prize
import com.example.prizes.data.local.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository: AppRepository = AppRepository(application)

    val prizes: LiveData<List<Prize>> = repository.getPrizes()

    fun deletePrize(prize: Prize) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePrize(prize)
    }

    fun insertPrize(title: String, price: Int) = viewModelScope.launch(Dispatchers.IO) {
        val prize = Prize(0, title, price)
        repository.insertPrize(prize)
    }
}