package com.example.lifechanger.data

data class TranslationResponse(
    val translations: List<Translation>
)

data class Translation(
    val text: String,
    val detectedSourceLanguage: String = "de"
)