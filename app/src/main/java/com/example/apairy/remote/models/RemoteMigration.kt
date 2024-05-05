package com.example.apairy.remote.models

data class RemoteMigration(
    val id: String,
    val name: String,
    val hiveCount: Int,
    val startDate: String,
    val endDate: String?,
    val latitude: Double?,
    val longitude: Double?,
    val note: String?,
    val imageURI: String?
)
