package com.example.apairy.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize



@Entity(tableName = "mistakes_table")
@Parcelize
data class Mistake(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "solution") val solution: String?,
    @ColumnInfo(name = "year") val year: String?,
): Parcelable
