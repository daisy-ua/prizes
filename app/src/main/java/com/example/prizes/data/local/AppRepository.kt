package com.example.prizes.data.local

import android.app.Application
import com.example.prizes.data.entities.Prize

class AppRepository(application: Application) {
    private var prizeDao: PrizeDao

    init {
        val db = AppDatabase.getInstance(application)
        prizeDao = db.PrizeDao()
    }

    fun getPrizes() = prizeDao.getPrizes()

    suspend fun insertPrize(prize: Prize) = prizeDao.insertPrize(prize)

    suspend fun deletePrize(prize: Prize) = prizeDao.deletePrize(prize)
}