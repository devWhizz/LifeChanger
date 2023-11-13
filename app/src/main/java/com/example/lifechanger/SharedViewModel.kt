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
import com.example.lifechanger.api.DeepLApiService
import com.example.lifechanger.api.retrofitDeepL
import com.example.lifechanger.data.CategoryRepository
import com.example.lifechanger.data.QuotesRepository
import com.example.lifechanger.data.TranslationResponse
import com.example.lifechanger.db.getQuoteDatabase
import com.example.lifechanger.data.model.Category
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.data.model.Quotes
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    //region QUOTES

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

    //endregion

    //region CATEGORIES

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

    //endregion

    //region DONATIONS

    private val _donations = MutableLiveData<List<Donation>>()
    val donations: LiveData<List<Donation>> get() = _donations

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

    // retrieve Paypal email address from Firebase
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

    //endregion

    //region SEARCH FEATURE

    // search through donations
    fun searchDonations(query: String): LiveData<List<Donation>> {
        val liveData = MutableLiveData<List<Donation>>()
        val results = mutableListOf<Donation>()

        donations.value?.let { allDonations ->
            for (donation in allDonations) {
                if (donation.title.contains(query, true) ||
                    donation.creator.contains(query, true) ||
                    donation.category.contains(query, true)
                ) {
                    results.add(donation)
                }
            }
        }
        liveData.postValue(results)
        return liveData
    }

    //endregion

    //region LIKE FEATURE

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

    //endregion

    //region TRANSLATION FEATURE

    // get language status
    fun getTargetLanguage(): String {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("SharedPreferencesLanguage", Context.MODE_PRIVATE)
        return sharedPrefs.getString("targetLanguage", "de") ?: "de"
    }

    // translate method using Deepl API
    fun translateText(text: String, targetLang: String): LiveData<String> {
        Log.d("TranslateText", "Translating text: $text to target language: $targetLang")
        val result = MutableLiveData<String>()

        val deepLApiKey = ApiConfig.deepLApiKey
        val deepLApiService: DeepLApiService by lazy { retrofitDeepL.create(DeepLApiService::class.java) }

        val call = deepLApiService.translateText(deepLApiKey, text, targetLang)

        call.enqueue(object : Callback<TranslationResponse> {
            override fun onResponse(
                call: Call<TranslationResponse>,
                response: Response<TranslationResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    result.postValue(response.body()!!.translations.first().text)
                    Log.d(
                        "TranslateText",
                        "Translation successful: ${response.body()!!.translations.first().text}"
                    )
                }
            }

            override fun onFailure(call: Call<TranslationResponse>, t: Throwable) {
                result.postValue(text)
                Log.e("TranslateText", "Translation failed: $t")
            }
        })
        return result
    }

    // translate donation titles
    fun translateDonationTitle(donation: Donation, targetLang: String): LiveData<String> {
        val result = MutableLiveData<String>()
        translateText(donation.title, targetLang).observeForever { translatedTitle ->
            result.value = translatedTitle
        }
        return result
    }

    // ### NOT IN USE DUE TO CHARACTER SAVING ###
    // translate donation descriptions
    fun translateDonationDescription(donation: Donation, targetLang: String): LiveData<String> {
        val result = MutableLiveData<String>()
        translateText(donation.description, targetLang).observeForever { translatedDescription ->
            result.value = translatedDescription
        }
        return result
    }

    //endregion

    // get dark mode status
    fun isDarkModeEnabled(): Boolean {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("SharedPreferencesDarkMode", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("darkModeEnabled", false)
    }
}