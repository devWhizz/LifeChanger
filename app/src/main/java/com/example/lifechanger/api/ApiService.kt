package com.example.lifechanger.api

import com.example.lifechanger.data.model.Quotes
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// setting BASEURL
val BASE_URL = "https://64fecdcef8b9eeca9e2918da.mockapi.io/api/"

// implementing moshi
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// implementing retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    //GET method to load API parameters saved in dataclass (Quotes)
    @GET("quotes")
    suspend fun getQuotes(): List<Quotes>

}

// access retrofit services
object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}