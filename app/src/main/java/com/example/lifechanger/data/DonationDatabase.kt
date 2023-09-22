package com.example.lifechanger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.db.DonationDao

@Database(entities = [Donation::class], version = 1)
abstract class DonationDatabase : RoomDatabase() {

    abstract val donationDao: DonationDao

}

private lateinit var dbInstance: DonationDatabase

fun getDonationDatabase(context: Context): DonationDatabase {
    synchronized(DonationDatabase::class.java) {
        // Initialize database if not already initialized
        if (!::dbInstance.isInitialized) {
            dbInstance = Room.databaseBuilder(
                context.applicationContext,
                DonationDatabase::class.java,
                "donation_database"
            ).allowMainThreadQueries().build()
        }
        return dbInstance
    }
}