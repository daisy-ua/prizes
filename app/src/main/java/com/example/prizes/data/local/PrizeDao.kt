package com.example.prizes.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.prizes.data.entities.Prize

@Dao
interface PrizeDao {
    @Query("SELECT * FROM prizes")
    fun getPrizes() : LiveData<List<Prize>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrize(prize: Prize)

    @Delete
    suspend fun deletePrize(prize: Prize)
}