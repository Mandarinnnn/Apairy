package com.example.apairy.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "hives_table")
@Parcelize
data class Hive(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "frame") val frame: Int?,
    @ColumnInfo(name = "honey") val honey: Int?,
    @ColumnInfo(name = "strength") val strength: Int?,
    @ColumnInfo(name = "weight") val weight: Int?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "queen") val queen: String?,
    @ColumnInfo(name = "imageURI") val imageURI: String?,
) : Parcelable
