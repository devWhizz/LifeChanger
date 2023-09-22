package com.example.lifechanger.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lifechanger.data.model.Donation

@Dao
interface DonationDao {

    // insert list of _donation objects into donation_table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(donationList : List<Donation>)

    // update single Donation object in donation_table
    @Update
    fun update(donation: Donation)

    // retrieve all Donation objects from donation_table as LiveData
    @Query("SELECT * FROM donation_table")
    fun getAll(): LiveData<List<Donation>>

    // retrieve count of records in donation_table
    @Query("SELECT COUNT(*) FROM donation_table")
    fun count(): Int

    // insert single Donation object into donation_table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(itemData: Donation)

    // retrieve donations from database based on their category
    @Query("SELECT * FROM donation_table WHERE Category = :category")
    fun getDonationsByCategory(category: String): LiveData<List<Donation>>

}