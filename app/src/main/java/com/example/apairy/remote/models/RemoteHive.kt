package com.example.apairy.remote.models

data class RemoteHive(
    val id: String,
    val name: String,
    val frameCount: Int,
    val queenYear: String,
    val note: String,
    val isMarked: Boolean,
)
