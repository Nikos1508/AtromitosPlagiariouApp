package com.example.atromitosplagiariouapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Announcements(
    val id: Int? = null,
    val title: String,
    val text: String
)