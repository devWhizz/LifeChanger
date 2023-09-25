package com.example.lifechanger

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lifechanger.api.Api
import com.example.lifechanger.data.CategoryRepository
import com.example.lifechanger.data.DonationRepository
import com.example.lifechanger.data.model.Category
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.data.getDonationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val donationDatabase = getDonationDatabase(application)

    private val repository = CategoryRepository()

    // create instance to access data from API
    private val repositoryDonationApi = DonationRepository(Api, donationDatabase)

    // create instances to access data from repository
    val donation = repositoryDonationApi.getAllDonationFromDatabase()

    init {
        loadDonation()
    }

    // list of Environment category elements for HomeFragment
    private val _environmentCategory: MutableLiveData<List<Category>> =
        MutableLiveData(repository.loadEnvironmentCategory())
    val environmentCategory: LiveData<List<Category>>
        get() = _environmentCategory

    // list of Animal category elements for HomeFragment
    private val _animalCategory: MutableLiveData<List<Category>> =
        MutableLiveData(repository.loadAnimalCategory())
    val animalCategory: LiveData<List<Category>>
        get() = _animalCategory

    // list of People category elements for HomeFragment
    private val _peopleCategory: MutableLiveData<List<Category>> =
        MutableLiveData(repository.loadPeopleCategory())
    val peopleCategory: LiveData<List<Category>>
        get() = _peopleCategory

    // list of development aid category elements for HomeFragment
    private val _developmentAidCategory: MutableLiveData<List<Category>> =
        MutableLiveData(repository.loadDevelopmentAidCategory())
    val developmentAidCategory: LiveData<List<Category>>
        get() = _developmentAidCategory


    // starting Coroutines to run processes while viewmodel is active
    // load data from API in repository
    private fun loadDonation() {

        viewModelScope.launch {
            Log.d("SharedViewModel", "Loading Donation Data from API.")
            repositoryDonationApi.loadDonation()
            Log.d("SharedViewModel", "Donation Data loaded successfully.")
        }
    }

    fun getTableCount(): Int {
        return repositoryDonationApi.getCount()
    }

    fun insertToDb(data: Donation): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            Log.d("SharedViewModel", "Inserting Donation Data into Database.")
            repositoryDonationApi.insertItem(data)
            Log.d("SharedViewModel", "Donation Data inserted successfully.")
        }
    }

    // retrieve donations based on a specific category from the repository
    fun getDonationByCategory(category: String): LiveData<List<Donation>> {
        return repositoryDonationApi.getDonationsByCategory(category)
    }
}