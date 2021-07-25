package com.example.prizes.callbacks

import com.example.prizes.data.entities.Prize

interface OnItemCheckListener {
    fun onItemCheck(item: Prize, position: Int)
    fun onItemUncheck(item: Prize, position: Int)
}