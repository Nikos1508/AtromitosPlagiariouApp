package com.example.atromitosplagiariouapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Programs (
    val id: Long,
    val group: String,
    val day: String,
    @SerialName("time-start")
    val timeStart: String,
    @SerialName("time-end")
    val timeEnd: String
)