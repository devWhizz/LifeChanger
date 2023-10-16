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

    // create instance to access data from API
    private val repositoryQuotesApi = QuotesRepository(Api, quoteDatabase)

    private val repository = CategoryRepository()
    private val firestore = FirebaseFirestore.getInstance()

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

    // save user input - search query (SearchFragment)
    var lastSearchQuery: String = ""

    init {
        loadDonationsFromFirestore()
        loadQuotes()
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

    // liveData to notify FavoritesFragment about changes in liked donations
    private val _likedDonationsUpdated = MutableLiveData<Unit>()
    val likedDonationsUpdated: LiveData<Unit> get() = _likedDonationsUpdated

    // add liked donations to recyclerview (FavoritesFragment)
    fun addToLikedDonations(donation: Donation) {
        sharedPreferences.edit()
            .putBoolean(donation.id, true)
            .apply()

        Log.d("SharedViewModel", "Donation with ID ${donation.id} was added to SharedPreferences.")

        val currentLikedDonations = likedDonations.value ?: emptyList()
        val updatedLikedDonations = currentLikedDonations.toMutableList()
        updatedLikedDonations.add(donation)
        likedDonations.value = updatedLikedDonations
    }

    // remove liked donations from recyclerview (FavoritesFragment)
    fun removeFromLikedDonations(donation: Donation) {
        sharedPreferences.edit()
            .putBoolean(donation.id, false)
            .apply()

        Log.d(
            "SharedViewModel",
            "Donation with ID ${donation.id} was removed from SharedPreferences."
        )

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

    // retrieve donation from the repository based on its ID
    fun getDonationById(donationId: String): MutableLiveData<Donation?> {
        val liveData = MutableLiveData<Donation?>()

        // retrieve data from Firebase and insert it into MutableLiveData object
        firestore.collection("donations")
            .whereEqualTo("id", donationId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("Viewmodel", "Error getting donations by id", exception)
                } else {
                    var donation: Donation? = null
                    if (snapshot != null) {
                        for (document in snapshot) {
                            donation = document.toObject(Donation::class.java)
                            break
                        }
                    }
                    liveData.postValue(donation)
                }
            }
        return liveData
    }

    fun getPaypalEmailForDonation(donationId: String): MutableLiveData<String?> {
        val liveData = MutableLiveData<String?>()

        firestore.collection("donations")
            .whereEqualTo("id", donationId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("Viewmodel", "Error getting donations by id", exception)
                    liveData.postValue(null)
                } else {
                    var paypalEmail: String? = null
                    if (snapshot != null) {
                        for (document in snapshot) {
                            val donation = document.toObject(Donation::class.java)
                            paypalEmail = donation.payment
                            break
                        }
                    }
                    liveData.postValue(paypalEmail)
                }
            }
        return liveData
    }

    fun searchDonations(query: String): LiveData<List<Donation>> {
        val liveData = MutableLiveData<List<Donation>>()
        val results = mutableListOf<Donation>()

        donations.value?.let { allDonations ->
            for (donation in allDonations) {
                if (donation.title.contains(query, true) || donation.creator.contains(
                        query,
                        true
                    )
                ) {
                    results.add(donation)
                }
            }
        }
        liveData.postValue(results)
        return liveData
    }
}