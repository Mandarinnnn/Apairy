package com.example.apairy.remote.models

data class RemoteHiveState(
    val id: String,
    val hiveId: String,
    val strength: Int,
    val frame: Int,
    val honey: Float,
    val date: String
)
