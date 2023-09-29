package com.example.lifechanger.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lifechanger.data.model.Quotes

@Database(entities = [Quotes::class], version = 1)
abstract class QuotesDatabase : RoomDatabase() {

    abstract val quotesDao: QuotesDao

}

private lateinit var dbInstance: QuotesDatabase

fun getQuoteDatabase(context: Context): QuotesDatabase {
    synchronized(QuotesDatabase::class.java) {
        // initialize database if not already initialized
        if (!::dbInstance.isInitialized) {
            dbInstance = Room.databaseBuilder(
                context.applicationContext,
                QuotesDatabase::class.java,
                "quotes_database"
            ).allowMainThreadQueries().build()
        }
        return dbInstance
    }
}