package com.example.prizes.utils

import android.util.Log
import com.example.prizes.data.entities.Prize

class PriceController {
    private var TARGET: Int = 100
    private var selectedItemsPrice = mutableListOf<Prize>()
    private var selectedItemsIndex = mutableListOf<Int>()

    private var currentlySelected: Prize? = null

    fun addItem(position: Int, prize: Prize) {
        currentlySelected = prize
        selectedItemsPrice.add(prize)
        selectedItemsIndex.add(position)
    }

    fun removeItem(position: Int, prize: Prize? = null) {
        selectedItemsPrice.remove(prize)
        selectedItemsIndex.remove(position)
    }

    fun checkSelection(_amount: Int) : List<Pair<Int, Prize>> {
        var amount = _amount
        val positions = mutableListOf<Pair<Int, Prize>>()

        while (amount > TARGET && selectedItemsPrice.size > 0) {
            val target = amount - TARGET
            val price = findClosest(selectedItemsPrice.dropLast(1), target)
            val prize = selectedItemsPrice.find{ it.price == price } as Prize
            val index = selectedItemsPrice.indexOf(prize)
            val position = selectedItemsIndex[index]

            Log.i("data", "$price $index")
            positions.add(Pair(position, prize))
            removeItem(position, prize)

            amount -= price
        }
            return positions
    }

    private fun findClosest(arr: List<Prize>, target: Int) : Int {
        val prices = mutableListOf<Int>()
        var max = 0

        for (item in arr) {
            val price = item.price.toString().toInt()
            if (max < price)
                max = price
            if (target < price) {
                prices.add(price)
            }
        }

        return if (prices.isEmpty()) max
        else prices.minOrNull() ?: 0
    }
}