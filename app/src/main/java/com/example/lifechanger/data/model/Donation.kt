package com.example.lifechanger.data.model

data class Donation (

    val id: String = "",
    val category: String = "",
    val title: String = "",
    val description: String = "",
    val creator: String = "",
    val image: String = "",
    val payment: String = "",
    var isLiked: Boolean = false

)
