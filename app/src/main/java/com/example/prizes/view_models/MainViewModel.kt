package com.example.prizes.view_models

import android.app.Application
import androidx.lifecycle.*
import com.example.prizes.data.entities.Prize
import com.example.prizes.data.local.AppRepository
import com.example.prizes.utils.PriceController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository: AppRepository = AppRepository(application)
    private val priceController = PriceController()

    val prizes: LiveData<List<Prize>> = repository.getPrizes()

    private val _insertedItem = MutableLiveData<Pair<String, Int>>()
    val insertedItem: LiveData<Pair<String, Int>> get() = _insertedItem

    fun insertItem(pair: Pair<String, Int>) {
        _insertedItem.value = pair
    }


    fun deletePrize(prize: Prize) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePrize(prize)
    }

    fun insertPrize(title: String, price: Int) = viewModelScope.launch(Dispatchers.IO) {
        val prize = Prize(0, title, price)
        repository.insertPrize(prize)
    }

    private var _totalAmount = MutableLiveData(0)
    val totalAmount get() = _totalAmount

    private fun setTotalAmount(price: Int) {
        _totalAmount.value = price
    }

    fun onItemUncheckCallback(item: Prize, position: Int) {
        item.price?.let { price ->
            totalAmount.value?.minus(price)
                ?.let { total -> setTotalAmount(total) }
            priceController.removeItem(position, item)
        }
    }

    fun onItemCheckCallback(item: Prize, position: Int) {
        item.price?.let { price ->
            totalAmount.value?.plus(price)
                ?.let { total -> setTotalAmount(total) }
            priceController.addItem(position, item)
        }
    }

    fun checkSelection() : List<Pair<Int, Prize>> {
        return totalAmount.value?.let { priceController.checkSelection(it) } ?: listOf()
    }
}