package com.example.prizes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prizes.data.entities.Prize

@Database(entities = [Prize::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun PrizeDao() : PrizeDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if(INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if(INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "prizes.db"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance()  { INSTANCE = null }
    }
}