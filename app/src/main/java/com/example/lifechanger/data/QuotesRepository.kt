package com.example.lifechanger.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lifechanger.api.Api
import com.example.lifechanger.data.model.Quotes
import com.example.lifechanger.db.QuotesDatabase

class QuotesRepository(private val quotesApi: Api, private val database: QuotesDatabase) {

    // livedata to create list of Quote objects which will be observed in fragment
    private val _quotes: MutableLiveData<List<Quotes>> = MutableLiveData<List<Quotes>>()

    // asynchronous loading fun to update livedata and saving/writing possibly changed data
    suspend fun loadQuotes() {
        try {
            val quotes = quotesApi.quotesApiService.getQuotes()
            _quotes.value = quotes
            insertQuotesIntoDatabase(quotes)
        } catch (e: Exception) {
            Log.e("Repository", "Error while loading $e")
        }
    }

    // insert quotes into Room database
    private fun insertQuotesIntoDatabase(quotes: List<Quotes>) {
        try {
            database.quotesDao.insertAll(quotes)
        } catch (e: Exception) {
            Log.d("Repository", "Error in database: $e")
        }
    }

    fun getRandomEntry(): LiveData<Quotes> {
        return database.quotesDao.getRandomEntry()
    }
}