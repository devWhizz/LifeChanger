package com.example.lifechanger.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lifechanger.data.model.Quotes

@Dao
interface QuotesDao {

    // insert list of _quotes objects into quotes_table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(quoteList: List<Quotes>)

    // update single Quote object in quotes_table
    @Update
    fun update(quote: Quotes)

    // retrieve all Quotes objects from quotes_table as LiveData
    @Query("SELECT * FROM quotes_table")
    fun getAll(): LiveData<List<Quotes>>

    // retrieve count of records in quotes_table
    @Query("SELECT COUNT(*) FROM quotes_table")
    fun count(): Int

    @Query("SELECT * FROM quotes_table ORDER BY RANDOM() LIMIT 1")
    fun getRandomEntry(): LiveData<Quotes>

    // insert single Quotes object into quotes_table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(itemData: Quotes)

}