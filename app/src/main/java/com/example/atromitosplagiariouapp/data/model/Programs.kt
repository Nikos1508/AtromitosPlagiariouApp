package com.example.atromitosplagiariouapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Programs(
    val id: Int? = null,
    val group: String,
    val day: String,
    val timestart: String,
    val timeend: String
)