package com.example.lifechanger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "quotes_table")
data class Quotes(

    // converting Json property names
    @PrimaryKey(autoGenerate = true)
    @Json(name = "ID")
    var id: Long = 0,

    @Json(name = "Quote")
    val quote: String,

    )