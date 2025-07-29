package com.example.atromitosplagiariouapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Championship(
    val id: Int? = null,
    val team: String,
    val points: Int,
    val goals: String,
    val wins: Int,
    val draws: Int,
    val loses: Int
)