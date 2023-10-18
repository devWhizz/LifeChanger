package com.example.lifechanger.api

import com.example.lifechanger.ApiConfig
import com.example.lifechanger.data.TranslationResponse
import com.example.lifechanger.data.model.Quotes
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// implementing moshi
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// implementing retrofit for Quotes
val retrofitQuotes: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://64fecdcef8b9eeca9e2918da.mockapi.io/api/")
    .build()

// implementing retrofit for deepL
val retrofitDeepL: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://api-free.deepl.com/v2/")
    .build()

interface QuotesApiService {
    //GET method to load API parameters saved in dataclass (Quotes)
    @GET("quotes")
    suspend fun getQuotes(): List<Quotes>

}

interface DeepLApiService {
    @POST("translate")
    fun translateText(
        @Query("auth_key") authKey: String = ApiConfig.deepLApiKey,
        @Query("text") textToTranslate: String,
        @Query("target_lang") targetLang: String
    ): Call<TranslationResponse>

}

// access retrofit services
object Api {
    val quotesApiService: QuotesApiService by lazy { retrofitQuotes.create(QuotesApiService::class.java) }
}