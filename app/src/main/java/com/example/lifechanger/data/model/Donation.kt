package com.example.lifechanger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "donation_table")
data class Donation(

    // converting Json property names
    // create properties for class to be used in adapter


    @PrimaryKey(autoGenerate = true)
    @Json(name = "ID")
    var id: Long = 0,

    @Json(name = "Category")
    val category: String,

    @Json(name = "Donation Title")
    val title: String,

    @Json(name = "Donation Description")
    val description: String,

    @Json(name = "Company Name")
    val company: String,

    @Json(name = "Donation Image")
    val image: String,

    @Json(name = "Account Info")
    val payment: String

)