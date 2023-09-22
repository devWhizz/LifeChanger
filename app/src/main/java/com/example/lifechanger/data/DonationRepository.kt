package com.example.lifechanger.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lifechanger.api.Api
import com.example.lifechanger.data.model.Donation

class DonationRepository(val donationApi: Api, private val database: DonationDatabase) {

    // livedata to create list of Donation-objekts which will be observed in fragment
    private val _donation: MutableLiveData<List<Donation>> = MutableLiveData<List<Donation>>()
    val donation: LiveData<List<Donation>>
        get() = _donation

    // asynchronous loading fun to update livedata and saving/writing possibly changed data
    suspend fun loadDonation() {
        try {
            val donations = donationApi.retrofitService.getDonation()
            _donation.value = donations
            database.donationDao.insertAll(donations)
        } catch (e: Exception) {
            Log.e("Repository", "Error while loading $e")
        }
    }

    // insert single donation into database
    fun insertItem(data: Donation) {
        try {
            database.donationDao.insertItem(data)
        } catch (e: Exception) {
            Log.d("Repository", "Error in database: $e")
        }
    }

    // retrieve all donations stored in database and returns them as LiveData
    fun getAllDonationFromDatabase(): LiveData<List<Donation>> {
        return database.donationDao.getAll()
    }

    // retrieve donations from database by category and return results as LiveData
    fun getDonationsByCategory(category: String): LiveData<List<Donation>> {
        return database.donationDao.getDonationsByCategory(category)
    }

    // retrieve total number of donation stored in database
    fun getCount(): Int {
        return database.donationDao.count()
    }
}