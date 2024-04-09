package com.example.apairy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Theory(
    val image_id: Int,
    val title: String,
    val category: Int
): Parcelable
