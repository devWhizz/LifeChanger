package com.example.lifechanger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lifechanger.api.Api
import com.example.lifechanger.data.CategoryRepository
import com.example.lifechanger.data.QuotesRepository
import com.example.lifechanger.db.getQuoteDatabase
import com.example.lifechanger.data.model.Category
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.data.model.Quotes
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val quoteDatabase = getQuoteDatabase(application)

    private val repository = CategoryRepository()
    private val firestore = FirebaseFirestore.getInstance()

    // create instance to access data from API
    private val repositoryQuotesApi = QuotesRepository(Api, quoteDatabase)

    // create instances to access data from repository
//    val donation = repositoryQuotesApi.getAllQuotesFromDatabase()

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

    init {
        loadDonationsFromFirestore()
        loadQuotes()
    }

    private val _donations = MutableLiveData<List<Donation>>()
    val donations: LiveData<List<Donation>> get() = _donations

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
    // load data from Firebase into repository
    private fun loadDonationsFromFirestore() {
        firestore.collection("donations")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.e("TAG", "Error getting donations", e)
                    return@addSnapshotListener
                }

                val donations = mutableListOf<Donation>()
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val donation = document.toObject(Donation::class.java)
                        donations.add(donation)
                    }
                }
                _donations.value = donations
            }
    }

    // liked donations
    private val likedDonations = MutableLiveData<List<Donation>>()
//    private val likedDonations: LiveData<List<Donation>> = _likedDonations

    // liveData to notify FavoritesFragment about changes in liked donations
    private val _likedDonationsUpdated = MutableLiveData<Unit>()
    val likedDonationsUpdated: LiveData<Unit> get() = _likedDonationsUpdated

    // add liked donations to Recyclerview (FavoritesFragment)
    fun addToLikedDonations(donation: Donation) {
        sharedPreferences.edit()
            .putBoolean(donation.id, true)
            .apply()

        Log.d("SharedViewModel", "Donation with ID ${donation.id} was added to liked donations.")

        val currentLikedDonations = likedDonations.value ?: emptyList()
        val updatedLikedDonations = currentLikedDonations.toMutableList()
        updatedLikedDonations.add(donation)
        likedDonations.value = updatedLikedDonations
    }

    // remove liked donations to Recyclerview (FavoritesFragment)
    fun removeFromLikedDonations(donation: Donation) {
        sharedPreferences.edit()
            .putBoolean(donation.id, false)
            .apply()

        Log.d("SharedViewModel", "Donation with ID ${donation.id} was removed from liked donations.")

        val currentLikedDonations = likedDonations.value ?: emptyList()
        val updatedLikedDonations = currentLikedDonations.toMutableList()
        updatedLikedDonations.remove(donation)
        likedDonations.value = updatedLikedDonations

        // notify FavoritesFragment about changes
        _likedDonationsUpdated.value = Unit
    }

    fun isLiked(donationId: String): Boolean {
        return sharedPreferences.getBoolean(donationId, false)
    }

    fun getLikedDonationIds(): Set<String> {
        return sharedPreferences.all
            .filter { it.value as Boolean }
            .map { it.key }
            .toSet()
    }

    fun getDonationsByIds(ids: Set<String>): List<Donation> {
        return donations.value.orEmpty()
            .filter { it.id in ids }
    }

    // retrieve donations based on a specific category from the repository
    fun getDonationByCategory(category: String): LiveData<List<Donation>> {
        val liveData = MutableLiveData<List<Donation>>()

        // retrieve data from Firebase and insert it into MutableLiveData object.
        firestore.collection("donations")
            .whereEqualTo("category", category)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("Viewmodel", "Error getting donations by category", exception)
                } else {
                    val donations = mutableListOf<Donation>()
                    if (snapshot != null) {
                        for (document in snapshot) {
                            val donation = document.toObject(Donation::class.java)
                            donations.add(donation)
                        }
                    }
                    liveData.postValue(donations)
                }
            }

        return liveData
    }

    // starting Coroutines to run processes while viewmodel is active
    // load data from API in repository
    private fun loadQuotes() {
        viewModelScope.launch {
            try {
                Log.d("SharedViewModel", "Loading Quote Data from API.")
                repositoryQuotesApi.loadQuotes()
                Log.d("SharedViewModel", "Quote Data loaded successfully.")
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error while loading $e")
            }
        }
    }

    fun getRandomQuote(): LiveData<Quotes> {
        return repositoryQuotesApi.getRandomEntry()
    }
}